package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.GiftStatisticMapper;
import club.dbg.cms.admin.service.database.DataWriteTask;
import club.dbg.cms.domain.admin.GiftStatisticDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GiftStatisticWriteTask implements DataWriteTask {
    private static final Logger log = LoggerFactory.getLogger(GiftStatisticWriteTask.class);
    private final GiftStatisticDO giftStatisticDO;
    private final GiftStatisticMapper giftStatisticMapper;

    public GiftStatisticWriteTask(GiftStatisticMapper giftStatisticMapper,
                                  GiftStatisticDO giftStatisticDO) {
        this.giftStatisticMapper = giftStatisticMapper;
        this.giftStatisticDO = giftStatisticDO;
    }


    @Override
    public void write() {
        try {
            giftStatisticMapper.insertGiftStatistic(giftStatisticDO);
        }catch (Exception e){
            log.info("礼物统计数据写入失败:", e);
        }
    }
}
