package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.WelcomeStatisticDO;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface WelcomeStatisticMapper {
    @Insert({
            "INSERT INTO tb_welcome_statistic(room_id, num, start_time, end_time)",
            "VALUES(#{roomId}, #{num}, #{startTime}, #{endTime})"
    })
    void insert(WelcomeStatisticDO welcomeStatisticDO);
}
