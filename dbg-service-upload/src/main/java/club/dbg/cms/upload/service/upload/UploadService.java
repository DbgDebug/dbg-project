package club.dbg.cms.upload.service.upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String uploadFile(String fileType, MultipartFile file) throws IOException;

    String uploadImage(MultipartFile file) throws IOException;
}
