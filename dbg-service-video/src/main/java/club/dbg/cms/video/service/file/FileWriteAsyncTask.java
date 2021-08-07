package club.dbg.cms.video.service.file;

import club.dbg.cms.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FileWriteAsyncTask implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(FileWriteAsyncTask.class);
    private final String filePath;
    private final String fileName;
    private final byte[] fileBytes;
    private final boolean isAppend;

    public FileWriteAsyncTask(
            String filePath,
            String fileName,
            byte[] fileBytes,
            boolean isAppend) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileBytes = fileBytes;
        this.isAppend = isAppend;
    }

    @Override
    public void run() {
        try {
            FileUtils.writeFileByBytes(filePath, fileName, fileBytes, isAppend);
        } catch (IOException e) {
            log.error("文件写入失败: ", e);
        }
    }
}
