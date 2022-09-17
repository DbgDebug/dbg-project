package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.service.upload.UploadService;
import club.dbg.cms.util.ResponseBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public ResponseBuild<String> images(@RequestParam("file") MultipartFile file) {
        return ResponseBuild.ok("");
    }

    @RequestMapping(value = "/game/rom", method = RequestMethod.POST)
    public ResponseBuild<String> uploadGameRom(@RequestParam("rom") MultipartFile file) {
        return ResponseBuild.ok(uploadService.uploadGameRom(file));
    }

    @RequestMapping(value = "/game/backup", method = RequestMethod.POST)
    public ResponseBuild<String> uploadGameBackup(@RequestParam("backup") MultipartFile file) {
        return ResponseBuild.ok(uploadService.uploadGameBackup(file));
    }
}
