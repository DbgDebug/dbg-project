package club.dbg.cms.blog.controller;

import club.dbg.cms.util.ResponseBuild;
import club.dbg.cms.blog.service.category.ICategoryService;
import club.dbg.cms.blog.service.category.pojo.CategoryDTO;
import club.dbg.cms.blog.service.category.pojo.CategoryListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, name = "获取文章分类列表")
    public ResponseEntity<ResponseBuild<CategoryListDTO>> categoryList(
            @RequestParam(value = "categoryName", required = false, defaultValue = "") String categoryName,
            @RequestParam("page")Integer page,
            @RequestParam("pageSize")Integer pageSize) {
        return ResponseBuild.build(categoryService.list(categoryName, page, pageSize));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET, name = "获取文章分类")
    public ResponseEntity<ResponseBuild<CategoryDTO>> getCategory(@PathVariable("id") Integer id) {
        return ResponseBuild.build(categoryService.get(id));
    }

    @RequestMapping(method = RequestMethod.POST, name = "创建文章分类")
    public ResponseEntity<ResponseBuild<Boolean>> addCategory(@RequestBody CategoryDTO category) {
        return ResponseBuild.build(categoryService.add(category));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT, name = "更新文章分类")
    public ResponseEntity<ResponseBuild<Boolean>> editCategory(@PathVariable("id") Integer id, @RequestBody CategoryDTO category) {
        category.setId(id);
        return ResponseBuild.build(categoryService.edit(category));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE, name = "删除文章分类")
    public ResponseEntity<ResponseBuild<Boolean>> deleteCategory(@PathVariable("id") Integer id) {
        return ResponseBuild.build(categoryService.delete(id));
    }
}
