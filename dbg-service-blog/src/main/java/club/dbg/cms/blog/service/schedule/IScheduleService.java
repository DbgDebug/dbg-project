package club.dbg.cms.blog.service.schedule;

public interface IScheduleService {
    void submit(IScheduleTask r, long delay);
}
