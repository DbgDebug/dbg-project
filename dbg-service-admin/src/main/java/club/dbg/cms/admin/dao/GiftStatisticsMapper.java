package club.dbg.cms.admin.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GiftStatisticsMapper {
    @Insert({
            "INSERT INTO tb_gift_statistics(room_id, gift_name, num, price, `date`)",
            "SELECT room_id, gift_name, sum(gift_num) as num, price, ${date} as `date`",
            "FROM tb_gift WHERE send_time >= #{startTime} AND send_time <= #{endTime}",
            "GROUP BY room_id, gift_name, price;"
    })
    void giftStatistics(
            @Param("date") Long date,
            @Param("startTime") Long startTime,
            @Param("endTime") Long entTime);

    @Insert({
            "INSERT INTO tb_gift_statistics(room_id, gift_name, num, price, `date`)",
            "SELECT room_id, gift_name, num, price, ${date} as `date`",
            "FROM tb_guard WHERE send_time >= #{startTime} AND send_time <= #{endTime}",
            "GROUP BY room_id, gift_name, num, price;"
    })
    void guardStatistics(@Param("date") Long date,
                         @Param("startTime") Long startTime,
                         @Param("endTime") Long entTime);
}
