package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.DanmuDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dbg
 */
@Repository
public interface DanmuMapper {
    int insertDanmuList(List<DanmuDO> danmuList);

    List<DanmuDO> selectDanmuList(@Param("roomid") Integer roomid, @Param("startTime") Long startTime,
                                  @Param("endTime") Long endTime, @Param("page") Integer page,
                                  @Param("pageSize") Integer pageSize);

    List<DanmuDO> selectDanmuByLimit(@Param("roomid") Integer roomid, @Param("startTime") Long startTime,
                                     @Param("endTime") Long endTime, @Param("page") Integer page,
                                     @Param("pageSize") Integer pageSize);

    int danmuCount(@Param("roomid") Integer roomid,
                   @Param("startTime") Long startTime,
                   @Param("endTime") Long endTime);
}
