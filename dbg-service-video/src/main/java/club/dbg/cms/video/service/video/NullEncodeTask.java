package club.dbg.cms.video.service.video;

import club.dbg.cms.video.service.video.pojo.ImageByte;

public class NullEncodeTask implements IVideoEncodeTask{
    @Override
    public ImageByte getImgByte() {
        return null;
    }

    @Override
    public IH264DataCallbackTask getCallbackTask() {
        return null;
    }
}
