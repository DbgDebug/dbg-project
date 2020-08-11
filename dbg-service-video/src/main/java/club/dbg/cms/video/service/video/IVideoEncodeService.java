package club.dbg.cms.video.service.video;

public interface IVideoEncodeService {
    boolean submit(Integer deviceId, IVideoEncodeTask videoEncodeTask);
    boolean close(Integer deviceId);
}
