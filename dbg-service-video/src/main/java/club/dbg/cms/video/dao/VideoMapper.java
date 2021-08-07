package club.dbg.cms.video.dao;

import club.dbg.cms.video.domain.VideoDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoMapper {
    @Select({
            "SELECT id,  user_id, video_id, creation_time, update_time FROM video WHERE id = #{id}"
    })
    VideoDO selectById(@Param("id") Integer id);

    @Insert({
            "INSERT INTO video(user_id, video_id, creation_time, update_time)",
            "VALUES(#{userId}, #{videoId}, #{creationTime}, #{updateTime})"
    })
    Integer insert(VideoDO video);
}
