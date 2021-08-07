package club.dbg.cms.upload.controller;

import club.dbg.cms.util.ResponseBuild;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("upload")
public class UploadController {
    @RequestMapping(value = "image", method = RequestMethod.POST)
    public ResponseBuild<String> image(MultipartFile file) {
        return ResponseBuild.ok(file.getContentType());
    }
}
