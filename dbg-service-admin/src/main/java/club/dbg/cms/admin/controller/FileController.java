package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.service.file.FileService;
import club.dbg.cms.rpc.pojo.ResponseResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ResponseResultDTO> getFileDir(
            @DecimalMax(value = "255", message = "路径长度不能大于255")
            @RequestParam(value = "path", defaultValue = "./") String path,
            @RequestParam(value = "only_file", defaultValue = "false") Boolean onlyFile) {
        ResponseResultDTO response = new ResponseResultDTO();
        response.setCode(20000);
        response.setData(fileService.getFileDir(path, onlyFile));
        return ResponseEntity.ok(response);
    }
}
