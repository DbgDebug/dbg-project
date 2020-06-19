package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.GiftStatisticsMapper;
import club.dbg.cms.admin.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class BiliBiliStatistics {
    private static final Logger log = LoggerFactory.getLogger(BiliBiliStatistics.class);

    private final MessageHandleService messageHandleService;

    private final GiftStatisticsMapper giftStatisticsMapper;

    private final DataSourceTransactionManager transactionManager;

    public BiliBiliStatistics(MessageHandleService messageHandleService, GiftStatisticsMapper giftStatisticsMapper,
                              DataSourceTransactionManager transactionManager) {
        this.messageHandleService = messageHandleService;
        this.giftStatisticsMapper = giftStatisticsMapper;
        this.transactionManager = transactionManager;
    }

    /**
     * 每天三点执行统计任务
     */
    @Scheduled(cron = "0 0 03 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public boolean giftStatistics() {
        //当前时间毫秒数
        long current = System.currentTimeMillis();
        long zero = (current / (1000 * 3600 * 24) * (1000 * 3600 * 24)
                - TimeZone.getTimeZone("Asia/Shanghai").getRawOffset() + 24 * 60 * 60 * 1000) / 1000;
        long date = zero - 24 * 60 * 60;
        long endTime = date + 24 * 60 * 60 - 1;
        try {
            int count = giftStatisticsMapper.countByDate((int)date);
            if(count != 0){
                log.info("统计数据已存在");
                return false;
            }
            messageHandleService.insertCacheGift();
            giftStatisticsMapper.guardStatistics(date, date, endTime);
            giftStatisticsMapper.giftStatistics(date, date, endTime);
        } catch (Exception e) {
            log.warn("统计数据异常:", e);
            return false;
        }
        return true;
    }

    synchronized public boolean refreshStatistics(Integer date){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        if(!sdf.format(new Date(date * 1000L)).equals("00:00:00")){
            throw new BusinessException("输入时间不正确");
        }
        long endTime = date + 24 * 60 * 60 - 1;
        TransactionStatus transactionStatus = getTransactionStatus();
        try {
            giftStatisticsMapper.deleteByDate(date);
            int count = giftStatisticsMapper.countByDate(date);
            if(count != 0){
                throw new BusinessException("统计数据已存在");
            }

            messageHandleService.insertCacheGift();
            giftStatisticsMapper.guardStatistics((long)date, (long)date, endTime);
            giftStatisticsMapper.giftStatistics((long)date, (long)date, endTime);

            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            log.warn("统计数据异常:", e);
            transactionManager.rollback(transactionStatus);
            throw new BusinessException("数据统计异常");
        }
        return true;
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
        return transactionManager.getTransaction(transDefinition);
    }
}
