package club.dbg.cms.video.service.video;

import club.dbg.cms.video.service.video.pojo.ImageByte;

public interface IVideoEncodeTask {
    ImageByte getImgByte();

    IH264DataCallbackTask getCallbackTask();
}
