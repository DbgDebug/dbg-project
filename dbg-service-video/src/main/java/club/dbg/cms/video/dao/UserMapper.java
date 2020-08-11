package club.dbg.cms.video.dao;

import club.dbg.cms.video.domain.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<UserDO> {
    @Select({
            "SELECT ID, UserName, Password, CreateTime, UpdateTime, Status ",
            "From User WHERE UserName = #{userName}"
    })
    UserDO selectByUserName(@Param("userName") String userName);

    @Select({
            "SELECT ID, UserName, Password, CreateTime, UpdateTime, Status ",
            "From User WHERE ID = #{id}"
    })
    UserDO selectByID(@Param("id") Integer id);
}
