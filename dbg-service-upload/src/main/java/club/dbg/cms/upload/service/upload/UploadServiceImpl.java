package club.dbg.cms.upload.service.upload;

import club.dbg.cms.upload.exception.BusinessException;
import club.dbg.cms.util.FileUtils;
import club.dbg.cms.util.ImageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UploadServiceImpl implements UploadService {
    private final String rootDir;

    private final String imagesDir;

    private final String gameRomDir;

    private final String tempDir;

    public UploadServiceImpl(
            @Value("${fileUpload.rootDir}")
                    String rootDir,
            @Value("${fileUpload.imagesDir}")
                    String imagesDir,
            @Value("${fileUpload.gameRomDir}")
                    String gameRomDir,
            @Value("${fileUpload.tempDir}")
            String tempDir) {
        this.rootDir = rootDir;
        this.imagesDir = imagesDir;
        this.gameRomDir = gameRomDir;
        this.tempDir = tempDir;
    }

    @Override
    public String uploadFile(String fileType, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        FileUtils.writeFileByBytes(rootDir + tempDir, file.getOriginalFilename(), bytes);
        return null;
    }

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String type;
        if (ImageUtils.isJPEG(bytes)) {
            type = ".jpg";
        } else if (ImageUtils.isPNG(bytes)) {
            type = ".png";
        } else {
            System.out.println(System.nanoTime());
            throw BusinessException.build("是个锤子 JPEG/PNG 图片");
        }
        FileUtils.writeFileByBytes(rootDir + imagesDir, file.getOriginalFilename(), bytes);
        return imagesDir + file.getOriginalFilename();
    }

    public String moveFile() {
        return null;
    }
}
