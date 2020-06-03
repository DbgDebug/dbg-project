package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.GiftMapper;
import club.dbg.cms.admin.service.database.DataWriteTask;
import club.dbg.cms.domain.admin.GiftDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GiftWriteTask implements DataWriteTask {
    private static final Logger log = LoggerFactory.getLogger(GiftWriteTask.class);

    private final GiftMapper giftMapper;
    
    private final ConcurrentLinkedQueue<GiftDO> giftLinkedQueue;

    public GiftWriteTask(GiftMapper giftMapper, ConcurrentLinkedQueue<GiftDO> giftLinkedQueue) {
        this.giftMapper = giftMapper;
        this.giftLinkedQueue = giftLinkedQueue;
    }

    @Override
    public void write() {
        try {
            List<GiftDO> giftDOS = new ArrayList<>(giftLinkedQueue);
            giftDOS.sort((gift1, gift2) -> {
                int diff = gift1.getSendTime() - gift2.getSendTime();
                return diff < 0 ? -1 : diff > 0 ? 1 : diff;
            });
            giftMapper.insertGifts(giftDOS);
        }catch (Exception e) {
            log.info("礼物信息写入失败:", e);
        }
    }
}
