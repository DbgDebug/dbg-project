package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.*;
import club.dbg.cms.admin.service.bilibili.pojo.GiftStatistic;
import club.dbg.cms.admin.service.bilibili.pojo.WelcomeStatistic;
import club.dbg.cms.admin.service.database.DataBaseService;
import club.dbg.cms.domain.admin.*;
import club.dbg.cms.util.bilibili.DanmuPatternUtils;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;

@Service
public class MessageHandleServiceImpl implements MessageHandleService {
    private static final Logger log = LoggerFactory.getLogger(MessageHandleServiceImpl.class);

    private final DanmuMapper danmuMapper;

    private final GuardMapper guardMapper;

    private final GiftMapper giftMapper;

    private final GiftStatisticMapper giftStatisticMapper;

    private final WelcomeStatisticMapper welcomeStatisticMapper;

    private final DataBaseService dataBaseService;

    private final ReentrantLock danmuLock = new ReentrantLock();

    private final ReentrantLock giftLock = new ReentrantLock();


    /**
     * 缓存弹幕用的链表
     */
    private ConcurrentLinkedQueue<DanmuDO> danmuLinkedQueue = new ConcurrentLinkedQueue<>();

    private ConcurrentLinkedQueue<GiftDO> giftLinkedQueue = new ConcurrentLinkedQueue<>();

    public MessageHandleServiceImpl(DanmuMapper danmuMapper, GiftMapper giftMapper,
                                    GiftStatisticMapper giftStatisticMapper,
                                    WelcomeStatisticMapper welcomeStatisticMapper,
                                    DataBaseService dataBaseService, GuardMapper guardMapper) {
        this.danmuMapper = danmuMapper;
        this.giftMapper = giftMapper;
        this.giftStatisticMapper = giftStatisticMapper;
        this.welcomeStatisticMapper = welcomeStatisticMapper;
        this.dataBaseService = dataBaseService;
        this.guardMapper = guardMapper;
    }

    @Override
    public void insertCacheDanmu() throws InterruptedException {
        danmuHandle(null, true);
    }

    @Override
    public void insertCacheGift() throws InterruptedException {
        giftHandle(null, true);
    }

    private final LongAdder danmuCount = new LongAdder();

    @Override
    public void danmuHandle(DanmuDO danmu, Boolean writeNow) throws InterruptedException {
        int danmuListMax = 100;
        danmuCount.add(1L);
        if (!writeNow) {
            danmuLinkedQueue.offer(danmu);
        }
        if (danmuCount.longValue() > danmuListMax || writeNow) {
            final ReentrantLock danmuLock = this.danmuLock;
            danmuLock.lockInterruptibly();
            try {
                if (danmuCount.longValue() > danmuListMax || writeNow) {
                    danmuCount.reset();
                    DanmuWriteTask danmuWriteTask = new DanmuWriteTask(danmuMapper, danmuLinkedQueue);
                    danmuLinkedQueue = new ConcurrentLinkedQueue<>();
                    dataBaseService.submitTask(danmuWriteTask);
                }
            } finally {
                danmuLock.unlock();
            }
        }
    }

    @Override
    public void guardHandle(GuardDO guardDO) throws InterruptedException {
        GuardWriteTask guardWriteTask = new GuardWriteTask(guardMapper, guardDO);
        dataBaseService.submitTask(guardWriteTask);
    }

    private final LongAdder giftCount = new LongAdder();

    @Override
    public void giftHandle(GiftDO giftDO, Boolean writeNow) throws InterruptedException {
        int giftListMax = 200;
        giftCount.add(1L);
        if (!writeNow) {
            giftLinkedQueue.offer(giftDO);
        }
        if (giftCount.longValue() > giftListMax || writeNow) {
            final ReentrantLock giftLock = this.giftLock;
            giftLock.lockInterruptibly();
            try {
                if (giftCount.longValue() > giftListMax || writeNow) {
                    giftCount.reset();
                    GiftWriteTask giftWriteTask = new GiftWriteTask(giftMapper, giftLinkedQueue);
                    giftLinkedQueue = new ConcurrentLinkedQueue<>();
                    dataBaseService.submitTask(giftWriteTask);
                }
            } finally {
                giftLock.unlock();
            }
        }
    }

    @Override
    public void giftStatisticHandle(GiftStatistic giftStatistic) throws InterruptedException {
        GiftStatisticDO giftStatisticDO = new GiftStatisticDO();
        giftStatisticDO.setRoomId(giftStatistic.getRoomId());
        giftStatisticDO.setGift(JSON.toJSONString(giftStatistic.getGiftMap().values()));
        giftStatisticDO.setStartTime(giftStatistic.getStartTime());
        giftStatisticDO.setEndTime(giftStatistic.getEndTime());
        dataBaseService.submitTask(new GiftStatisticWriteTask(giftStatisticMapper, giftStatisticDO));
    }

    @Override
    public void welcomeStatisticHandle(WelcomeStatistic welcomeStatistic) throws InterruptedException {
        WelcomeStatisticDO welcomeStatisticDO = new WelcomeStatisticDO();
        welcomeStatisticDO.setRoomId(welcomeStatistic.getRoomId());
        welcomeStatisticDO.setNum(welcomeStatistic.getWelcomeSet().size());
        welcomeStatisticDO.setStartTime(welcomeStatistic.getStartTime());
        welcomeStatisticDO.setEndTime(welcomeStatistic.getEndTime());
        dataBaseService.submitTask(new WelcomeWriteTask(welcomeStatisticMapper, welcomeStatisticDO));
    }

    @Override
    public void danmuMsg(Integer roomId, String msg) throws InterruptedException {
        Matcher mDanmu = DanmuPatternUtils.readDanmuInfo.matcher(msg);
        Matcher mUid = DanmuPatternUtils.readDanmuUid.matcher(msg);
        Matcher mNickname = DanmuPatternUtils.readDanmuUser.matcher(msg);
        Matcher mSendTime = DanmuPatternUtils.readDanmuSendTime.matcher(msg);

        if (!(mDanmu.find() && mUid.find()
                && mNickname.find() && mSendTime.find())) {
            log.warn("弹幕信息解析失败:{}", msg);
            return;
        }

        String danmuText = mDanmu.group(1);
        int uid = Integer.parseInt(mUid.group(1));
        String nickname = mNickname.group(1);
        long sendTime = Long.parseLong(mSendTime.group(1));
        DanmuDO danmu = new DanmuDO();
        danmu.setRoomid(roomId);
        danmu.setUid(uid);
        danmu.setDanmu(danmuText.length() > 30 ? danmuText.substring(0, 30) : danmuText);
        danmu.setNickname(nickname.length() > 20 ? nickname.substring(0, 20) : nickname);
        danmu.setSendTime(sendTime / 1000);
        danmuHandle(danmu, false);
    }

    @Override
    public void gifMsg(Integer roomId, String msg) throws InterruptedException {
        // 正则匹配
        Matcher mGiftName = DanmuPatternUtils.readGiftName.matcher(msg);
        Matcher mNum = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mUser = DanmuPatternUtils.readGiftUser.matcher(msg);
        Matcher mUid = DanmuPatternUtils.readUserId.matcher(msg);
        Matcher mGiftId = DanmuPatternUtils.readGiftId.matcher(msg);
        Matcher mPrice = DanmuPatternUtils.readGiftPrice.matcher(msg);
        Matcher mTimestamp = DanmuPatternUtils.readGiftSendTime.matcher(msg);
        Matcher mAction = DanmuPatternUtils.readGiftAction.matcher(msg);
        Matcher mSuperGiftNum = DanmuPatternUtils.readSuperGiftNum.matcher(msg);

        if (!(mGiftName.find() && mNum.find()
                && mGiftId.find() && mUid.find()
                && mUser.find() && mPrice.find()
                && mTimestamp.find()
                && mAction.find()
                && mSuperGiftNum.find())) {
            log.warn("礼物信息解析失败:{}", msg);
            return;
        }

        int giftId = Integer.parseInt(mGiftId.group(1));
        String giftName = DanmuPatternUtils.unicodeToString(mGiftName.group(1));
        int num = Integer.parseInt(mNum.group(1));
        String user = DanmuPatternUtils.unicodeToString(mUser.group(1));
        int uid = Integer.parseInt(mUid.group(1));
        int price = Integer.parseInt(mPrice.group(1));
        int sendTime = Integer.parseInt(mTimestamp.group(1));
        int sNum = Integer.parseInt(mSuperGiftNum.group(1));

        GiftDO giftDO = new GiftDO();
        giftDO.setRoomId(roomId);
        giftDO.setUid(uid);
        giftDO.setUsername(user.length() > 20 ? user.substring(0, 20) : user);
        giftDO.setGiftNum(num);
        giftDO.setGiftId(giftId);
        giftDO.setPrice(price);
        giftDO.setGiftName(giftName);
        giftDO.setSendTime(sendTime);
        giftDO.setPaidGift(sNum > 0 && price > 0 ? 1 : 0);
        giftHandle(giftDO, false);
    }

    @Override
    public void guardMsg(Integer roomId, String msg) throws InterruptedException {
        Matcher mUid = DanmuPatternUtils.readUserId.matcher(msg);
        Matcher mUsername = DanmuPatternUtils.readUsername.matcher(msg);
        Matcher mGuardLevel = DanmuPatternUtils.readGuardLevel.matcher(msg);
        Matcher mGiftName = DanmuPatternUtils.readGuardGiftName.matcher(msg);
        Matcher mNum = DanmuPatternUtils.readGiftNum.matcher(msg);
        Matcher mPrice = DanmuPatternUtils.readGiftPrice.matcher(msg);
        Matcher mSendTime = DanmuPatternUtils.readStartTime.matcher(msg);

        if (!(mUid.find() && mUsername.find()
                && mGuardLevel.find() && mGiftName.find()
                && mNum.find() && mPrice.find()
                && mSendTime.find())) {
            log.warn("舰长购买信息解析失败:{}", msg);
            return;
        }

        int uid = Integer.parseInt(mUid.group(1));
        String username = mUsername.group(1);
        int guardLevel = Integer.parseInt(mGuardLevel.group(1));
        String giftName = mGiftName.group(1);
        int num = Integer.parseInt(mNum.group(1));
        int price = Integer.parseInt(mPrice.group(1));
        int sendTime = Integer.parseInt(mSendTime.group(1));

        GuardDO guardDO = new GuardDO();
        guardDO.setUid(uid);
        guardDO.setUsername(username);
        guardDO.setGuardLevel(guardLevel);
        guardDO.setGiftName(giftName);
        guardDO.setNum(num);
        guardDO.setPrice(price);
        guardDO.setSendTime(sendTime);
        guardDO.setRoomId(roomId);
        guardHandle(guardDO);
    }

    @Override
    public List<DanmuDO> getDanmuCacheList() {
        return new ArrayList<>(danmuLinkedQueue);
    }

    @Override
    public ConcurrentLinkedQueue<DanmuDO> getDanmuCacheQueue() {
        return danmuLinkedQueue;
    }

    @Override
    public List<GiftDO> getGiftCacheList() {
        return new ArrayList<>(giftLinkedQueue);
    }

    @Override
    public ConcurrentLinkedQueue<GiftDO> getGiftCacheQueue() {
        return giftLinkedQueue;
    }
}
