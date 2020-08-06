package club.dbg.cms.admin.service.bilibili;

import club.dbg.cms.admin.dao.DanmuMapper;
import club.dbg.cms.admin.service.database.DataWriteTask;
import club.dbg.cms.domain.admin.DanmuDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class DanmuWriteTask implements DataWriteTask {
    private static final Logger log = LoggerFactory.getLogger(DataWriteTask.class);

    private final ConcurrentLinkedQueue<DanmuDO> danmuLinkedQueue;
    private final DanmuMapper danmuMapper;

    public DanmuWriteTask(DanmuMapper danmuMapper, ConcurrentLinkedQueue<DanmuDO> danmuLinkedQueue) {
        this.danmuMapper = danmuMapper;
        this.danmuLinkedQueue = danmuLinkedQueue;
    }

    @Override
    public void write() {
        try {
            List<DanmuDO> danmuDOList = new ArrayList<>(danmuLinkedQueue);
            if(danmuDOList.isEmpty()) {
                return;
            }
            danmuDOList.sort((danmu1, danmu2) -> {
                long diff = danmu1.getSendTime() - danmu2.getSendTime();
                return diff < 0 ? -1 : diff == 0 ? 0 : 1;
            });
            danmuMapper.insertDanmuList(danmuDOList);
        } catch (Exception e) {
            //处理写入数据库异常，暂不处理
            log.error("弹幕写入数据库异常:", e);
        }
    }
}
