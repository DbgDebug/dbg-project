package club.dbg.cms.upload.service.category;

import club.dbg.cms.upload.service.category.pojo.CategoryDTO;
import club.dbg.cms.upload.service.category.pojo.CategoryListDTO;

public interface ICategoryService {
    CategoryListDTO list(String categoryName, Integer page, Integer pageSize);

    CategoryDTO get(Integer id);

    Boolean add(CategoryDTO category);

    Boolean edit(CategoryDTO category);

    Boolean delete(Integer id);
}