package club.dbg.cms.blog.dao;

import club.dbg.cms.blog.domain.CategoryDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper extends BaseMapper<CategoryDO> {
    List<CategoryDO> selectCategoryList(@Param("page") Integer page, @Param("pageSize") Integer pageSize);

    @Select({
            "SELECT Id, CategoryId, CategoryName, CreateTime, UpdateTime, Status",
            "FROM Category WHERE CategoryId = #{categoryId}"
    })
    CategoryDO selectByCategoryId(@Param("categoryId") String categoryId);

    @Select({
            "SELECT COUNT(*) FROM Category"
    })
    int categoryCount();
}
