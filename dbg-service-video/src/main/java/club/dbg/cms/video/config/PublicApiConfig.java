package club.dbg.cms.video.config;

import java.util.HashSet;

public class PublicApiConfig {
    public static HashSet<String> apiSet = new HashSet<>(){{
        apiSet.add("/device/login");
        apiSet.add("/user/login");
        apiSet.add("/WebSocket");
        apiSet.add("/device/video");
        apiSet.add("/user/video");
    }};
}
