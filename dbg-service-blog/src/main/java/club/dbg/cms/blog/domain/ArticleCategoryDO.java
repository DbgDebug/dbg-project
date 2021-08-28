package club.dbg.cms.blog.domain;

import club.dbg.cms.blog.service.article.pojo.ArticleCategoryDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "ArticleCategory")
public class ArticleCategoryDO {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ArticleId")
    private Integer articleId;

    @TableField(value = "categoryId")
    private String categoryId;

    public Integer getId() {
        return id;
    }

    public ArticleCategoryDO() {

    }

    public ArticleCategoryDO(ArticleCategoryDTO articleCategoryDTO) {
        this.articleId = articleCategoryDTO.getArticleId();
        this.categoryId = articleCategoryDTO.getCategoryId();
    }

    public ArticleCategoryDO(Integer articleId, String categoryId) {
        this.articleId = articleId;
        this.categoryId = categoryId;
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
