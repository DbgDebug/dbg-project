package club.dbg.cms.admin.service.paint;

import club.dbg.cms.admin.dao.StableDiffusionTaskMapper;
import club.dbg.cms.admin.exception.BusinessException;
import club.dbg.cms.admin.service.paint.pojo.StableDiffusionParam;
import club.dbg.cms.domain.admin.StableDiffusionTaskDO;
import club.dbg.cms.util.FileUtils;
import club.dbg.cms.util.ImageUtils;
import club.dbg.cms.util.SHA;
import club.dbg.cms.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
public class StableDiffusionServiceImpl implements StableDiffusionService {
    private final static Logger log = LoggerFactory.getLogger(StableDiffusionServiceImpl.class);

    private final String rootDir;

    private final String imagesDir;

    private final String privateKey;

    private final StableDiffusionTaskMapper stableDiffusionTaskMapper;

    public StableDiffusionServiceImpl(
            @Value("${file.rootDir}")
            String rootDir,
            @Value("${file.imagesDir}")
            String imagesDir,
            @Value("${stableDiffusion.privateKey}")
            String privateKey,
            StableDiffusionTaskMapper stableDiffusionTaskMapper) {
        this.rootDir = rootDir;
        this.imagesDir = imagesDir;
        this.privateKey = privateKey;
        this.stableDiffusionTaskMapper = stableDiffusionTaskMapper;
    }

    @Override
    public StableDiffusionTaskDO getTask(String sign, Long timestamp) {
        long t = System.currentTimeMillis();
        if (t - timestamp > 2000) {
            return null;
        }
        String signCheck = SHA.sha256(timestamp + privateKey);
        if (!signCheck.equals(sign)) {
            return null;
        }
        return stableDiffusionTaskMapper.selectParams();
    }

    @Override
    public String createTask(StableDiffusionParam param) {
        if (param.getPrompt().length() > 800) {
            throw BusinessException.build("描述过长");
        }
        if (param.getNegativePrompt().length() > 800) {
            throw BusinessException.build("负面描述过长");
        }
        if (param.getCfgScale() < 1 || param.getCfgScale() > 30) {
            throw BusinessException.build("CFG Scale 范围 1-30");
        }
        if (param.getSamplingSteps() < 1 ||param.getSamplingSteps() > 150) {
            throw BusinessException.build("Sampling Steps 范围 1-150");
        }
        String params = "{\"fn_index\":12,\"data\":[\""
                + param.getPrompt()
                + "\",\""
                + param.getNegativePrompt()
                + "\",\"None\",\"None\","
                + param.getSamplingSteps()
                + ",\""
                + param.getSamplingMethod()
                + "\",false,false,1,1,"
                + param.getCfgScale()
                + ","
                + param.getSeed()
                + ",-1,0,0,0,false,"
                + param.getHeight() + "," + param.getWidth()
                + ",false,false,0.7,\"None\",false,false,null,\"\",\"Seed\",\"\",\"Nothing\",\"\",true,false,null,\"\",\"\"],"
                + "\"session_hash\":\"" + getSessionHash() + "\"}";
        StableDiffusionTaskDO taskDO = new StableDiffusionTaskDO();
        taskDO.setCfgScale(param.getCfgScale());
        taskDO.setWidth(param.getWidth());
        taskDO.setHeight(param.getHeight());
        taskDO.setCreationTime((int)(System.currentTimeMillis() / 1000));
        taskDO.setParams(params);
        taskDO.setSamplingMethod(param.getSamplingMethod());
        taskDO.setSamplingSteps(param.getSamplingSteps());
        taskDO.setSeed(param.getSeed());
        taskDO.setUuid(UUIDUtils.getUUID());
        if (stableDiffusionTaskMapper.insert(taskDO) != 1) {
            throw BusinessException.build("提交失败");
        }
        return taskDO.getUuid();
    }

    private String getSessionHash() {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
                'm', 'n', 'o', 'p', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 11; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }


    @Override
    public String uploadImage(String uuid, Long seed, String sign, MultipartFile file) throws IOException {
        if (uuid == null || sign == null) {
            return "FAIL";
        }
        byte[] bytes = file.getBytes();
        String signCheck = SHA.sha256(SHA.sha256(bytes) + uuid + seed + privateKey);
        if (!signCheck.equals(sign)) {
            log.error("验签错误");
            return "FAIL";
        }
        StableDiffusionTaskDO taskDO = stableDiffusionTaskMapper.selectByUuid(uuid);
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        FileUtils.writeFileByBytes(rootDir + imagesDir + dateStr + "/", uuid + ImageUtils.getImageType(bytes), bytes);
        String imagePath = imagesDir + dateStr + "/" + uuid + ImageUtils.getImageType(bytes);
        String params = taskDO.getParams();
        params = params.replace(",-1,-1,0,0,0,false", "," + seed + ",-1,0,0,0,false");
        System.out.println(params);
        stableDiffusionTaskMapper.taskUpdate(taskDO.getId(), seed, params, imagePath, (int) (timestamp / 1000));
        return "OK";
    }
}
