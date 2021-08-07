package club.dbg.cms.upload.service.article;

import club.dbg.cms.upload.dao.ArticleCategoryMapper;
import club.dbg.cms.upload.dao.ArticleMapper;
import club.dbg.cms.upload.domain.ArticleCategoryDO;
import club.dbg.cms.upload.domain.ArticleDO;
import club.dbg.cms.upload.exception.BlogException;
import club.dbg.cms.upload.service.article.pojo.ArticleCategoryDTO;
import club.dbg.cms.upload.service.article.pojo.ArticleDTO;
import club.dbg.cms.upload.service.article.pojo.ArticleListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService implements IArticleService {
    public static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final DataSourceTransactionManager transactionManager;

    private final ArticleMapper articleMapper;

    private final ArticleCategoryMapper articleCategoryMapper;

    public ArticleService(DataSourceTransactionManager transactionManager,
                          ArticleMapper articleMapper,
                          ArticleCategoryMapper articleCategoryMapper) {
        this.transactionManager = transactionManager;
        this.articleMapper = articleMapper;
        this.articleCategoryMapper = articleCategoryMapper;
    }

    @Override
    public ArticleListDTO list(String keyWord, Integer page, Integer pageSize) {
        page = (page - 1) * pageSize;
        if (keyWord != null && !keyWord.isEmpty()) {
            keyWord = "%" + keyWord + "%";
        }
        ArticleListDTO articleList = new ArticleListDTO();
        articleList.setArticleList(articleMapper.selectArticleList(keyWord, page, pageSize));
        articleList.setTotal(articleMapper.articleListCount(keyWord));
        return articleList;
    }

    @Override
    public ArticleListDTO tag(String tag) {
        List<ArticleDTO> articleDTOS = articleMapper.selectByTag(tag);
        ArticleListDTO articleListDTO = new ArticleListDTO();
        articleListDTO.setArticleList(articleDTOS);
        return articleListDTO;
    }

    @Override
    @Cacheable(value = "article", key = "#p0")
    public ArticleDTO get(Integer id) {
        ArticleDO articleDO = articleMapper.selectById(id);
        if (articleDO == null) {
            throw new BlogException("文章不存在");
        }
        List<ArticleCategoryDO> articleCategoryDOS = articleCategoryMapper.selectListByArticleId(id);
        List<ArticleCategoryDTO> articleCategoryDTOS = new ArrayList<>();
        for (ArticleCategoryDO categoryDO : articleCategoryDOS) {
            articleCategoryDTOS.add(new ArticleCategoryDTO(categoryDO));
        }
        return new ArticleDTO(articleDO, articleCategoryDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(ArticleDTO articleDTO) {
        articleDTO.setCreateTime(System.currentTimeMillis() / 1000);
        articleDTO.setUpdateTime(articleDTO.getCreateTime());
        ArticleDO articleDO = new ArticleDO(articleDTO);
        if (articleMapper.insert(articleDO) != 1) {
            throw new BlogException("添加失败");
        }
        List<ArticleCategoryDO> articleCategoryDOS = new ArrayList<>();
        for (ArticleCategoryDTO categoryDTO : articleDTO.getCategories()) {
            categoryDTO.setArticleId(articleDO.getId());
            articleCategoryDOS.add(new ArticleCategoryDO(categoryDTO));
        }
        if (articleCategoryMapper.inserts(articleCategoryDOS) != articleCategoryDOS.size()) {
            throw new BlogException("添加失败");
        }
        return articleDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(cacheNames = "article", key = "#articleDTO.id")
    public Boolean edit(ArticleDTO articleDTO) {
        articleDTO.setCreateTime(null);
        articleDTO.setUpdateTime(System.currentTimeMillis() / 1000);
        ArticleDO articleDO = new ArticleDO(articleDTO);
        if (articleMapper.updateById(articleDO) != 1) {
            throw new BlogException("修改失败");
        }
        articleCategoryMapper.deleteByArticleId(articleDTO.getId());
        List<ArticleCategoryDO> articleCategoryDOS = new ArrayList<>();
        for (ArticleCategoryDTO categoryDTO : articleDTO.getCategories()) {
            categoryDTO.setArticleId(articleDO.getId());
            articleCategoryDOS.add(new ArticleCategoryDO(categoryDTO));
        }
        if (articleCategoryMapper.inserts(articleCategoryDOS) != articleCategoryDOS.size()) {
            throw new BlogException("添加失败");
        }
        return true;
    }

    @Override
    public Boolean delete(Integer id) {
        if (articleMapper.deleteById(id) != 1) {
            throw new BlogException("删除失败");
        }
        articleCategoryMapper.deleteByArticleId(id);
        return true;
    }

    private TransactionStatus getTransactionStatus() {
        DefaultTransactionDefinition transDefinition = new DefaultTransactionDefinition();
        transDefinition.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
        return transactionManager.getTransaction(transDefinition);
    }
}
