package club.dbg.cms.blog.service.schedule;

public class ScheduleTask implements IScheduleTask {
    private boolean isCancel = false;

    @Override
    public void run() {
        if (isCancel) {
            return;
        }
        System.out.println("run");
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }
}
