package club.dbg.cms.upload.service.schedule;

public interface IScheduleTask extends Runnable{
    boolean isCancel();
    void setCancel(boolean cancel);
}
