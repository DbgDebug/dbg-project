package club.dbg.cms.upload.service.schedule;

public interface IScheduleService {
    void submit(IScheduleTask r, long delay);
}
