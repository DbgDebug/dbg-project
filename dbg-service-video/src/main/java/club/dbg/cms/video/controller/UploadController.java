package club.dbg.cms.video.controller;

import club.dbg.cms.util.FileUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/upload")
public class UploadController {
    private final AtomicInteger atomicInteger = new AtomicInteger(1);

    @RequestMapping(value = "mp4", method = RequestMethod.POST)
    public void upload(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        System.out.println(bytes.length);
        FileUtils.writeFileByBytes("E:\\video\\" , atomicInteger.getAndAdd(1) + ".mp4", bytes, false);
    }
}
