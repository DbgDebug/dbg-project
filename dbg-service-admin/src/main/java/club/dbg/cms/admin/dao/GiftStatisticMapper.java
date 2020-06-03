package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.GiftStatisticDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftStatisticMapper {
    List<GiftStatisticDO> selectByExample(@Param("roomId") Integer roomId,
                                          @Param("startTime") Integer startTime,
                                          @Param("endTime") Integer endTime);

    @Insert({
            "INSERT INTO tb_gift_statistic(room_id, gift, start_time, end_time)",
            "values(#{roomId}, #{gift}, #{startTime}, #{endTime})"
    })
    int insertGiftStatistic(GiftStatisticDO giftStatisticDO);
}
