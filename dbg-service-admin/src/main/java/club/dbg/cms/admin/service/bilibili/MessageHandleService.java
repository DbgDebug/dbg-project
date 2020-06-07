package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.service.bilibili.pojo.GiftStatistic;
import club.dbg.cms.admin.service.bilibili.pojo.WelcomeStatistic;
import club.dbg.cms.domain.admin.DanmuDO;
import club.dbg.cms.domain.admin.GiftDO;
import club.dbg.cms.domain.admin.GuardDO;

import java.security.Guard;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface MessageHandleService {
    List<DanmuDO> getDanmuCacheList();

    ConcurrentLinkedQueue<DanmuDO> getDanmuCacheQueue();

    List<GiftDO> getGiftCacheList();

    ConcurrentLinkedQueue<GiftDO> getGiftCacheQueue();

    void danmuHandle(DanmuDO danmuDO) throws InterruptedException;

    void guardHandle(GuardDO guardDO) throws InterruptedException;

    void giftHandle(GiftDO giftDO) throws InterruptedException;

    void giftStatisticHandle(GiftStatistic giftStatistic) throws InterruptedException;

    void welcomeStatisticHandle(WelcomeStatistic welcomeStatistic) throws InterruptedException;

    void insertCacheDanmu() throws InterruptedException;
}
