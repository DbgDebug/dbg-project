package club.dbg.cms.upload.service.ai;

import club.dbg.cms.util.BASE64;
import club.dbg.cms.util.SHA;
import club.dbg.cms.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StableDiffusionTask implements Runnable {
    private final static Logger log = LoggerFactory.getLogger(StableDiffusionTask.class);

    private static final RestTemplate restTemplate = new RestTemplate();

    public final static Pattern readData = Pattern.compile("base64,(.*?)\"");
    public final static Pattern readSeed = Pattern.compile("Seed: (.*?),");

    private final String uuid;

    private final String params;

    private final String uploadUrl;

    private final String privateKey;

    public StableDiffusionTask(String uuid, String params, String uploadUrl, String privateKey) {
        this.uuid = uuid;
        this.params = params;
        this.uploadUrl = uploadUrl;
        this.privateKey = privateKey;
    }

    public static StableDiffusionTask build(String uuid, String params, String uploadUrl, String privateKey) {
        return new StableDiffusionTask(uuid, params, uploadUrl, privateKey);
    }

    @Override
    public void run() {
        HttpHeaders header = new HttpHeaders();
        header.add("accept", "application/json, text/plain, */*");
        header.add("accept-encoding", "deflate, br");
        header.add("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        header.add("content-type", "application/json;charset=UTF-8");
        HttpEntity<String> httpEntity = new HttpEntity<>(params, header);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://127.0.0.1:7860/api/predict/", httpEntity, String.class);
        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            log.error("请求失败:{}", responseEntity.getStatusCodeValue());
        }
        String body = responseEntity.getBody();
        if (body == null) {
            log.error("未返回图片");
            return;
        }
        Matcher matcher = readData.matcher(body);
        String data;
        if (matcher.find()) {
            data = matcher.group(1);
        } else {
            log.error("未获取到图片数据");
            return;
        }
        String seed;
        Matcher seedMtc = readSeed.matcher(body);
        if (seedMtc.find()) {
            seed = seedMtc.group(1);
        } else {
            log.error("未获取到种子数据");
            return;
        }
        byte[] bytes = BASE64.decode(data);
        HttpHeaders picHeader = new HttpHeaders();
        picHeader.setContentDispositionFormData("file", UUIDUtils.getUUID() + ".png");
        ByteArrayResource byteArrayResource = new ByteArrayResource(bytes);
        HttpEntity<ByteArrayResource> picturePart = new HttpEntity<>(byteArrayResource, picHeader);
        HttpHeaders headerUpload = new HttpHeaders();
        headerUpload.setContentType(MediaType.MULTIPART_FORM_DATA);
        headerUpload.add("sign", SHA.sha256(SHA.sha256(bytes) + uuid + seed + privateKey));
        headerUpload.add("uuid", uuid);
        headerUpload.add("seed", seed.trim());
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("file", picturePart);
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(multiValueMap, headerUpload);
        restTemplate.postForEntity(uploadUrl, entity, String.class);
    }
}
