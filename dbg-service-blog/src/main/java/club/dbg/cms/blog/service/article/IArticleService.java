package club.dbg.cms.blog.service.article;

import club.dbg.cms.blog.service.article.pojo.ArticleDTO;
import club.dbg.cms.blog.service.article.pojo.ArticleListDTO;

import java.util.List;

public interface IArticleService {
    ArticleListDTO list(String keyWord, Integer page, Integer pageSize);

    ArticleListDTO tag(String tag);

    ArticleDTO get(Integer id);

    Integer add(ArticleDTO article);

    Boolean edit(ArticleDTO article);

    Boolean delete(Integer id);
}
