package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.ProgramDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dbg
 * @date 2020/09/06
 */
@Repository
public interface ProgramMapper {
    List<ProgramDO> selectList(@Param("described") String described,
                               @Param("language") String language,
                               @Param("page") Integer page,
                               @Param("pageSize")Integer pageSize);

    ProgramDO selectById(@Param("id") Integer id);


    Integer insert(ProgramDO programDO);

    @Update({
            "UPDATE tb_program ",
            "SET update_time = #{updateTime},source_code = #{sourceCode} ",
            "WHERE id = #{id}"
    })
    Integer update(ProgramDO programDO);

    @Delete({
            "DELETE FROM tb_program WHERE id = #{id}"
    })
    Integer delete(@Param("id") Integer id);

    @Update({
            "UPDATE tb_program SET `status` = #{status} WHERE id = #{id}"
    })
    Integer updateStatus(@Param("id") Integer id, @Param("status")Integer status);
}
