package icu.dbgdev.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {
    private final String serverUrl;
    private final String appId;
    private final String privateKey;
    private final String alipayPublicKey;
    public AlipayConfig(@Value("${alipay.serverUrl}") String serverUrl,
                        @Value("${alipay.appId}") String appId,
                        @Value("${alipay.privateKey}") String privateKey,
                        @Value("${alipay.alipayPublicKey}") String alipayPublicKey) {

        this.serverUrl = serverUrl;
        this.appId = appId;
        this.privateKey = privateKey;
        this.alipayPublicKey = alipayPublicKey;
    }
    @Bean
    public AlipayClient alipayClient() {
        return new DefaultAlipayClient(serverUrl, appId, privateKey, "json",
                "utf-8", alipayPublicKey, "RSA2");
    }
}
