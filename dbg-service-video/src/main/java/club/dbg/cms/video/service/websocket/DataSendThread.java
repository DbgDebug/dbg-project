package club.dbg.cms.video.service.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class DataSendThread implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(DataSendThread.class);
    private static final LinkedBlockingQueue<IWebSocketSendTask> dataTaskQueue = new LinkedBlockingQueue<>();

    private DataSendThread() {
    }

    public static DataSendThread getInstance() {
        return Inner.instance;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                IWebSocketSendTask task = dataTaskQueue.take();
                task.task();
            } catch (InterruptedException | IOException e) {
                log.warn("数据发送失败");
            }
        }
    }

    public void submit(IWebSocketSendTask task)  {
        try {
            dataTaskQueue.put(task);
        } catch (InterruptedException e) {
            log.info("添加任务失败");
        }
    }

    private static class Inner {
        private static final DataSendThread instance = new DataSendThread();
    }
}
