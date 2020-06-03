package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.service.bilibili.pojo.DanmuConf;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BiliBiliApi {
    @Value("${bilibili.roomInitUrl}")
    String roomInit;

    /**
     * 获取真实的直播房间 ID
     *
     * @param roomId 直播房间 ID
     * @return 真实的直播房间 ID
     */
    public String getRoomId(int roomId) {
        // 初始化
        String realRoomId = null;
        try {
            // B 站提供的获取直播 id 的 api
            URL url = new URL(roomInit + "?id=" + roomId);
            // 获取网络数据流
            InputStream con = url.openStream();
            // 按照 UTF-8 编码解析
            String data = new String(IOUtils.toByteArray(con), StandardCharsets.UTF_8);
            // 关闭数据流
            con.close();
            // 简单的 JSON 不需要用 Gson 解析，正则省事
            Matcher matcher = Pattern.compile("\"room_id\":(\\d+),").matcher(data);
            if (matcher.find()) {
                realRoomId = matcher.group(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return realRoomId;
    }

    public DanmuConf getDanmuConf(int roomId) {
        String token;
        List<DanmuConf> danmuConfList = new ArrayList<>();
        try {
            URL url = new URL("https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id="+ roomId);
            // 获取网络数据流
            InputStream con = url.openStream();
            // 按照 UTF-8 编码解析
            String data = new String(IOUtils.toByteArray(con), StandardCharsets.UTF_8);
            // 关闭数据流
            con.close();
            JSONObject jsonObject = JSON.parseObject(data);
            jsonObject = jsonObject.getJSONObject("data");
            token = jsonObject.getString("token");
            danmuConfList = JSON.parseArray(jsonObject.getString("host_server_list"), DanmuConf.class);
            danmuConfList.get(0).setToken(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return danmuConfList.get(0);
    }
}
