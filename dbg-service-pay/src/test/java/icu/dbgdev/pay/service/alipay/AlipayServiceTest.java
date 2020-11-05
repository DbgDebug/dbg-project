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
                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCymLyglIwCJeaV80WKox9/nhAC+DFnGcWyrtRY+/KlPqOOMePYRYfT3TtC1emamrBMTTB/QVwZI1w4aUUAb7aWtNOUHgedx0v4xhy+s3vdEDXdNCuy+xRV/oZyIU667x2XoJU7eJtN7MkG+B9UgPdICbCmZ28AybxfMTO9+arKmhuLeE1tRP3xS4gF4v6eDlszeicTe9+Kv+i+24njgjbSEFZZYyNg2Eyuc/9cz+6jcveChCEUb7lGz7B9fEi1xA9qUX9q8zPTrXnBM/ElNpYapXehlmOk7SC4xXk13hjGG/I3VllHzxvh4ItwO7mS2TbvZ3S/kJd6xBuRCJraGSaRAgMBAAECggEBAIJFnvGS6T133+Zo+MTTj9pKtWs1FvQZfHeFxybRQIEAW9UQ1x2eOiF0MhHibu9y/j3p4AIy7rb3cAFF6CsVUZ5EcECOVwfjo6rm93iy+xqZBHFB6M45DvBbJD6DxSXd3TwIylIC+2Wv3kLTIidFmxpU1yHKONHeVSUqgo+EWexFmGwZdWGXpvUHhp/ZJ4SeCRjGY8O07E8G8oIS48Ou2+K2QfOwl2wPVEMaQjGHLtTpDUXps2OQWXd0clxQoixw7OtNhFTPzTLjyp9kqMHhDbc76mW5tUI92S5IGHt9Gjp8Qyo8+wKPDRsQE/47pNSXzkX1zn/YsG+4x/dFGgWbJnECgYEA/Zrxmadg8zAaT3SNlOz4XUX5RMA6YPcjBRfkw8oeZxmgOzeAJFL7li7iaetbQ8P0KMyLLn/uyLY5beYhx8tMFIOAgziQRHDnbJBufHUWonbFTep8Jy2zhBhd7tWz5cgieEiq4FstGEWnZer/IfjUXoajRtno91NzVmE7kqTmj18CgYEAtEh4TK4FwWJ/wGECbpvp8Qnufc6+OkmQq9L56a/Qasx0c2sh2YV7i+EbAz9S3Q+8UPnsuxJwHm2Cx2Vn1d1zHDsuCgAyYoIbYkc+3jxxWlnxlzsjxh22WSZ96RRIq/qembUXBP9X9tUGXOb7U/mS3w/IrNlth+cs0+yIq96hQA8CgYEAmCaYor4H7X9Dmb3sn5fYnmU32uaVX82F5i1io5Ck0uhN+JkguSpMJNt2HWQF+k8bMksdicVZZnPlWqkCRVMUDw6iUJfj/hvazIiOsP6ymMA3Obrfx8WkeTEBUiRGbSq4e3/lfPtWv8QyXXQyo5gdGaUfWoesPI6FmSJ20lE6vnMCgYBWKkjtBUOU8Rnz/amm6ff/D2sgUB7VRvHRXc26ZiBr2vpRa8PK7iRlb5p0i+Bz7aBo53rA0ogp0tdtq8bcDbKFrLdNaliqglrKNxkSKLtYnqJ8UHTBrNPJej81lk9Be1AwUMAG0G/fdmNLJc67jQ4odKfKDUVvbgsb4vJTit1w+wKBgGKpia0Yi2sUXD/l6NdsSDmdU2gER20pWw5/GI8goharJztKfl+/uBvgLq1kdOGgOHhlj42VJZ+iKrN9m5QXUlWrBp7UaWEOJuoDb0iSBGonkqbNYzWEYwNoimnqTcZbVty/fRPf8KienwsBK8GX2y/+bn87UjulrPVqTrIqDYk9",
                "json","utf-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuYUYVi9phHuSS2NTyWoXZ36l5mTaVeM+gwdM+xuhR3aZEx9NQiINi7gW9cm1q7my3jyYeAHk422k81YvuBQAd2ZnPITm7iRPfLjnhwMHdBJPghdlpRRXGp4Z1v1RIGSFG0/3S2HVWDV9ryobHMn5VlgmZHLzThK+ZJ1Dva2YRTAMS1kMY6tTGewXht2PnwY0q0TEqE390zulKRZWaQurFPWKjeiakvh2LsSJvBDFJ0cXvU0GUcZIqM9pHSYkOITnmskNtjJd2OVMwU2koN/X9kESrMgp/Dh2saTaXwi4TlZmI5DdlIfYLjvcsOCOJCuz2f5fquvv5nUQXSZlCSUEYQIDAQAB","RSA2");
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
        config.merchantPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCymLyglIwCJeaV80WKox9/nhAC+DFnGcWyrtRY+/KlPqOOMePYRYfT3TtC1emamrBMTTB/QVwZI1w4aUUAb7aWtNOUHgedx0v4xhy+s3vdEDXdNCuy+xRV/oZyIU667x2XoJU7eJtN7MkG+B9UgPdICbCmZ28AybxfMTO9+arKmhuLeE1tRP3xS4gF4v6eDlszeicTe9+Kv+i+24njgjbSEFZZYyNg2Eyuc/9cz+6jcveChCEUb7lGz7B9fEi1xA9qUX9q8zPTrXnBM/ElNpYapXehlmOk7SC4xXk13hjGG/I3VllHzxvh4ItwO7mS2TbvZ3S/kJd6xBuRCJraGSaRAgMBAAECggEBAIJFnvGS6T133+Zo+MTTj9pKtWs1FvQZfHeFxybRQIEAW9UQ1x2eOiF0MhHibu9y/j3p4AIy7rb3cAFF6CsVUZ5EcECOVwfjo6rm93iy+xqZBHFB6M45DvBbJD6DxSXd3TwIylIC+2Wv3kLTIidFmxpU1yHKONHeVSUqgo+EWexFmGwZdWGXpvUHhp/ZJ4SeCRjGY8O07E8G8oIS48Ou2+K2QfOwl2wPVEMaQjGHLtTpDUXps2OQWXd0clxQoixw7OtNhFTPzTLjyp9kqMHhDbc76mW5tUI92S5IGHt9Gjp8Qyo8+wKPDRsQE/47pNSXzkX1zn/YsG+4x/dFGgWbJnECgYEA/Zrxmadg8zAaT3SNlOz4XUX5RMA6YPcjBRfkw8oeZxmgOzeAJFL7li7iaetbQ8P0KMyLLn/uyLY5beYhx8tMFIOAgziQRHDnbJBufHUWonbFTep8Jy2zhBhd7tWz5cgieEiq4FstGEWnZer/IfjUXoajRtno91NzVmE7kqTmj18CgYEAtEh4TK4FwWJ/wGECbpvp8Qnufc6+OkmQq9L56a/Qasx0c2sh2YV7i+EbAz9S3Q+8UPnsuxJwHm2Cx2Vn1d1zHDsuCgAyYoIbYkc+3jxxWlnxlzsjxh22WSZ96RRIq/qembUXBP9X9tUGXOb7U/mS3w/IrNlth+cs0+yIq96hQA8CgYEAmCaYor4H7X9Dmb3sn5fYnmU32uaVX82F5i1io5Ck0uhN+JkguSpMJNt2HWQF+k8bMksdicVZZnPlWqkCRVMUDw6iUJfj/hvazIiOsP6ymMA3Obrfx8WkeTEBUiRGbSq4e3/lfPtWv8QyXXQyo5gdGaUfWoesPI6FmSJ20lE6vnMCgYBWKkjtBUOU8Rnz/amm6ff/D2sgUB7VRvHRXc26ZiBr2vpRa8PK7iRlb5p0i+Bz7aBo53rA0ogp0tdtq8bcDbKFrLdNaliqglrKNxkSKLtYnqJ8UHTBrNPJej81lk9Be1AwUMAG0G/fdmNLJc67jQ4odKfKDUVvbgsb4vJTit1w+wKBgGKpia0Yi2sUXD/l6NdsSDmdU2gER20pWw5/GI8goharJztKfl+/uBvgLq1kdOGgOHhlj42VJZ+iKrN9m5QXUlWrBp7UaWEOJuoDb0iSBGonkqbNYzWEYwNoimnqTcZbVty/fRPf8KienwsBK8GX2y/+bn87UjulrPVqTrIqDYk9";
        // 注：证书文件路径支持设置为文件系统中的路径或CLASS_PATH中的路径，优先从文件系统中加载，加载失败后会继续尝试从CLASS_PATH中加载
        // config.merchantCertPath = "<-- 请填写您的应用公钥证书文件路径，例如：/foo/appCertPublicKey_2019051064521003.crt -->";
        // config.alipayCertPath = "<-- 请填写您的支付宝公钥证书文件路径，例如：/foo/alipayCertPublicKey_RSA2.crt -->";
        // config.alipayRootCertPath = "<-- 请填写您的支付宝根证书文件路径，例如：/foo/alipayRootCert.crt -->";

        // 注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuYUYVi9phHuSS2NTyWoXZ36l5mTaVeM+gwdM+xuhR3aZEx9NQiINi7gW9cm1q7my3jyYeAHk422k81YvuBQAd2ZnPITm7iRPfLjnhwMHdBJPghdlpRRXGp4Z1v1RIGSFG0/3S2HVWDV9ryobHMn5VlgmZHLzThK+ZJ1Dva2YRTAMS1kMY6tTGewXht2PnwY0q0TEqE390zulKRZWaQurFPWKjeiakvh2LsSJvBDFJ0cXvU0GUcZIqM9pHSYkOITnmskNtjJd2OVMwU2koN/X9kESrMgp/Dh2saTaXwi4TlZmI5DdlIfYLjvcsOCOJCuz2f5fquvv5nUQXSZlCSUEYQIDAQAB";

        // 可设置异步通知接收服务地址（可选）
        // "<-- 请填写您的支付类接口异步通知接收服务地址，例如：https://www.test.com/callback -->";
        // config.notifyUrl =  "https://api.dbg-dev.icu:9700/pay/callback/alipay";

        // 可设置AES密钥，调用AES加解密相关接口时需要（可选）
        // config.encryptKey = "<-- 请填写您的AES密钥，例如：aa4BtZ4tspm2wnXLb1ThQA== -->";

        return config;
    }

}