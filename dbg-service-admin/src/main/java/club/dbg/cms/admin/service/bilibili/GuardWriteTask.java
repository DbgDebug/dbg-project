package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.GuardMapper;
import club.dbg.cms.admin.service.database.DataWriteTask;
import club.dbg.cms.domain.admin.GuardDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuardWriteTask implements DataWriteTask {
    private static final Logger log = LoggerFactory.getLogger(GuardWriteTask.class);

    private final GuardMapper guardMapper;

    private final GuardDO guardDO;

    public GuardWriteTask(GuardMapper guardMapper, GuardDO guardDO) {
        this.guardMapper = guardMapper;
        this.guardDO = guardDO;
    }

    @Override
    public void write() {
        try {
            guardMapper.insert(guardDO);
        }catch (Exception e){
            log.warn("舰长购买信息写入失败:", e);
        }
    }
}
