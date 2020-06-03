package club.dbg.cms.admin.service.file;

import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.file.pojo.FileDir;
import club.dbg.cms.admin.service.file.pojo.FileProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dbg
 */

@Service
public class FileServiceImpl implements FileService {
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
