package club.dbg.cms.video.service.video;

public interface IVideoEncodeService {
    boolean submit(Integer deviceId, IVideoEncodeTask videoEncodeTask) throws InterruptedException;
    boolean close(Integer deviceId);
}
