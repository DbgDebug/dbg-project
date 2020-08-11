package club.dbg.cms.video.service.device;

import club.dbg.cms.video.service.video.IH264DataCallbackTask;
import club.dbg.cms.video.service.video.IVideoEncodeTask;
import club.dbg.cms.video.service.video.pojo.ImageByte;

public class ImagesToH264Task implements IVideoEncodeTask {

    private final ImageByte imageByte;

    private final IH264DataCallbackTask h264CallbackTask;

    public ImagesToH264Task(ImageByte imageByte, IH264DataCallbackTask h264CallbackTask) {
        this.imageByte = imageByte;
        this.h264CallbackTask = h264CallbackTask;
    }

    @Override
    public ImageByte getImgByte() {
        return imageByte;
    }

    @Override
    public IH264DataCallbackTask getCallbackTask() {
        return h264CallbackTask;
    }
}
