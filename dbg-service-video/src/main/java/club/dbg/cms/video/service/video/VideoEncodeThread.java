package club.dbg.cms.video.service.video;

import club.dbg.cms.video.service.video.pojo.H264Byte;
import club.dbg.cms.video.service.video.pojo.ImageByte;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class VideoEncodeThread implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(VideoEncodeThread.class);

    private final IVideoEncodeService videoEncodeService;
    private final LinkedBlockingQueue<IVideoEncodeTask> videoEncodeTasks;
    private final Integer deviceId;
    private final MessageHandle messageHandle;
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private boolean isShutdown = false;

    public VideoEncodeThread(Integer deviceId, IVideoEncodeService videoEncodeService, LinkedBlockingQueue<IVideoEncodeTask> videoEncodeTasks) {
        this.videoEncodeService = videoEncodeService;
        this.videoEncodeTasks = videoEncodeTasks;
        this.deviceId = deviceId;
        messageHandle = new MessageHandle(videoEncodeTasks);
    }

    @Override
    public void run() {
        socket();
        if(!isShutdown){
            videoEncodeService.close(deviceId);
        }
    }

    public void close() {
        isShutdown = true;
        try {
            if(socket != null){
                socket.close();
            }
        } catch (IOException e) {
            log.info("编码线程已关闭");
        }
        videoEncodeTasks.offer(new NullEncodeTask());
    }

    private void socket() {
        try {
            // 连接
            socket = new Socket("127.0.0.1", 6666);
            // 获取数据输出流
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            // 获取输入流
            InputStream inputStream = socket.getInputStream();
            // 发送初始化信息
            ByteBuffer byteBuffer = ByteBuffer.allocate(8);
            byteBuffer.putShort((short) 640);
            byteBuffer.putShort((short) 360);
            byteBuffer.putShort((short) 10);
            byteBuffer.putShort((short) 500);
            dataOutputStream.write(byteBuffer.array());
            // 接收信息处理
            inputStreamHandle(inputStream);
        } catch (Exception e) {
            log.info("已退出", e);
        }
    }

    /**
     * 读取数据
     * 头部：
     * [0, 3] - 长度数据 int
     * [4, 7] - 用户标识 int
     *
     * @param inputStream 输入流
     * @throws IOException io异常
     */
    private void inputStreamHandle(InputStream inputStream) throws IOException, InterruptedException {
        int headerSize = 8;
        byte[] headerBytes = new byte[headerSize];
        while (!Thread.currentThread().isInterrupted()) {
            IVideoEncodeTask videoEncodeTask = videoEncodeTasks.take();
            if(videoEncodeTask.getImgByte() == null){
                return;
            }
            send(videoEncodeTask.getImgByte());

            int readCount = 0;
            while (readCount < headerSize) {
                readCount += inputStream.read(headerBytes, readCount, headerSize - readCount);
            }
            ByteBuffer headerBuf = ByteBuffer.wrap(headerBytes);
            int length = headerBuf.getInt() - headerSize;
            int deviceId = headerBuf.get();

            byte[] data = new byte[length];
            readCount = 0;
            while (readCount < length) {
                readCount += inputStream.read(data, readCount, length - readCount);
            }
            IH264DataCallbackTask callbackTask = videoEncodeTask.getCallbackTask();
            callbackTask.callback(new H264Byte(deviceId, data));
        }
    }

    /**
     * 数据长度为 imageByte.getImgBytes().array().length + 4
     *
     * @throws IOException IO异常
     */
    private void send(ImageByte imageByte) throws IOException {
        int size = imageByte.getImgBytes().array().length + 4;
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(size);
        dataOutputStream.write(buffer.array());
        dataOutputStream.write(imageByte.getImgBytes().array());

        //ByteBuffer buffer = ByteBuffer.allocate(size);
        //buffer.putInt(size);
        //buffer.put(imageByte.getImgBytes().array());
        //dataOutputStream.write(buffer.array());
    }

    private void netty() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress("127.0.0.1", 6666);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new IdleStateHandler(0, 30000, 0, TimeUnit.MILLISECONDS))
                            .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, -4, 0))
                            .addLast(messageHandle);
                }
            });
            ChannelFuture channelFuture = bootstrap.connect().sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.warn("连接异常:", e);
        } finally {
            eventLoopGroup.shutdownGracefully().sync();
            log.info("连接结束");
        }
    }
}