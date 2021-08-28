package club.dbg.cms.blog.dao;

import club.dbg.cms.blog.domain.ArticleDO;
import club.dbg.cms.blog.service.article.pojo.ArticleDTO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleMapper extends BaseMapper<ArticleDO> {
    List<ArticleDTO> selectArticleList(
            @Param("keyWord") String keyWord,
            @Param("page") Integer page,
            @Param("pageSize") Integer pageSize);

    Integer articleListCount(@Param("keyWord") String keyWord);

    List<ArticleDTO> selectByTag(@Param("tag") String tag);
}
