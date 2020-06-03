package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.DanmuMapper;
import club.dbg.cms.admin.dao.GiftMapper;
import club.dbg.cms.admin.dao.GiftStatisticMapper;
import club.dbg.cms.admin.dao.WelcomeStatisticMapper;
import club.dbg.cms.admin.redis.RedisUtils;
import club.dbg.cms.admin.service.bilibili.pojo.GiftStatistic;
import club.dbg.cms.admin.service.bilibili.pojo.WelcomeStatistic;
import club.dbg.cms.admin.service.database.DataBaseService;
import club.dbg.cms.domain.admin.DanmuDO;
import club.dbg.cms.domain.admin.GiftDO;
import club.dbg.cms.domain.admin.GiftStatisticDO;
import club.dbg.cms.domain.admin.WelcomeStatisticDO;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MessageHandleServiceImpl implements MessageHandleService {
    private final DanmuMapper danmuMapper;

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
                                    DataBaseService dataBaseService, RedisUtils redisUtils) {
        this.danmuMapper = danmuMapper;
        this.giftMapper = giftMapper;
        this.giftStatisticMapper = giftStatisticMapper;
        this.welcomeStatisticMapper = welcomeStatisticMapper;
        this.dataBaseService = dataBaseService;
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

    private final LongAdder danmuCount = new LongAdder();
    @Override
    public void danmuHandle(DanmuDO danmu) throws InterruptedException {
        int danmuListMax = 100;
        danmuCount.add(1L);
        danmuLinkedQueue.offer(danmu);
        if(danmuCount.longValue() > danmuListMax){
            final ReentrantLock danmuLock = this.danmuLock;
            danmuLock.lockInterruptibly();
            try {
                if(danmuCount.longValue() > danmuListMax){
                    DanmuWriteTask danmuWriteTask = new DanmuWriteTask(danmuMapper, danmuLinkedQueue);
                    danmuLinkedQueue = new ConcurrentLinkedQueue<>();
                    dataBaseService.submitTask(danmuWriteTask);
                }
                danmuCount.reset();
            }finally {
                danmuLock.unlock();
            }
        }
    }

    private final LongAdder giftCount = new LongAdder();
    @Override
    public void giftHandle(GiftDO giftDO) throws InterruptedException {
        int giftListMax = 200;
        giftCount.add(1L);
        giftLinkedQueue.offer(giftDO);
        if(giftCount.longValue() > giftListMax){
            final ReentrantLock giftLock = this.giftLock;
            giftLock.lockInterruptibly();
            try {
                if(giftCount.longValue() > giftListMax){
                    GiftWriteTask giftWriteTask = new GiftWriteTask(giftMapper, giftLinkedQueue);
                    giftLinkedQueue = new ConcurrentLinkedQueue<>();
                    dataBaseService.submitTask(giftWriteTask);
                }
                giftCount.reset();
            }finally {
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
    public void insertCacheDanmu() throws InterruptedException {
        ConcurrentLinkedQueue<DanmuDO> linkedQueue = danmuLinkedQueue;
        danmuLinkedQueue = new ConcurrentLinkedQueue<>();
        DanmuWriteTask danmuWriteTask = new DanmuWriteTask(danmuMapper, linkedQueue);
        dataBaseService.submitTask(danmuWriteTask);
    }
}
