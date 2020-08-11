package club.dbg.cms.video.config;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;

@Component
public class PublicApiConfig {
    public static HashSet<String> apiSet = new HashSet<>();

    @PostConstruct
    public void init(){
        apiSet.add("/device/login");
        apiSet.add("/user/login");
    }
}
