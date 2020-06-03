package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.WelcomeStatisticMapper;
import club.dbg.cms.admin.service.database.DataWriteTask;
import club.dbg.cms.domain.admin.WelcomeStatisticDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WelcomeWriteTask implements DataWriteTask {
    private static final Logger log = LoggerFactory.getLogger(WelcomeWriteTask.class);

    private final WelcomeStatisticMapper welcomeStatisticMapper;

    private final WelcomeStatisticDO welcomeStatisticDO;

    public WelcomeWriteTask(WelcomeStatisticMapper welcomeStatisticMapper, WelcomeStatisticDO welcomeStatisticDO) {
        this.welcomeStatisticMapper = welcomeStatisticMapper;
        this.welcomeStatisticDO = welcomeStatisticDO;
    }

    @Override
    public void write() {
        try {
            welcomeStatisticMapper.insert(welcomeStatisticDO);
        }catch (Exception e) {
            log.info("直播欢迎数据统计数据写入失败:", e);
        }
    }
}
