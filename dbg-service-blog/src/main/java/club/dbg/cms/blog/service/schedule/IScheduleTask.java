package club.dbg.cms.blog.service.schedule;

public interface IScheduleTask extends Runnable{
    boolean isCancel();
    void setCancel(boolean cancel);
}
