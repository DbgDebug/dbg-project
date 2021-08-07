package club.dbg.cms.upload.service.article.pojo;

import java.util.List;

public class ArticleListDTO {
    private List<ArticleDTO> articleList;
    private Integer total;

    public List<ArticleDTO> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleDTO> articleList) {
        this.articleList = articleList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
