package club.dbg.cms.admin.dao;

import club.dbg.cms.domain.admin.StableDiffusionTaskDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface StableDiffusionTaskMapper {
    @Select("SELECT uuid, params FROM stable_diffusion_task WHERE completion_time is null LIMIT 1")
    StableDiffusionTaskDO selectParams();

    @Select({
            "SELECT id, uuid, creation_time, completion_time, width, height, sampling_steps, ",
            "cfg_scale, sampling_method, params, image_path, seed FROM stable_diffusion_task WHERE uuid = #{uuid}"
    })
    StableDiffusionTaskDO selectByUuid(@Param("uuid") String uuid);

    @Update({
            "UPDATE stable_diffusion_task SET seed = #{seed}, params = #{params}, image_path = #{imagePath}, ",
            "completion_time = #{completionTime} WHERE id = #{id}"
    })
    int taskUpdate(Integer id, Long seed, String params, String imagePath, Integer completionTime);

    @Insert({
            "INSERT INTO stable_diffusion_task(uuid, creation_time, width, height, sampling_steps, cfg_scale, ",
            "sampling_method, params, seed) ",
            "VALUES(#{uuid}, #{creationTime}, #{width}, #{height}, #{samplingSteps}, #{cfgScale}, #{samplingMethod}, ",
            "#{params}, #{seed})"
    })
    int insert(StableDiffusionTaskDO taskDO);
}
