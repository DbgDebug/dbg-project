package club.dbg.cms.blog.controller;

import club.dbg.cms.blog.service.article.IArticleService;
import club.dbg.cms.blog.service.article.IArticleTagService;
import club.dbg.cms.blog.service.article.pojo.ArticleDTO;
import club.dbg.cms.blog.service.article.pojo.ArticleListDTO;
import club.dbg.cms.blog.service.article.pojo.ArticleTagDTO;
import club.dbg.cms.util.ResponseBuild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/article")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final IArticleService articleService;

    private final IArticleTagService articleTagService;

    public ArticleController(IArticleService articleService, IArticleTagService articleTagService) {
        this.articleService = articleService;
        this.articleTagService = articleTagService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, name = "获取文章列表")
    public ResponseBuild<ArticleListDTO> articleList(
            @RequestParam(value = "keyWord", required = false) String keyWord,
            @RequestParam("page") Integer page,
            @RequestParam("pageSize") Integer pageSize) {
        return ResponseBuild.ok(articleService.list(keyWord, page, pageSize));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, name = "获取文章")
    public ResponseBuild<ArticleDTO> getArticle(@PathVariable("id") Integer id) {
        return ResponseBuild.ok(articleService.get(id));
    }

    @RequestMapping(method = RequestMethod.POST, name = "创建文章")
    public ResponseBuild<Integer> addArticle(@RequestBody @Valid ArticleDTO articleDTO) {
        return ResponseBuild.ok(articleService.add(articleDTO));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, name = "更新文章")
    public ResponseBuild<Boolean> editArticle(
            @PathVariable("id") Integer id,
            @RequestBody @Valid ArticleDTO articleDTO) {
        articleDTO.setId(id);
        return ResponseBuild.ok(articleService.edit(articleDTO));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, name = "删除文章")
    public ResponseBuild<Boolean> deleteArticle(@PathVariable("id") Integer id) {
        return ResponseBuild.ok(articleService.delete(id));
    }

    @RequestMapping(value = "/tag/{tag}", method = RequestMethod.GET, name = "获取文章（Tag）")
    public ResponseBuild<ArticleListDTO> getArticleByTag(@PathVariable("tag") String tag) {
        return ResponseBuild.ok(articleService.tag(tag));
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST, name = "创建文章Tag")
    public ResponseBuild<Boolean> createArticleTag(@RequestBody @Valid ArticleTagDTO articleTagDTO) {
        return ResponseBuild.ok(articleTagService.createArticleTag(articleTagDTO));
    }

    @RequestMapping(value = "/tag", method = RequestMethod.PUT, name = "更新文章Tag")
    public ResponseBuild<Boolean> updateArticleTag(@RequestBody @Valid ArticleTagDTO articleTagDTO) {
        return ResponseBuild.ok(articleTagService.updateArticleTag(articleTagDTO));
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.DELETE, name = "删除文章Tag")
    public ResponseBuild<Boolean> deleteArticleTag(@PathVariable("id") Integer id) {
        return ResponseBuild.ok(articleTagService.deleteArticleTag(id));
    }
}
