package club.dbg.cms.blog.domain;

import club.dbg.cms.blog.service.article.pojo.ArticleTagDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "ArticleTag")
public class ArticleTagDO {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "ArticleId")
    private Integer articleId;

    @TableField(value = "Tag")
    private String tag;

    public ArticleTagDO() {

    }

    public ArticleTagDO(ArticleTagDTO articleTagDTO) {
        this.id = articleTagDTO.getId();
        this.articleId = articleTagDTO.getArticleId();
        this.tag = articleTagDTO.getTag();
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
