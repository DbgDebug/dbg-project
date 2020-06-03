package club.dbg.cms.admin.service.database;

public interface DataBaseService {
    void submitTask(DataWriteTask dataWriteTask) throws InterruptedException;
}
