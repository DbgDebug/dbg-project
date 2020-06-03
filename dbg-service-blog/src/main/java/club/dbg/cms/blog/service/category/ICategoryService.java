package club.dbg.cms.blog.service.category;

import club.dbg.cms.blog.service.category.pojo.CategoryDTO;
import club.dbg.cms.blog.service.category.pojo.CategoryListDTO;

import java.util.List;

public interface ICategoryService {
    CategoryListDTO list(String categoryName, Integer page, Integer pageSize);

    CategoryDTO get(Integer id);

    Boolean add(CategoryDTO category);

    Boolean edit(CategoryDTO category);

    Boolean delete(Integer id);
}
