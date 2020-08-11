package club.dbg.cms.video.service.video;

import club.dbg.cms.video.service.video.pojo.ImageByte;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageHandle extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(MessageHandle.class);
    private final LinkedBlockingQueue<IVideoEncodeTask> videoEncodeTasks;
    private ChannelHandlerContext ctx = null;

    public MessageHandle(LinkedBlockingQueue<IVideoEncodeTask> videoEncodeTasks) {
        this.videoEncodeTasks = videoEncodeTasks;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("socket已连接");
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 数据读取
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 释放资源
        log.warn("Unexpected exception from downstream : {}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果写通道处于空闲状态就发送心跳命令
            if (IdleState.WRITER_IDLE.equals(event.state())) {
                log.info(Thread.currentThread().getName());
                //ctx.channel().writeAndFlush(nettyTest.sendDataPack(Unpooled.buffer(), 2, ""));
            }
        }
    }

    private static class WriteThread implements Runnable {
        private final LinkedBlockingQueue<ImageByte> imageBytes;
        private final ChannelHandlerContext ctx;

        private WriteThread(LinkedBlockingQueue<ImageByte> imageBytes, ChannelHandlerContext ctx) {
            this.imageBytes = imageBytes;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                ImageByte imageByte = null;
                try {
                    imageByte = imageBytes.take();
                } catch (InterruptedException e) {
                    log.warn("video encode:");
                    continue;
                }
                ByteBuf byteBuf = Unpooled.buffer();
                byteBuf.writeInt(imageByte.getImgBytes().array().length + 4);
                byteBuf.writeBytes(imageByte.getImgBytes().array());
                ctx.writeAndFlush(byteBuf);
            }
        }
    }

    // 数据触发 writeAndFlush(ByteBuf).addListener(trafficGenerator);
    private final ChannelFutureListener trafficGenerator = new ChannelFutureListener() {
        //完成操作后的方法调用，即只要成功无限调用generateTraffic()
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if (channelFuture.isSuccess()) {

            } else {
                channelFuture.cause().printStackTrace();
                channelFuture.channel().close();
            }
        }
    };

    private void writeAndFlush() throws InterruptedException {
        IVideoEncodeTask videoEncodeTask = videoEncodeTasks.take();
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(videoEncodeTask.getImgByte().getImgBytes().array().length + 4);
        byteBuf.writeBytes(videoEncodeTask.getImgByte().getImgBytes().array());
        ctx.writeAndFlush(byteBuf).addListener(trafficGenerator);
    }
}
