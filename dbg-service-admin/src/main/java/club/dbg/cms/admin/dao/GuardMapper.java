package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.GuardDO;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface GuardMapper {
    @Insert({
            "INSERT INTO tb_guard(uid, username, guard_level, gift_name, num, price, send_time, room_id)",
            "VALUES(#{uid}, #{username}, #{guardLevel}, #{num}, #{price}, #{sendTime}, #{roomId})"
    })
    int insert(GuardDO guardDO);
}
