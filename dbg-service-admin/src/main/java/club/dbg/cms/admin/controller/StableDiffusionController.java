package club.dbg.cms.admin.controller;

import club.dbg.cms.admin.filter.pojo.MyHttpServletRequest;
import club.dbg.cms.admin.service.paint.StableDiffusionService;
import club.dbg.cms.admin.service.paint.pojo.StableDiffusionParam;
import club.dbg.cms.domain.admin.StableDiffusionTaskDO;
import club.dbg.cms.util.ResponseBuild;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("stable-diffusion")
public class StableDiffusionController {
    private final StableDiffusionService stableDiffusionService;

    public StableDiffusionController(StableDiffusionService stableDiffusionService) {
        this.stableDiffusionService = stableDiffusionService;
    }

    @RequestMapping(value = "/create-task", method = RequestMethod.POST)
    public ResponseBuild<String> createTask(@RequestBody StableDiffusionParam stableDiffusionParam) {
        return ResponseBuild.ok(stableDiffusionService.createTask(stableDiffusionParam));
    }

    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public ResponseBuild<StableDiffusionTaskDO> getTask(@RequestParam("sign") String sign, @RequestParam("t") Long timestamp) {
        return ResponseBuild.ok(stableDiffusionService.getTask(sign,timestamp));
    }

    @RequestMapping(value = "/upload-image", method = RequestMethod.POST)
    public String uploadImage(MyHttpServletRequest request, @Param("file") MultipartFile file) throws IOException {
        String taskId = request.getHeader("uuid");
        String sign = request.getHeader("sign");
        String seed = request.getHeader("seed");
        return stableDiffusionService.uploadImage(taskId, Long.parseLong(seed), sign, file);
    }
}
