package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.util.bilibili.DanmuPatternUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.ByteStreams;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;

public class BiliBiliApiTest {
    @Test
    public void timeTest() {
        long current = System.currentTimeMillis();
        long zero = (current / (1000 * 3600 * 24) * (1000 * 3600 * 24)
                - TimeZone.getTimeZone("Asia/Shanghai").getRawOffset() + 24 * 60 * 60 * 1000) / 1000;
        long date = zero - 24 * 60 * 60;
        long endTime = date + 24 * 60 * 60 - 1;
        System.out.println(zero);
        System.out.println(date);
        System.out.println(endTime);
    }

    @Test
    public void guardTest() {
        String msg = "{\"cmd\":\"GUARD_BUY\",\"data\":{\"uid\":1464150,\"username\":\"偃玥Elaine\",\"guard_level\":3,\"num\":1,\"price\":198000,\"role_name\":10003,\"gift_name\":\"舰长\",\"start_time\":1591886563,\"end_time\":1591886563}}";
        Matcher mUid = DanmuPatternUtils.readUserId.matcher(msg);
        Matcher mUsername = DanmuPatternUtils.readUsername.matcher(msg);
        Matcher mGuardLevel = DanmuPatternUtils.readGuardLevel.matcher(msg);
        Matcher mGiftName = DanmuPatternUtils.readGuardGiftName.matcher(msg);
        Matcher mNum = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mPrice = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mSendTime = DanmuPatternUtils.readStartTime.matcher(msg);

        if (!(mUid.find() && mUsername.find()
                && mGuardLevel.find() && mGiftName.find()
                && mNum.find() && mPrice.find()
                && mSendTime.find())) {
            System.out.println("舰长购买信息解析失败:" + msg);
            return;
        }
    }

    @Test
    public void danmuConfTest() {
        try {
            // B 站提供的获取直播 id 的 api
            URL url = new URL("https://api.live.bilibili.com/room/v1/Danmu/getConf?room_id=" + 1016);
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