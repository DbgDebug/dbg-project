package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.GiftStatisticsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.TimeZone;

@Service
public class BiliBiliStatistics {
    private static final Logger log = LoggerFactory.getLogger(BiliBiliStatistics.class);

    private final GiftStatisticsMapper giftStatisticsMapper;

    public BiliBiliStatistics(GiftStatisticsMapper giftStatisticsMapper) {
        this.giftStatisticsMapper = giftStatisticsMapper;
    }

    /**
     * 每天三点执行统计任务
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void giftStatistics() {
        //当前时间毫秒数
        long current = System.currentTimeMillis();
        //今天零点零分零秒的毫秒数
        long zero = current / (1000 * 3600 * 24) * (1000 * 3600 * 24)
                - TimeZone.getDefault().getRawOffset() + 24 * 60 * 60 * 1000;
        long date = zero - 24 * 60 * 60 * 1000;
        long endTime = date + 24 * 60 * 60 * 1000 -1;
        giftStatisticsMapper.guardStatistics(date, date, endTime);
        giftStatisticsMapper.giftStatistics(date, date, endTime);
    }
}
