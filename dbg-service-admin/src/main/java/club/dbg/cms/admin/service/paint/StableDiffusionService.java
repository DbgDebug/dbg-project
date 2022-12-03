package club.dbg.cms.admin.service.paint;

import club.dbg.cms.admin.service.paint.pojo.StableDiffusionParam;
import club.dbg.cms.domain.admin.StableDiffusionTaskDO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StableDiffusionService {
    StableDiffusionTaskDO getTask(String sign, Long timestamp);

    String createTask(StableDiffusionParam param);

    String uploadImage(String taskId,  Long seed, String sign, MultipartFile file) throws IOException;
}
