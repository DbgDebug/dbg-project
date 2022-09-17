package club.dbg.cms.upload.controller;

import club.dbg.cms.upload.service.upload.UploadService;
import club.dbg.cms.util.ResponseBuild;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("upload")
public class UploadController {
    private final UploadService imagesUploadService;

    public UploadController(UploadService imagesUploadService) {
        this.imagesUploadService = imagesUploadService;
    }

    @RequestMapping(value = "image", method = RequestMethod.POST)
    public ResponseBuild<String> image(
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseBuild.ok(imagesUploadService.uploadImage(file));
    }

    @RequestMapping(value = "file", method = RequestMethod.POST)
    public ResponseBuild<String> file(
            @RequestParam("type") String type,
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseBuild.ok(imagesUploadService.uploadFile(type, file));
    }
}
