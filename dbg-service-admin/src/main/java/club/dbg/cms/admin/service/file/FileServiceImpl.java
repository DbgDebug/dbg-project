package club.dbg.cms.admin.service.file;

import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.file.pojo.FileDir;
import club.dbg.cms.admin.service.file.pojo.FileProperty;
import club.dbg.cms.admin.service.file.pojo.UploadResultDTO;
import club.dbg.cms.rpc.pojo.Operator;
import club.dbg.cms.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */

@Service
public class FileServiceImpl implements FileService {
    private final String rootDir;

    private final String imagesDir;

    public FileServiceImpl(
            @Value("${file.rootDir}")
            String rootDir,
            @Value("${file.imagesDir}")
            String imagesDir) {
        this.rootDir = rootDir;
        this.imagesDir = imagesDir;
    }

    @Override
    public FileDir getFileDir(String path, boolean onlyFile) {
        FileDir fileDir = new FileDir();
        fileDir.setDir(path);
        fileDir.setDirList(getFiles(path));
        return fileDir;
    }

    @Override
    public boolean deleteFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            throw new BusinessException("不是文件，不能删除。");
        }
        if (!file.delete()) {
            throw new BusinessException("文件删除失败");
        }
        return true;
    }

    @Override
    public UploadResultDTO uploadImg(Operator operator, MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        String type;
        if (bytes[1] == (byte) 'P'
                && bytes[2] == (byte) 'N'
                && bytes[3] == (byte) 'G') {
            type = ".png";
        } else if (bytes[6] == (byte) 'J'
                && bytes[7] == (byte) 'F'
                && bytes[8] == (byte) 'I'
                && bytes[9] == (byte) 'F') {
            type = ".jpg";
        } else if (bytes[6] == (byte) 'E'
                && bytes[7] == (byte) 'x'
                && bytes[8] == (byte) 'i'
                && bytes[9] == (byte) 'f') {
            type = ".jpg";
        } else if (bytes[6] == (byte) 'e'
                && bytes[7] == (byte) 'x'
                && bytes[8] == (byte) 'i'
                && bytes[9] == (byte) 'f') {
            type = ".jpg";
        } else if (bytes[0] == (byte) 0xFF
                && bytes[1] == (byte) 0xD8
                && bytes[bytes.length - 2] == (byte) 0xFF
                && bytes[bytes.length - 1] == (byte) 0xD9) {
            type = ".jpg";
        } else if (bytes[0] == (byte) 'G'
                && bytes[1] == (byte) 'I'
                && bytes[2] == (byte) 'F') {
            type = ".gif";
        } else {
            throw BusinessException.build("只支持 PNG/JPEG/GIF 格式的图片");
        }
        if (bytes.length / 1024 > 5120) {
            throw BusinessException.build("只支持5MB以内的 PNG/JPEG/GIF 格式的图片");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        FileUtils.writeFileByBytes("", "", bytes);
        return null;
    }

    private List<FileProperty> getFiles(String path) {
        List<FileProperty> files = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            return files;
        }

        File[] fileList = file.listFiles();
        if (fileList == null) {
            return files;
        }
        for (File fileTemp : fileList) {
            FileProperty fileProperty = new FileProperty();
            if (fileTemp.isFile()) {
                fileProperty.setType(1);
            } else if (fileTemp.isDirectory()) {
                fileProperty.setType(2);
            }
            fileProperty.setFileName(fileTemp.getName());
            fileProperty.setPath(fileTemp.getPath());
            fileProperty.setUpdateTime(fileTemp.lastModified());
            files.add(fileProperty);
        }

        return files;
    }
}
