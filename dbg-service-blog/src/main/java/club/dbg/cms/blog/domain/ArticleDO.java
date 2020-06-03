package club.dbg.cms.blog.domain;

import club.dbg.cms.blog.service.article.pojo.ArticleDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "Article")
public class ArticleDO {
    public ArticleDO() {

    }

    public ArticleDO(ArticleDTO article) {
        this.id = article.getId();
        this.author = article.getAuthor();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.markdown = article.getMarkdown();
        this.status = article.getStatus();
        this.createTime = article.getCreateTime();
        this.updateTime = article.getUpdateTime();
    }

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "Author")
    private String author;

    @TableField(value = "Title")
    private String title;

    @TableField(value = "Content")
    private String content;

    @TableField(value = "Markdown")
    private String markdown;

    @TableField(value = "Status")
    private Integer status;

    @TableField(value = "CreateTime")
    private Long createTime;

    @TableField(value = "UpdateTime")
    private Long updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
