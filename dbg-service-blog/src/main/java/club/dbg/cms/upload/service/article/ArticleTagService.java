package club.dbg.cms.upload.service.article;

import club.dbg.cms.upload.dao.ArticleTagMapper;
import club.dbg.cms.upload.domain.ArticleTagDO;
import club.dbg.cms.upload.exception.BlogException;
import club.dbg.cms.upload.service.article.pojo.ArticleTagDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleTagService implements IArticleTagService {
    private static final Logger log = LoggerFactory.getLogger(ArticleTagService.class);

    private final ArticleTagMapper articleTagMapper;

    public ArticleTagService(ArticleTagMapper articleTagMapper) {
        this.articleTagMapper = articleTagMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createArticleTag(ArticleTagDTO articleTagDTO) {
        if (articleTagMapper.insert(new ArticleTagDO(articleTagDTO)) != 1) {
            throw new BlogException("添加失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateArticleTag(ArticleTagDTO articleTagDTO) {
        if (articleTagMapper.updateById(new ArticleTagDO(articleTagDTO)) != 1) {
            throw new BlogException("更新失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteArticleTag(Integer id) {
        if (articleTagMapper.deleteById(id) != 1) {
            throw new BlogException("删除失败");
        }
        return true;
    }
}
