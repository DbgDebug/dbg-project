package club.dbg.cms.video.service.file;

import club.dbg.cms.video.service.asynctask.AsyncTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FileWriteService {
    private final static Logger log = LoggerFactory.getLogger(FileWriteService.class);

    private final AsyncTaskService asyncTaskService;

    public FileWriteService(AsyncTaskService asyncTaskService) {
        this.asyncTaskService = asyncTaskService;
    }

    /**
     * 异步写入文件
     * @param path /path/
     * @param fileName fileName.xxx
     * @param bytes byte[]
     * @param isAppend 是否为追加写入
     */
    public void writeAsync(String path, String fileName, byte[] bytes, boolean isAppend) {
        FileWriteAsyncTask fileWriteAsyncTask = new FileWriteAsyncTask(path, fileName, bytes, isAppend);
        asyncTaskService.execute(fileWriteAsyncTask);
    }
}
