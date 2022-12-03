package club.dbg.cms.admin.service.paint;

import club.dbg.cms.admin.service.paint.pojo.StableDiffusionParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StableDiffusionServiceImplTest {

    @Autowired
    StableDiffusionService stableDiffusionService;

    @Test
    public void getTask() {
    }

    @Test
    public void createTask() {
        StableDiffusionParam param = new StableDiffusionParam();
        param.setWidth(512);
        param.setHeight(512);
        param.setPrompt("{{{masterpiece}}}, best quality, illustration,blue background, beautiful detailed eyes, 1girl, {{{white ponytail}}}, {{wavy hair}},{{{dishevled hair}}}, messy hair, long bangs, white t-shirt,{{detailed face}},{an extremely delicate and beautiful}");
        param.setNegativePrompt("nsfw, lowres, bad anatomy, bad hands, text,error, missing fngers,extra digt ,fewer digits,cropped, wort quality ,low quality,normal quality, jpeg artifacts,signature,watermark, username, blurry, bad feet");
        param.setSamplingSteps(27);
        param.setSamplingMethod("Euler a");
        param.setCfgScale(8.5F);
        param.setSeed(-1L);
        stableDiffusionService.createTask(param);
    }

    @Test
    public void uploadImage() {
    }
}