package club.dbg.cms.upload.service.ai;

import club.dbg.cms.util.SHA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StableDiffusionService {
    private final static Logger log = LoggerFactory.getLogger(StableDiffusionService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    /*
    private final ThreadPoolExecutor taskThread = new ThreadPoolExecutor(
            1,
            1,
            0,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(1000),
            new ThreadPoolExecutor.AbortPolicy());*/

    private final String uploadUrl;

    private final String getTaskUrl;

    private final String privateKey;

    public StableDiffusionService(
            @Value("${stableDiffusion.uploadUrl}")
            String uploadUrl,
            @Value("${stableDiffusion.getTaskUrl}")
            String getTaskUrl,
            @Value("${stableDiffusion.privateKey}")
            String privateKey) {
        this.uploadUrl = uploadUrl;
        this.getTaskUrl = getTaskUrl;
        this.privateKey = privateKey;
    }

    // @Scheduled(fixedDelay = 2000)
    public void getTask() {
        long timestamp = System.currentTimeMillis();
        String sign = SHA.sha256(timestamp + privateKey);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getTaskUrl + "?sign=" + sign + "&t=" + timestamp, String.class);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.info("http status error:{}", responseEntity.getStatusCodeValue());
            return;
        }
        String body = responseEntity.getBody();
        if (body == null) {
            log.info("task is null");
            return;
        }
        System.out.println(body);
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            log.error("jsonObject is null");
            return;
        }
        JSONObject dataJson = jsonObject.getJSONObject("data");
        StableDiffusionTask task = StableDiffusionTask.build(dataJson.getString("uuid"), dataJson.getString("params"), uploadUrl, privateKey);
        // taskThread.execute(task);
        task.run();
    }
}
