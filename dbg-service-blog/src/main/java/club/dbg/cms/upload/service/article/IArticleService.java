package club.dbg.cms.upload.service.article;

import club.dbg.cms.upload.service.article.pojo.ArticleDTO;
import club.dbg.cms.upload.service.article.pojo.ArticleListDTO;

public interface IArticleService {
    ArticleListDTO list(String keyWord, Integer page, Integer pageSize);

    ArticleListDTO tag(String tag);

    ArticleDTO get(Integer id);

    Integer add(ArticleDTO article);

    Boolean edit(ArticleDTO article);

    Boolean delete(Integer id);
}
