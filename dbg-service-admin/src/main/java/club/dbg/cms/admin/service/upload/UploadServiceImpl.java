package club.dbg.cms.admin.service.upload;

import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadServiceImpl implements UploadService {
    private final static Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);

    private final String rootDir = "E:/upload";

    private final String romDir = "/rom";

    private final String backupDir = "/backup";

    @Override
    public String uploadGameRom(MultipartFile file) {
        System.out.println(file.getOriginalFilename());
        String filePath;
        try {
            byte[] romBytes = file.getBytes();
            if (romBytes.length < 1) {
                throw BusinessException.build("文件上传失败，请重试");
            }
            System.out.println(rootDir + romDir);
            FileUtils.writeFileByBytes(rootDir + romDir, "/"+file.getOriginalFilename(), romBytes);
            filePath = romDir + "/" + file.getOriginalFilename();
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw BusinessException.build("文件上传失败，请重试");
        }

        return filePath;
    }

    @Override
    public String uploadGameBackup(MultipartFile file) {
        String filePath;
        try {
            byte[] romBytes = file.getBytes();
            if (romBytes.length < 1) {
                throw BusinessException.build("文件上传失败，请重试");
            }
            FileUtils.writeFileByBytes(rootDir + backupDir, file.getOriginalFilename(), romBytes);
            filePath = backupDir + "/" + file.getOriginalFilename();
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw BusinessException.build("文件上传失败，请重试");
        }
        return filePath;
    }
}
