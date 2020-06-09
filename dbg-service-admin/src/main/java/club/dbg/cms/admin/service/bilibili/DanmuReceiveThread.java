package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.service.bilibili.pojo.*;
import club.dbg.cms.domain.admin.DanmuDO;
import club.dbg.cms.domain.admin.GiftDO;
import club.dbg.cms.domain.admin.GuardDO;
import club.dbg.cms.util.ZLibUtils;
import club.dbg.cms.util.bilibili.DanmuPatternUtils;
import org.apache.commons.lang3.RandomUtils;
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

    private final HeartBeatTask heartBeatTask;

    private final BiliBiliApi biliBiliApi;

    private final int id;

    private final int roomId;

    private final WelcomeStatistic welcomeStatistic = new WelcomeStatistic();

    private final GiftStatistic giftStatistic = new GiftStatistic();

    private Socket socket = null;

    private Boolean isShutdown = false;

    /**
     * 数据输出流，用于发送数据包
     */
    private DataOutputStream dataOutputStream;

    DanmuReceiveThread(BiliBiliServiceImpl bilibiliService,
                       MessageHandleService messageHandleService,
                       RoomInfo roomInfo, HeartBeatTask heartBeatTask,
                       BiliBiliApi biliBiliApi) {
        this.biliBiliService = bilibiliService;
        this.messageHandleService = messageHandleService;
        this.id = roomInfo.getId();
        this.roomId = roomInfo.getRoomid();
        this.heartBeatTask = heartBeatTask;
        this.biliBiliApi = biliBiliApi;
        int time = (int) (System.currentTimeMillis() / 1000);
        giftStatistic.setRoomId(roomId);
        giftStatistic.setStartTime(time);
        welcomeStatistic.setRoomId(roomId);
        welcomeStatistic.setStartTime(time);
    }

    @Override
    public void run() {
        createSocket();
    }

    private void createSocket() {
        try {
            DanmuConf danmuConf = biliBiliApi.getDanmuConf(roomId);
            if (danmuConf == null) {
                biliBiliService.stop(id);
                return;
            }
            // 连接
            socket = new Socket(danmuConf.getHost(), danmuConf.getPort());
            // 获取数据输出流
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            // 获取输出流
            InputStream inputStream = socket.getInputStream();
            // 发送加入信息
            sendJoinMsg(roomId, danmuConf.getToken());
            // 提示，已经连接
            log.info("ip:{},端口:{}", socket.getLocalAddress().toString(), socket.getLocalPort());
            log.info("房间{}已经连接", roomId);
            // 心跳包
            heartBeatTask.submit(this);
            // 接收信息处理
            inputStreamHandel(inputStream);
        } catch (Exception e) {
            if (!isShutdown) {
                log.error("Exception:", e);
                log.info("房间{}已退出", roomId);
            }
        } finally {
            if (!giftStatistic.getGiftMap().isEmpty()) {
                int time = (int) (System.currentTimeMillis() / 1000);
                giftStatistic.setEndTime(time);
                welcomeStatistic.setEndTime(time);
                try {
                    messageHandleService.giftStatisticHandle(giftStatistic);
                    messageHandleService.welcomeStatisticHandle(welcomeStatistic);
                } catch (InterruptedException e) {
                    log.info("数据统计处理异常:", e);
                }
            }
            if (!isShutdown) {
                biliBiliService.stop(id);
            }
        }
    }

    /**
     * 如果长度大于 16，说明是有数据的
     * 3：人气值
     * 5：弹幕
     * // 头部长度 int headLength =
     * // byteBuffer.getShort();
     * // 协议 protocol
     * // byteBuffer.getShort();
     * // 操作码 action
     * // byteBuffer.getInt();
     * // 协议 sequence
     * // byteBuffer.getInt();
     */
    private void inputStreamHandel(InputStream inputStream) throws Exception {
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
            // 如果长度小于等于 16，谨防万一，加一个上限
            if (length <= headerSize || length > 65534) {
                continue;
            }
            // 剔除头部，进行读取
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
                    danmuMsg(msg);
                } catch (InterruptedException ie) {
                    log.info("弹幕处理异常:", ie);
                }
                break;
            case "SEND_GIFT":
                try {
                    gifMsg(msg);
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
                    guardMsg(msg);
                } catch (Exception e) {
                    log.warn("舰长开通处理异常:", e);
                }
                break;
            case "ROOM_SILENT_OFF":
                // log.info("直播结束roomId:{}", roomId);
                // bilibiliService.stop(id);
                break;
            default:
                // log.info(bodyString);
                break;
        }
    }

    private void danmuMsg(String msg) throws InterruptedException {
        Matcher mDanmu = DanmuPatternUtils.readDanmuInfo.matcher(msg);
        Matcher mUid = DanmuPatternUtils.readDanmuUid.matcher(msg);
        Matcher mNickname = DanmuPatternUtils.readDanmuUser.matcher(msg);
        Matcher mSendTime = DanmuPatternUtils.readDanmuSendTime.matcher(msg);

        if (!(mDanmu.find() && mUid.find()
                && mNickname.find() && mSendTime.find())) {
            log.warn("弹幕信息解析失败:{}", msg);
            return;
        }

        String danmuText = mDanmu.group(1);
        int uid = Integer.parseInt(mUid.group(1));
        String nickname = mNickname.group(1);
        long sendTime = Long.parseLong(mSendTime.group(1));
        DanmuDO danmu = new DanmuDO();
        danmu.setRoomid(roomId);
        danmu.setUid(uid);
        danmu.setDanmu(danmuText);
        danmu.setNickname(nickname.length() > 20 ? nickname.substring(0, 20) : nickname);
        danmu.setSendTime(sendTime / 1000);
        messageHandleService.danmuHandle(danmu);
    }

    private void gifMsg(String msg) throws InterruptedException {
        // 正则匹配
        Matcher mGiftName = DanmuPatternUtils.readGiftName.matcher(msg);
        Matcher mNum = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mUser = DanmuPatternUtils.readGiftUser.matcher(msg);
        Matcher mUid = DanmuPatternUtils.readUserId.matcher(msg);
        Matcher mGiftId = DanmuPatternUtils.readGiftId.matcher(msg);
        Matcher mPrice = DanmuPatternUtils.readGiftPrice.matcher(msg);
        Matcher mTimestamp = DanmuPatternUtils.readGiftSendTime.matcher(msg);

        if (!(mGiftName.find() && mNum.find()
                && mGiftId.find() && mUid.find()
                && mUser.find() && mPrice.find()
                && mTimestamp.find())) {
            log.warn("礼物信息解析失败:{}", msg);
            return;
        }

        int giftId = Integer.parseInt(mGiftId.group(1));
        String giftName = DanmuPatternUtils.unicodeToString(mGiftName.group(1));
        int num = Integer.parseInt(mNum.group(1));
        String user = DanmuPatternUtils.unicodeToString(mUser.group(1));
        int uid = Integer.parseInt(mUid.group(1));
        int price = Integer.parseInt(mPrice.group(1));
        int sendTime = Integer.parseInt(mTimestamp.group(1));

        GiftDO giftDO = new GiftDO();
        giftDO.setRoomId(roomId);
        giftDO.setUid(uid);
        giftDO.setUsername(user.length() > 20 ? user.substring(0, 20) : user);
        giftDO.setGiftNum(num);
        giftDO.setGiftId(giftId);
        giftDO.setPrice(price);
        giftDO.setGiftName(giftName);
        giftDO.setSendTime(sendTime);
        messageHandleService.giftHandle(giftDO);

        GiftCount giftCount = giftStatistic.getGiftMap().get(giftId);
        if (giftCount != null) {
            giftCount.setCount(giftCount.getCount() + num);
        } else {
            giftCount = new GiftCount();
            giftCount.setGiftId(giftId);
            giftCount.setGiftName(giftName);
            giftCount.setCount(num);
            giftCount.setPrice(price);
            giftStatistic.getGiftMap().put(giftId, giftCount);
        }
    }

    private void guardMsg(String msg) throws InterruptedException {
        Matcher mUid = DanmuPatternUtils.readUserId.matcher(msg);
        Matcher mUsername = DanmuPatternUtils.readUsername.matcher(msg);
        Matcher mGuardLevel = DanmuPatternUtils.readGuardLevel.matcher(msg);
        Matcher mGiftName = DanmuPatternUtils.readGiftName.matcher(msg);
        Matcher mNum = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mPrice = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mSendTime = DanmuPatternUtils.readStartTime.matcher(msg);

        if (!(mUid.find() && mUsername.find()
                && mGuardLevel.find() && mGiftName.find()
                && mNum.find() && mPrice.find()
                && mSendTime.find())) {
            log.warn("舰长购买信息解析失败:{}", msg);
            return;
        }

        int uid = Integer.parseInt(mUid.group(1));
        String username = mUsername.group(1);
        int guardLevel = Integer.parseInt(mGuardLevel.group(1));
        String giftName = mGiftName.group(1);
        int num = Integer.parseInt(mNum.group(1));
        int price = Integer.parseInt(mPrice.group(1));
        int sendTime = Integer.parseInt(mSendTime.group(1));

        GuardDO guardDO = new GuardDO();
        guardDO.setUid(uid);
        guardDO.setUsername(username);
        guardDO.setGuardLevel(guardLevel);
        guardDO.setGiftName(giftName);
        guardDO.setNum(num);
        guardDO.setPrice(price);
        guardDO.setSendTime(sendTime);
        guardDO.setRoomId(roomId);
        messageHandleService.guardHandle(guardDO);
    }

    private void welcomeMsg(String msg) {
        Matcher mUid = DanmuPatternUtils.readUserId.matcher(msg);
        if (mUid.find()) {
            welcomeStatistic.getWelcomeSet().add(Integer.parseInt(mUid.group(1)));
        }
    }

    /**
     * 首次连接发送的认证数据
     *
     * @param roomId 真实的直播房间 ID
     */
    private void sendJoinMsg(Integer roomId, String token) {
        // 生成随机的 UID
        long clientId = RandomUtils.nextLong(100000000000000L, 300000000000000L);
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
