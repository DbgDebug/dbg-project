package club.dbg.cms.upload.service.article.pojo;

import club.dbg.cms.upload.domain.ArticleDO;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ArticleDTO {
    public ArticleDTO() {
    }

    public ArticleDTO(ArticleDO article, List<ArticleCategoryDTO> categories) {
        this.id  = article.getId();
        this.author = article.getAuthor();
        this.title = article.getTitle();
        this.markdown = article.getMarkdown();
        this.status = article.getStatus();
        this.updateTime = article.getUpdateTime();
        this.createTime = article.getCreateTime();
        this.categories = categories;
    }

    public static ArticleDTO build(String content){
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setContent(content);
        return articleDTO;
    }

    private Integer id;

    @Length(min = 1, max = 20)
    private String author;

    @Length(min = 1, max = 30)
    private String title;

    @NotNull
    private List<ArticleCategoryDTO> categories;

    @NotNull
    private String content;

    @NotNull
    private String markdown;

    @Range(max = 1000)
    private Integer status;

    private Long createTime;

    private Long updateTime;

    private List<ArticleTagDTO> tags;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ArticleCategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<ArticleCategoryDTO> categories) {
        this.categories = categories;
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

    public List<ArticleTagDTO> getTags() {
        return tags;
    }

    public void setTags(List<ArticleTagDTO> tags) {
        this.tags = tags;
    }
}
