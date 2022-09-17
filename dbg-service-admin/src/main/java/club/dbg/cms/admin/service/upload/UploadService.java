package club.dbg.cms.admin.service.upload;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    String uploadGameRom(MultipartFile file);

    String uploadGameBackup(MultipartFile file);
}
