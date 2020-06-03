package club.dbg.cms.admin.service.bilibili.pojo;

import club.dbg.cms.domain.admin.DanmuDO;

import java.util.List;

public class DanmuList {
    private List<DanmuDO> danmuList;
    private int total;

    public List<DanmuDO> getDanmuList() {
        return danmuList;
    }

    public void setDanmuList(List<DanmuDO> danmuList) {
        this.danmuList = danmuList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
