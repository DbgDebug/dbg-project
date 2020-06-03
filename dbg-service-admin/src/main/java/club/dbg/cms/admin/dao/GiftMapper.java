package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.GiftDO;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftMapper {
    @Insert({
            "INSERT INTO tb_gift(room_id, uid, gift_num, gift_id, price, gift_name, username, send_time)",
            "values(#{roomId}, #{uid}, #{giftNum}, #{giftId}, #{price}, #{giftName}, #{username}, #{sendTime})"
    })
    void insertGift();

    void insertGifts(List<GiftDO> giftDOList);
}
