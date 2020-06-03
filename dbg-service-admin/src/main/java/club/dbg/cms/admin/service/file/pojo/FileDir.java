package club.dbg.cms.admin.service.file.pojo;

import java.util.List;

public class FileDir {
    String dir;
    List<FileProperty> dirList;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public List<FileProperty> getDirList() {
        return dirList;
    }

    public void setDirList(List<FileProperty> dirList) {
        this.dirList = dirList;
    }
}
