package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.GiftStatisticDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftStatisticsMapper {

    @Select({
            "SELECT ID, room_id, gift_name, num, price, `date`, paid_gift ",
            "FROM tb_gift_statistics ",
            "WHERE `date` = #{date}"
    })
    List<GiftStatisticDO> select(@Param("date") Integer date);

    @Select({
            "SELECT COUNT(*) ",
            "FROM tb_gift_statistics ",
            "WHERE `date` = #{date}"
    })
    int countByDate(@Param("date") Integer date);

    @Delete({
            "DELETE FROM tb_gift_statistics WHERE `date` = #{date}"
    })
    void deleteByDate(@Param("date") Integer date);

    @Insert({
            "INSERT INTO tb_gift_statistics(room_id, gift_name, num, price, `date`, paid_gift)",
            "SELECT room_id, gift_name, sum(gift_num) as num, price, ${date} as `date`, paid_gift",
            "FROM tb_gift WHERE send_time >= #{startTime} AND send_time <= #{endTime}",
            "GROUP BY room_id, gift_name, price, paid_gift;"
    })
    void giftStatistics(
            @Param("date") Integer date,
            @Param("startTime") Integer startTime,
            @Param("endTime") Integer entTime);

    @Insert({
            "INSERT INTO tb_gift_statistics(room_id, gift_name, num, price, `date`, paid_gift)",
            "SELECT room_id, gift_name, SUM(num), price, ${date} as `date`, 1 as paid_gift",
            "FROM tb_guard WHERE send_time >= #{startTime} AND send_time <= #{endTime}",
            "GROUP BY room_id, gift_name, price;"
    })
    void guardStatistics(@Param("date") Integer date,
                         @Param("startTime") Integer startTime,
                         @Param("endTime") Integer entTime);
}
