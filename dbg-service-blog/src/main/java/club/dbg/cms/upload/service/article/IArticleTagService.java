package club.dbg.cms.upload.service.article;

import club.dbg.cms.upload.service.article.pojo.ArticleTagDTO;

public interface IArticleTagService {
    Boolean createArticleTag(ArticleTagDTO articleTagDTO);

    Boolean updateArticleTag(ArticleTagDTO articleTagDTO);

    Boolean deleteArticleTag(Integer id);
}
