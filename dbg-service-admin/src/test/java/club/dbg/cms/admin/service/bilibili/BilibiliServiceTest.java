package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.DanmuMapper;
import club.dbg.cms.admin.dao.GiftStatisticsMapper;
import club.dbg.cms.admin.service.bilibili.pojo.GiftCount;
import club.dbg.cms.domain.admin.DanmuDO;
import club.dbg.cms.util.ZLibUtils;
import club.dbg.cms.util.bilibili.DanmuPatternUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// @SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BilibiliServiceTest {
    private final static Logger log = LoggerFactory.getLogger(BilibiliServiceTest.class);
    /*
     * ROOM_SILENT_OFF 直播结束
     * DANMU_MSG	收到弹幕
     * SEND_GIFT	有人送礼
     * WELCOME	欢迎加入房间
     * WELCOME_GUARD	欢迎房管加入房间
     * SYS_MSG	系统消息
     * NOTICE_MSG 也是系统信息
     * ENTRY_EFFECT 舰长进入房间信息
     * COMBO_SEND 连击礼物起始
     * COMBO_END 连击礼物结束
     * ROOM_RANK 周星榜
     * GUARD_MSG 开通舰长信息
     * GUARD_BUY 舰长购买信息
     * GUARD_LOTTERY_START 购买舰长后抽奖信息
     * RAFFLE_END 抽奖结果
     * SPECIAL_GIFT 神奇的东西，不知道是啥
     * WISH_BOTTLE 这又是啥
     */

    @Autowired
    DanmuMapper danmuMapper;

    @Autowired
    GiftStatisticsMapper giftStatisticsMapper;

    @Test
    public void webSocketTest() throws IOException, InterruptedException {

        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("danmu-pool-%d").daemon(true).build());
        Runnable runnable = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                i++;
                log.info("测试{}", i);
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(runnable, 3, 3, TimeUnit.SECONDS);
    }


    @Test
    public void sqlTest() {
        giftStatisticsMapper.guardStatistics(123123, 1590854400, 1590940799);
    }

    @Test
    public void danmuTest() {
        String danmu = "{\"cmd\":\"GUARD_BUY\",\"data\":{\"uid\":99212186,\"username\":\"biliの小电视\",\"guard_level\":3,\"num\":1,\"price\":198000,\"role_name\":10003,\"gift_name\":\"舰长\",\"start_time\":1590761565,\"end_time\":1590761565}}";
        String s = "{\"cmd\":\"DANMU_MSG\",\"info\":[[0,1,25,16777215,1590565272460,1590564193,0,\"2bf457d5\",0,0,0],\"敌国另一个黑科技要来了\",[12336935,\"虾饺w\",0,0,0,10000,1,\"\"],[1,\"剧毒\",\"在剧毒中心呼唤爱\",37034,6406234,\"\",0],[29,0,5805790,\"\\u003e50000\"],[\"cake-flour\",\"title-1-1\"],0,0,null,{\"ts\":1590565272,\"ct\":\"2E44493B\"},0,0,null,null,0]}";
        System.out.println(ZLibUtils.compress(s.getBytes()).length);
    }

    @Test
    public void danmuSortTest() {
        List<DanmuDO> danmuDOList = new ArrayList<>();
        DanmuDO danmuDO = new DanmuDO();
        danmuDO.setSendTime(1);
        danmuDOList.add(danmuDO);
        DanmuDO danmuDO1 = new DanmuDO();
        danmuDO1.setSendTime(5);
        danmuDOList.add(danmuDO1);
        DanmuDO danmuDO2 = new DanmuDO();
        danmuDO2.setSendTime(2);
        danmuDOList.add(danmuDO2);
        System.out.println(JSON.toJSONString(danmuDOList));
        danmuDOList.sort((danmu1, danmu2) -> {
            long diff = danmu1.getSendTime() - danmu2.getSendTime();
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        });
        System.out.println(JSON.toJSONString(danmuDOList));
    }

    @Test
    public void giftTest() {
        List<GiftTest> user = new ArrayList<>();
        List<GiftTest> giftTests = new ArrayList<>();
        Iterator<GiftTest> iterator = giftTests.iterator();
        List<GiftTest> newGift = new ArrayList<>();
        System.out.println(JSON.toJSONString(giftTests));
        while (iterator.hasNext()) {
            GiftTest giftTest = iterator.next();
            iterator.remove();
            if (!newGift.contains(giftTest)) {
                newGift.add(giftTest);
            } else {
                continue;
            }
            for (GiftTest giftTest1 : giftTests) {
                if (giftTest.equals(giftTest1)) {
                    giftTest.setCount(giftTest.getCount() + giftTest1.getCount());
                }
            }
        }
        System.out.println(JSON.toJSONString(newGift));
    }

    @Test
    public void patternTest() {
        String s = "{\"cmd\":\"DANMU_MSG\",\"info\":[[0,1,25,16777215,1590927903443,1590927459,0,\"36464f37\",0,0,0],\"北京大学出版社！\",[34934219,\"Axel_Zhai\",0,0,0,10000,1,\"\"],[12,\"大母鹅\",\"EdmundDZhang\",5050,10512625,\"\",0],[20,0,6406234,\"\\u003e50000\"],[\"title-128-1\",\"title-128-1\"],0,0,null,{\"ts\":1590927903,\"ct\":\"12E402CB\"},0,0,null,null,0]}";
        Pattern readDanmuSendTime = Pattern.compile("\\[\\[\\d+,\\d+,\\d+,\\d+,(\\d+)");
        Pattern readDanmuUid = Pattern.compile(",\\[(\\d+)");
        long t1 = System.currentTimeMillis();
        Matcher matcher = readDanmuSendTime.matcher(s);
        Matcher mUid = readDanmuUid.matcher(s);
        if (matcher.find() && mUid.find()) {
            long a = Long.parseLong(matcher.group(1));
            long uid = Long.parseLong(mUid.group(1));
            System.out.println("uid:" + uid);
            System.out.println("time:" + (System.currentTimeMillis() - t1));
        } else {
            System.out.println("null");
        }
    }

    @Test
    public void jsonTest() {
        String s = "{\"cmd\":\"DANMU_MSG\",\"info\":[[0,1,25,16777215,1590927903443,1590927459,0,\"36464f37\",0,0,0],\"北京大学出版社！\",[34934219,\"Axel_Zhai\",0,0,0,10000,1,\"\"],[12,\"大母鹅\",\"EdmundDZhang\",5050,10512625,\"\",0],[20,0,6406234,\"\\u003e50000\"],[\"title-128-1\",\"title-128-1\"],0,0,null,{\"ts\":1590927903,\"ct\":\"12E402CB\"},0,0,null,null,0]}";
        long t1 = System.currentTimeMillis();
        JSONObject jsonObj = JSON.parseObject(s);
        JSONArray infoJsonArr = jsonObj.getJSONArray("info");
        // String danmuText = infoJsonArr.getString(1);
        // int uid = infoJsonArr.getJSONArray(2).getInteger(0);
        // String nickname = infoJsonArr.getJSONArray(2).getString(1);
        long sendTime = infoJsonArr.getJSONArray(0).getLong(4);
        System.out.println("a:" + sendTime);
        System.out.println("time:" + (System.currentTimeMillis() - t1));
    }

    @Test
    public void bitTest() {
        int a = 0xF;
        System.out.println(a >> 2);
    }

    static class GiftTest {
        private int count;
        private int giftId;
        private String giftName;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GiftTest giftTest = (GiftTest) o;

            return giftId == giftTest.giftId;
        }

        @Override
        public int hashCode() {
            return giftId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getGiftId() {
            return giftId;
        }

        public void setGiftId(int giftId) {
            this.giftId = giftId;
        }

        public String getGiftName() {
            return giftName;
        }

        public void setGiftName(String giftName) {
            this.giftName = giftName;
        }
    }

}