package club.dbg.cms.blog.service.article.pojo;

import club.dbg.cms.blog.domain.ArticleCategoryDO;

public class ArticleCategoryDTO {
    private Integer id;

    private Integer articleId;

    private String categoryId;

    public ArticleCategoryDTO() {

    }

    public ArticleCategoryDTO(Integer articleId, String categoryId) {
        this.articleId = articleId;
        this.categoryId = categoryId;
    }

    public ArticleCategoryDTO(ArticleCategoryDO articleCategoryDO) {
        this.articleId = articleCategoryDO.getArticleId();
        this.categoryId = articleCategoryDO.getCategoryId();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
