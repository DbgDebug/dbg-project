package club.dbg.cms.admin.service.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class DataWriteServiceImpl implements DataBaseService {
    private static final Logger log = LoggerFactory.getLogger(DataWriteServiceImpl.class);

    private final DataWriteThread dataWriteThread;

    public DataWriteServiceImpl() {
        this.dataWriteThread = new DataWriteThread();
        new Thread(dataWriteThread).start();
    }

    @Override
    public void submitTask(DataWriteTask dataWriteTask) throws InterruptedException {
        dataWriteThread.submit(dataWriteTask);
    }

    private static class DataWriteThread implements Runnable {
        private final LinkedBlockingQueue<DataWriteTask> dataWriteTasks = new LinkedBlockingQueue<>();

        public void submit(DataWriteTask dataWriteTask) throws InterruptedException {
            dataWriteTasks.put(dataWriteTask);
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    DataWriteTask dataWriteTask = dataWriteTasks.take();
                    dataWriteTask.write();
                } catch (Exception e) {
                    log.warn("数据写入任务异常:", e);
                }
            }
            log.info("数据写入线程已退出");
        }
    }
}
