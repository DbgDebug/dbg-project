package club.dbg.cms.video.service.video;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class VideoEncodeService implements IVideoEncodeService {
    private final static Logger log = LoggerFactory.getLogger(VideoEncodeService.class);
    private final static ConcurrentHashMap<Integer, LinkedBlockingQueue<IVideoEncodeTask>> h264Map = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<Integer, VideoEncodeThread> encodeThreadMap = new ConcurrentHashMap<>();
    private final static ThreadPoolExecutor encodePoolExecutor = new ThreadPoolExecutor(
            1,
            2,
            0,
            TimeUnit.MILLISECONDS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.AbortPolicy());

    @Override
    public boolean submit(Integer deviceId, IVideoEncodeTask videoEncodeTask) {
        if (!h264Map.containsKey(deviceId)) {
            synchronized (this) {
                if (encodePoolExecutor.getActiveCount() >= 2) {
                    return false;
                }
                LinkedBlockingQueue<IVideoEncodeTask> videoEncodeTasks = new LinkedBlockingQueue<>();
                h264Map.put(deviceId, videoEncodeTasks);
                VideoEncodeThread encodeThread = new VideoEncodeThread(deviceId, this, videoEncodeTasks);
                encodeThreadMap.put(deviceId, encodeThread);
                encodePoolExecutor.execute(encodeThread);
            }
        }
        LinkedBlockingQueue<IVideoEncodeTask> queue = h264Map.get(deviceId);
        return queue.offer(videoEncodeTask);
    }

    @Override
    public boolean close(Integer deviceId) {
        h264Map.remove(deviceId);
        VideoEncodeThread encodeThread = encodeThreadMap.remove(deviceId);
        if (encodeThread != null) {
            encodeThread.close();
        }
        return true;
    }
}