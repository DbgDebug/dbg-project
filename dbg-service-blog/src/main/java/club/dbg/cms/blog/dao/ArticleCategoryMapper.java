package club.dbg.cms.blog.dao;

import club.dbg.cms.blog.domain.ArticleCategoryDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCategoryMapper extends BaseMapper<ArticleCategoryDO> {
    int inserts(@Param("list") List<ArticleCategoryDO> articleCategoryDOS);

    @Select({
            "SELECT Id, ArticleId, CategoryId FROM ArticleCategory WHERE ArticleId = #{articleId}"
    })
    List<ArticleCategoryDO> selectListByArticleId(@Param("articleId") Integer articleId);

    @Delete({
            "DELETE FROM ArticleCategory WHERE ArticleId = #{articleId}"
    })
    int deleteByArticleId(@Param("articleId") Integer articleId);
}
