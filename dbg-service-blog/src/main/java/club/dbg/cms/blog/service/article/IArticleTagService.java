package club.dbg.cms.blog.service.article;

import club.dbg.cms.blog.service.article.pojo.ArticleTagDTO;

public interface IArticleTagService {
    Boolean createArticleTag(ArticleTagDTO articleTagDTO);

    Boolean updateArticleTag(ArticleTagDTO articleTagDTO);

    Boolean deleteArticleTag(Integer id);
}
