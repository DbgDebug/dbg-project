package club.dbg.cms.admin.service.file;

import club.dbg.cms.admin.service.file.pojo.FileDir;

public interface FileService {
    FileDir getFileDir(String path, boolean onlyFile);

    /**
     * 只允许删除文件，不能删除目录
     *
     * @param path
     * @return
     */
    boolean deleteFile(String path);
}
