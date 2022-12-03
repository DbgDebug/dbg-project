package club.dbg.cms.admin.service.file;

import club.dbg.cms.admin.service.file.pojo.FileDir;
import club.dbg.cms.admin.service.file.pojo.UploadResultDTO;
import club.dbg.cms.rpc.pojo.Operator;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    FileDir getFileDir(String path, boolean onlyFile);

    /**
     * 只允许删除文件，不能删除目录
     *
     * @param path
     * @return
     */
    boolean deleteFile(String path);

    UploadResultDTO uploadImg(Operator operator, MultipartFile file) throws IOException;
}
