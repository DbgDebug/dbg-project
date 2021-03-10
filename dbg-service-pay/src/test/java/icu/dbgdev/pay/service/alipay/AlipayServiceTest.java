package icu.dbgdev.pay.service.alipay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
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

    @Test
    public void createPayEasySDKTest(){
        Factory.setOptions(getOptions());
        try {
            // 2. 发起API调用（以创建当面付收款二维码为例）
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                    .preCreate("这是一个测试支付5分钱", "21234567890", "0.05");
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                System.out.println("调用成功");
                System.out.println(JSON.toJSONString(response));
            } else {
                System.err.println("调用失败，原因：" + response.msg + "，" + response.subMsg);
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Test
    public void createPayOldTest() throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do","2016092500594904",
                "",
                "json","utf-8",
                "","RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("http://114.55.31.227:8910/v1/pay/alipay/notify");
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("out_trade_no","20150320010101008");
        paramMap.put("total_amount","12.00");
        paramMap.put("subject","测试");
        paramMap.put("timeout_express","30m");

        request.setBizContent(JSON.toJSONString(paramMap));

        com.alipay.api.response.AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            System.out.println(response.getBody());
        } else {
            System.out.println("调用失败");
        }

    }

    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com/gateway.do";
        config.signType = "RSA2";

        config.appId = "2016092500594904";

        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "";
        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        // config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        // config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";

        // 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = "";

        // 可设置异步通知接收服务地址（可选）
        // "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        // config.notifyUrl =  "https://api.dbg-dev.icu:9700/pay/callback/alipay";

        // 可设置AES密钥，调用AES加解密相关接口时需要（可选）
        // config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

        return config;
    }

}