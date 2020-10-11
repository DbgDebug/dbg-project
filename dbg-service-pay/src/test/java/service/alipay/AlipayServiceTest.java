package service.alipay;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AlipayServiceTest {

    @Test
    public void scanCodeTest() throws IOException, NotFoundException {
        // byte[] bytes = FileUtils.readFileByBytes("E:\\images\\qr.png");
        // byte[] bytes = {1,1,1,1,1,1,1,1};
        // ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        // InputStream inputStream = new
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(new File("E:" + File.separator + "images" + File.separator + "qr.png")));
        if(bufferedImage == null){
            return;
        }
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Map<DecodeHintType, String> hints = new HashMap<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        // 这里还可以是
        //BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        Result res = new MultiFormatReader().decode(bitmap, hints);
        System.out.println("图片中内容：" + res.getText());
        System.out.println("图片中格式：" + res.getBarcodeFormat());
    }

}