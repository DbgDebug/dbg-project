package club.dbg.cms.admin.service.netty;

import club.dbg.cms.admin.service.bilibili.BiliBiliApi;
import club.dbg.cms.admin.service.bilibili.pojo.DanmuConf;
import club.dbg.cms.util.ZLibUtils;
import club.dbg.cms.util.bilibili.DanmuPatternUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;

public class NettyTest implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(NettyTest.class);

    private final int roomId = 1512103;

    @Override
    public void run() {
    }

    @Test
    public void danmuTest() {
        try {
            init();
        } catch (Exception e) {
            log.warn("连接异常，已退出", e);
        }
    }

    private void init() throws InterruptedException {
        NettyTest nettyTest = this;
        DanmuConf danmuConf = new BiliBiliApi().getDanmuConf(roomId);
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress(danmuConf.getHost(), danmuConf.getPort());
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    log.info("roomId:{}", roomId);
                    ch.pipeline().addLast(new IdleStateHandler(0, 30000, 0, TimeUnit.MILLISECONDS))
                            .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0))
                            .addLast(new MessageHandle(nettyTest, roomId, danmuConf));
                }
            });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
            log.info("已连接");
        } catch (InterruptedException e) {
            log.warn("连接异常:", e);
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
            log.info("连接结束");
        }
    }

    protected ByteBuf sendJoinMsg(ByteBuf buf, Integer roomId, String token) {
        // 生成随机的 UID
        long clientId = System.currentTimeMillis();
        // 发送验证包
        return sendDataPack(buf, 7,
                String.format("{\"uid\": %d, " +
                        "\"roomid\": %d, " +
                        "\"protover\": 1, " +
                        "\"platform\": \"web\", " +
                        "\"clientver\": \"1.8.2\", " +
                        "\"type\": 2, " +
                        "\"key\": \"%s\"}", clientId, roomId, token));
    }

    protected ByteBuf sendDataPack(ByteBuf buf, int action, String body) {
        // 数据部分，以 UTF-8 编码解析成 Byte
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        // 封包总长度，因为头部固定为 16 字长，故加上 16
        int length = bodyBytes.length + 16;

        // 写入 4 字长的封包总大小数据，所以为 int
        buf.writeInt(length);

        // 写入 2 字长的头部长度数据，头部长度固定为 16
        buf.writeShort((short) 16);

        // 写入 2 字长的协议版本数据，默认为 1
        buf.writeShort((short) 1);

        // 写入 4 字长的操作码，操作码有 2,3,5,7,8
        buf.writeInt(action);

        // 写入 4 字长的 sequence，意味不明，取常数 1
        buf.writeInt(1);

        // 写入数据
        buf.writeBytes(bodyBytes);

        // 写入输出数据流中
        return buf;
    }

    public int getRoomId() {
        return roomId;
    }
}

/**
 * 消息处理
 */
class MessageHandle extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MessageHandle.class);

    private final NettyTest nettyTest;

    private final int roomId;

    private final DanmuConf danmuConf;

    public MessageHandle(NettyTest nettyTest, int roomId, DanmuConf danmuConf) {
        this.nettyTest = nettyTest;
        this.roomId = roomId;
        this.danmuConf = danmuConf;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("socket已连接");
        log.info(Thread.currentThread().getName());
        ctx.writeAndFlush(nettyTest.sendJoinMsg(Unpooled.buffer(), roomId, danmuConf.getToken()));
        //Thread.sleep(30000);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        int headerSize = 16;
        // 头部的长度数据
        byte[] headByte = new byte[headerSize];
        byteBuf.readBytes(headByte);
        ByteBuffer byteBuffer = ByteBuffer.wrap(headByte);
        int length = byteBuffer.getInt();
        // 头部长度 int headLength = byteBuffer.getShort();
        // 协议 0为文本数据, 1为纯int, 2为zlib压缩后的文本数据 int protocol = byteBuffer.getShort();
        // 操作码 int action = byteBuffer.getInt();
        // 协议 int sequence = byteBuffer.getInt();
        // 如果长度小于等于 16，谨防万一，加一个上限
        if (length <= headerSize || length > 65534) {
            return;
        }
        // 读取头部以外的数据
        byte[] dataByte = new byte[length - headerSize];
        byteBuf.readBytes(dataByte);
        byte[] bytes = new byte[length];
        System.arraycopy(headByte, 0, bytes, 0, headerSize);
        System.arraycopy(dataByte, 0, bytes, headerSize, dataByte.length);
        byteHandle(bytes, ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 释放资源
        log.warn("Unexpected exception from downstream : {}", cause.getMessage());
        ctx.close();
    }

    private void byteHandle(byte[] byteData, ChannelHandlerContext context) {
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
                        byteHandle(ZLibUtils.decompress(bytes), context);
                    } else {
                        actionFive(bytes);
                    }
                    break;
                case 8:
                    context.writeAndFlush(nettyTest.sendDataPack(Unpooled.buffer(), 2, ""));
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
                log.info("DANMU_MSG:{}", msg);
                break;
            case "SEND_GIFT":
                log.info("SEND_GIFT:{}", msg);
                /*
                try {
                    Matcher mGiftId = DanmuPatternUtils.readGiftId.matcher(msg);
                    Matcher mGiftName = DanmuPatternUtils.readGiftName.matcher(msg);
                    Matcher mNum = DanmuPatternUtils.readGiftNum.matcher(msg);
                    Matcher mPrice = DanmuPatternUtils.readGiftPrice.matcher(msg);
                    Matcher mAction = DanmuPatternUtils.readGiftAction.matcher(msg);
                    Matcher mSuperGiftNum = DanmuPatternUtils.readSuperGiftNum.matcher(msg);
                    if (!(mGiftId.find() && mGiftName.find()
                            && mNum.find() && mPrice.find() & mAction.find()
                            && mSuperGiftNum.find())) {
                        return;
                    }
                    int gid = Integer.parseInt(mGiftId.group(1));
                    String giftName = DanmuPatternUtils.unicodeToString(mGiftName.group(1));
                    int num = Integer.parseInt(mNum.group(1));
                    int price = Integer.parseInt(mPrice.group(1));
                    String action = DanmuPatternUtils.unicodeToString(mAction.group(1));
                    int sNum =  Integer.parseInt(mSuperGiftNum.group(1));
                    if (gid != 1) {
                        log.info("giftName:{}, num:{}, price:{}, action:{}, sNum:{}", giftName, num, price, action, sNum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                */

                break;
            case "WELCOME":
                // welcomeMsg(bodyString);
                break;
            case "GUARD_MSG":
                log.info("舰长消息:{}", msg);
                break;
            case "GUARD_BUY":
                log.info("购买舰长信息:{}", msg);
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

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果写通道处于空闲状态就发送心跳命令
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                log.info(Thread.currentThread().getName());
                ctx.channel().writeAndFlush(nettyTest.sendDataPack(Unpooled.buffer(), 2, ""));
            }
        }
    }
}