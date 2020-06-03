package club.dbg.cms.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * ContentApplication.class
 *
 * @author dbg
 * @date 2019/08/15
 */

@EnableAutoConfiguration
@EnableDiscoveryClient
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class, args);
    }
}
