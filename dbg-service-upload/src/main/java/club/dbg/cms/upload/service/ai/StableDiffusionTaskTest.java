package club.dbg.cms.upload.service.ai;

import club.dbg.cms.util.BASE64;
import club.dbg.cms.util.FileUtils;
import club.dbg.cms.util.SHA;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StableDiffusionTaskTest {
    private final Logger log = LoggerFactory.getLogger(StableDiffusionTaskTest.class);

    private final RestTemplate restTemplate = new RestTemplate();

    public final Pattern readData = Pattern.compile("base64,(.*?)\"");

    @Test
    public void run() throws IOException {
        String params = "{\"fn_index\":12,\"data\":[\"{{{masterpiece}}}, best quality, illustration,dark background, sunlight, beautiful detailed eyes, 1girl, expressionless, {{{white short hair}}}, {{wavy hair}},{{{dishevled hair}}}, messy hair, long bangs, hairs between eyes, {{{{white bloomers}}}},{{open clothes}},{detailed face},{an extremely delicate and beautiful}\",\"nsfw, lowres, bad anatomy, bad hands, text,error, missing fngers,extra digt ,fewer digits,cropped, wort quality ,low quality,normal quality, jpeg artifacts,signature,watermark, username, blurry, bad feet\",\"None\",\"None\",27,\"Euler a\",false,false,1,1,8.5,-1,-1,0,0,0,false,512,512,false,false,0.7,\"None\",false,false,null,\"\",\"Seed\",\"\",\"Nothing\",\"\",true,false,null,\"\",\"\"],\"session_hash\":\"aw1nft6vehf\"}";
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
            return;
        }
        byte[] bytes = BASE64.decode(data);
        long timestamp = System.currentTimeMillis() / 1000;
        FileUtils.writeFileByBytes("E:\\images", "\\" + timestamp + ".png", bytes);
    }

    @Test
    public void test() {
        long timestamp = System.currentTimeMillis();
        String sign = SHA.sha256(timestamp + "123456789");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://127.0.0.1:8700/v1/stable-diffusion/task?sign=" + sign + "&t=" + timestamp, String.class);
        String body = responseEntity.getBody();
        if (body == null) {
            System.out.println("body is null");
            return;
        }
        System.out.println(body);
        JSONObject jsonObject = JSON.parseObject(body);
        if (jsonObject == null) {
            System.out.println("jsonObject is null");
            return;
        }
        JSONObject dataJson = jsonObject.getJSONObject("data");
        StableDiffusionTask task = StableDiffusionTask.build(dataJson.getString("uuid"), dataJson.getString("params"), "http://127.0.0.1:8700/v1/stable-diffusion/upload-image", "123456789");
        task.run();
    }

}