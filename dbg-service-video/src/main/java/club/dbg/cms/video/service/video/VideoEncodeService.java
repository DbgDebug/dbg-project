package club.dbg.cms.video.service.video;

import club.dbg.cms.video.service.device.H264CallbackTask;
import club.dbg.cms.video.service.device.ImagesToH264Task;
import club.dbg.cms.video.service.video.pojo.ImageByte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.nio.ByteBuffer;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Lazy(value = false)
public class VideoEncodeService implements IVideoEncodeService {
    private final static Logger log = LoggerFactory.getLogger(VideoEncodeService.class);

    private final static ConcurrentHashMap<Integer, LinkedBlockingQueue<IVideoEncodeTask>> deviceMap = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Integer, VideoEncodeThread> videoEncodeMap = new ConcurrentHashMap<>();
    private final static ThreadPoolExecutor encodePoolExecutor = new ThreadPoolExecutor(
            1,
            2,
            0,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.AbortPolicy());

    private final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public boolean submit(Integer deviceId, ByteBuffer byteBuffer, ConcurrentLinkedQueue<Session> sessions) throws InterruptedException {
        ImageByte imageByte = new ImageByte(deviceId, byteBuffer);
        H264CallbackTask h264CallbackTask = new H264CallbackTask(sessions);
        ImagesToH264Task imagesToH264Task = new ImagesToH264Task(imageByte, h264CallbackTask);
        if (!deviceMap.containsKey(deviceId)) {
            reentrantLock.lockInterruptibly();
            try {
                if (!deviceMap.containsKey(deviceId)) {
                    if (encodePoolExecutor.getActiveCount() >= 2) {
                        return false;
                    }
                    LinkedBlockingQueue<IVideoEncodeTask> videoEncodeTasks = new LinkedBlockingQueue<>();
                    deviceMap.put(deviceId, videoEncodeTasks);
                    VideoEncodeThread videoEncodeThread = new VideoEncodeThread(deviceId, this, videoEncodeTasks);
                    videoEncodeMap.put(deviceId, videoEncodeThread);
                    encodePoolExecutor.execute(videoEncodeThread);
                }
            } finally {
                reentrantLock.unlock();
            }
        }
        LinkedBlockingQueue<IVideoEncodeTask> queue = deviceMap.get(deviceId);
        if (queue == null) {
            return false;
        }
        return queue.offer(imagesToH264Task);
    }

    @Override
    public boolean close(Integer deviceId) {
        deviceMap.remove(deviceId);
        VideoEncodeThread encodeThread = videoEncodeMap.remove(deviceId);
        if (encodeThread != null) {
            encodeThread.close();
        }
        return true;
    }
}