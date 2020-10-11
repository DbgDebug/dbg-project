package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.service.bilibili.pojo.*;
import club.dbg.cms.util.ZLibUtils;
import club.dbg.cms.util.bilibili.DanmuPatternUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

/**
 * @author ---
 */
public class DanmuReceiveThread implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(DanmuReceiveThread.class);

    private final BiliBiliServiceImpl biliBiliService;

    private final MessageHandleService messageHandleService;

    private final HeartBeatScheduled heartBeatTask;

    private final BiliBiliApi biliBiliApi;

    private final int id;

    private final int roomId;

    private Socket socket = null;

    private Boolean isShutdown = false;

    /**
     * 数据输出流，用于发送数据包
     */
    private DataOutputStream dataOutputStream;

    DanmuReceiveThread(BiliBiliServiceImpl bilibiliService,
                       MessageHandleService messageHandleService,
                       RoomInfo roomInfo, HeartBeatScheduled heartBeatTask,
                       BiliBiliApi biliBiliApi) {
        this.biliBiliService = bilibiliService;
        this.messageHandleService = messageHandleService;
        this.id = roomInfo.getId();
        this.roomId = roomInfo.getRoomid();
        this.heartBeatTask = heartBeatTask;
        this.biliBiliApi = biliBiliApi;
    }

    @Override
    public void run() {
        createSocket();
    }

    private void createSocket() {

        DanmuConf danmuConf = biliBiliApi.getDanmuConf(roomId);
        if (danmuConf == null) {
            biliBiliService.stop(id);
            return;
        }
        int count = 0;
        while (count < 3) {
            count++;
            try {
                // 连接
                socket = new Socket(danmuConf.getHost(), danmuConf.getPort());
                // 获取数据输出流
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                // 获取输出流
                InputStream inputStream = socket.getInputStream();
                // 发送加入信息
                sendJoinMsg(roomId, danmuConf.getToken());
                log.info("房间{}已经连接", roomId);
                // 心跳包
                heartBeatTask.submit(this);
                // 接收信息处理
                inputStreamHandle(inputStream);
            } catch (Exception e) {
                if (!isShutdown) {
                    log.error("Exception:", e);
                    log.info("房间{}已退出", roomId);
                } else {
                    count = 3;
                }
            }
        }
        if (!isShutdown) {
            biliBiliService.stop(id);
        }
    }

    /**
     * 头部长度为16个字节
     * 数据长度（包含头部长度） int
     * 头部长度 short int
     * 协议 protocol short int
     * 操作码 action int
     * 协议 sequence int
     */
    private void inputStreamHandle(InputStream inputStream) throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            // 开始读取数据流，先开辟缓存区
            int headerSize = 16;
            byte[] buf = new byte[headerSize];
            int count = headerSize;
            // 已经成功读取的字节的个数
            int readCount = 0;
            while (readCount < count) {
                readCount += inputStream.read(buf, readCount, count - readCount);
            }
            ByteBuffer byteBuffer = ByteBuffer.wrap(buf);
            // 头部的长度数据
            int length = byteBuffer.getInt();
            short hs = byteBuffer.getShort();
            // 如果长度小于等于 16，谨防万一，加一个上限
            if (length <= headerSize || length > 65534) {
                continue;
            }
            // 剔除头部长度，进行读取
            byte[] bodyByte = new byte[length - headerSize];
            count = length - headerSize;
            // 已经成功读取的字节的个数
            readCount = 0;
            while (readCount < count) {
                readCount += inputStream.read(bodyByte, readCount, count - readCount);
            }
            byte[] bytes = new byte[length];
            System.arraycopy(buf, 0, bytes, 0, headerSize);
            System.arraycopy(bodyByte, 0, bytes, headerSize, bodyByte.length);
            byteHandle(bytes);
        }
    }

    private void byteHandle(byte[] byteData) {
        int lengthSum = 0;
        while (lengthSum < byteData.length) {
            byte[] headerByte = new byte[16];
            System.arraycopy(byteData, lengthSum, headerByte, 0, headerByte.length);
            ByteBuffer byteBuffer = ByteBuffer.wrap(headerByte);
            int length = byteBuffer.getInt();
            // 头大小
            byteBuffer.getShort();
            int protocol = byteBuffer.getShort();
            int action = byteBuffer.getInt();
            // int sequence = byteBuffer.getInt();
            byte[] bytes = new byte[length - 16];
            System.arraycopy(byteData, lengthSum + 16, bytes, 0, bytes.length);
            switch (action) {
                case 5:
                    if (protocol == 2) {
                        byteHandle(ZLibUtils.decompress(bytes));
                    } else {
                        actionFive(bytes);
                    }
                    break;
                case 8:
                    sendHeartBeat();
                    break;
            }
            lengthSum += length;
        }
    }

    private void actionFive(byte[] bodyByte) {
        String msg = new String(bodyByte, StandardCharsets.UTF_8);
        String msgType = "";
        Matcher mCmd = DanmuPatternUtils.readCmd.matcher(msg);
        if (mCmd.find()) {
            msgType = mCmd.group(1);
        }
        switch (msgType) {
            case "DANMU_MSG":
                try {
                    messageHandleService.danmuMsg(roomId, msg);
                } catch (InterruptedException ie) {
                    log.info("弹幕处理异常:", ie);
                }
                break;
            case "SEND_GIFT":
                try {
                    messageHandleService.gifMsg(roomId, msg);
                } catch (InterruptedException ie) {
                    log.info("礼物处理异常:", ie);
                }
                break;
            case "WELCOME":
                // welcomeMsg(bodyString);
                break;
            case "GUARD_MSG":
                log.info("舰长消息:{}", msg);
                break;
            case "GUARD_BUY":
                log.info("购买舰长信息:{}", msg);
                try {
                    messageHandleService.guardMsg(roomId, msg);
                } catch (Exception e) {
                    log.warn("舰长开通处理异常:", e);
                }
                break;
            case "ROOM_SILENT_OFF":
                // log.info("直播结束roomId:{}", roomId);
                // bilibiliService.stop(id);
                break;
            default:
                break;
        }
    }

    /**
     * 首次连接发送的认证数据
     *
     * @param roomId 真实的直播房间 ID
     */
    private void sendJoinMsg(Integer roomId, String token) {
        // 生成随机的 UID
        long clientId = System.currentTimeMillis();
        // 发送验证包
        sendDataPack(7,
                String.format("{\"uid\": %d, " +
                        "\"roomid\": %d, " +
                        "\"protover\": 1, " +
                        "\"platform\": \"web\", " +
                        "\"clientver\": \"1.8.2\", " +
                        "\"type\": 2, " +
                        "\"key\": \"%s\"}", clientId, roomId, token));
    }

    /**
     * 客户端发送的心跳包，30 秒发送一次
     */
    protected void sendHeartBeat() {
        sendDataPack(2, "");
    }

    /**
     * 固定的发包方法
     *
     * @param action 操作码，可选 2,3,5,7,8
     * @param body   发送的数据本体部分
     */
    private void sendDataPack(int action, String body) {
        try {
            // 数据部分，以 UTF-8 编码解析成 Byte
            byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

            // 封包总长度，因为头部固定为 16 字长，故加上 16
            int length = bodyBytes.length + 16;

            // 为缓冲区分配长度
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);

            // 存入 4 字长的封包总大小数据，所以为 int
            byteBuffer.putInt(length);

            // 存入 2 字长的头部长度数据，头部长度固定为 16
            byteBuffer.putShort((short) 16);

            // 存入 2 字长的协议版本数据，默认为 1
            byteBuffer.putShort((short) 1);

            // 存入 4 字长的操作码，操作码有 2,3,5,7,8
            byteBuffer.putInt(action);

            // 存入 4 字长的 sequence，意味不明，取常数 1
            byteBuffer.putInt(1);

            // 存入数据
            byteBuffer.put(bodyBytes);

            // 写入输出数据流中
            dataOutputStream.write(byteBuffer.array());
        } catch (IOException e) {
            biliBiliService.stop(id);
            log.error("数据发送失败，roomId:" + this.roomId, e);
        }
    }

    public void closeNow() {
        this.isShutdown = true;
        heartBeatTask.cancel(this.id);
        try {
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }
}
