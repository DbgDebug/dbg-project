package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.service.bilibili.pojo.DanmuConf;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class BiliBiliApiTest {

    @Test
    public void danmuConfTest() {
        try {
            // B 站提供的获取直播 id 的 api
            URL url = new URL("https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id="+ 1016);
            // 获取网络数据流
            InputStream con = url.openStream();
            // 按照 UTF-8 编码解析
            String data = new String(ByteStreams.toByteArray(con), StandardCharsets.UTF_8);
            // 关闭数据流
            con.close();
            System.out.println(data);
            JSONObject jsonObject = JSON.parseObject(data);
            jsonObject = jsonObject.getJSONObject("data");
            System.out.println(jsonObject.getString("token"));
            //
            //String hostServerList = jsonObject.getString("host_server_list");
            //List<DanmuConf> danmuConfList = JSON.parseArray(hostServerList, DanmuConf.class);
            //System.out.println(danmuConfList.get(0).getHost());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void strTest() {
        System.out.println(String.format("{\"uid\": %d, " +
                "\"roomid\": %d, " +
                "\"protover\": 1, " +
                "\"platform\": \"web\", " +
                "\"clientver\": \"1.8.2\", " +
                "\"type\": 2, " +
                "\"key\": \"%s\"}", 100000042121L, 1016, "token"));
    }

}