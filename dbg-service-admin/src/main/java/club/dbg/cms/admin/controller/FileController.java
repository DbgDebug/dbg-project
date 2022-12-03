package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.service.file.FileService;
import club.dbg.cms.admin.service.file.pojo.FileDir;
import club.dbg.cms.admin.service.file.pojo.UploadResultDTO;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import club.dbg.cms.util.ResponseBuild;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.DecimalMax;

/**
 * @author dbg
 */

@RestController
public class FileController {
    final
    FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @RequestMapping(value = "get_file_dir", name = "获取目录文件", method = RequestMethod.GET)
    public ResponseBuild<FileDir> getFileDir(
            @DecimalMax(value = "255", message = "路径长度不能大于255")
            @RequestParam(value = "path", defaultValue = "./") String path,
            @RequestParam(value = "only_file", defaultValue = "false") Boolean onlyFile) {
        return ResponseBuild.ok(fileService.getFileDir(path, onlyFile));
    }

    public ResponseBuild<UploadResultDTO> upload(
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseBuild.ok(null);
    }
}
