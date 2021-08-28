package club.dbg.cms.blog.service.category;

import club.dbg.cms.blog.dao.CategoryMapper;
import club.dbg.cms.blog.domain.CategoryDO;
import club.dbg.cms.blog.exception.BlogException;
import club.dbg.cms.blog.service.category.pojo.CategoryDTO;
import club.dbg.cms.blog.service.category.pojo.CategoryListDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService implements ICategoryService {
    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryMapper articleCategoryMapper;

    public CategoryService(CategoryMapper articleCategoryMapper) {
        this.articleCategoryMapper = articleCategoryMapper;
    }

    @Override
    public CategoryListDTO list(String categoryName, Integer page, Integer pageSize) {
        page = (page - 1) * pageSize;
        CategoryListDTO categoryListDTO = new CategoryListDTO();
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        List<CategoryDO> categoryDOS = articleCategoryMapper.selectCategoryList(page, pageSize);
        for (CategoryDO categoryDO : categoryDOS) {
            categoryDTOList.add(new CategoryDTO(categoryDO));
        }
        categoryListDTO.setCategoryList(categoryDTOList);
        categoryListDTO.setTotal(articleCategoryMapper.categoryCount());
        return categoryListDTO;
    }

    @Override
    public CategoryDTO get(Integer id) {
        return new CategoryDTO(articleCategoryMapper.selectById(id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(CategoryDTO category) {
        synchronized (this) {
            if (articleCategoryMapper.selectByCategoryId(category.getCategoryId()) != null) {
                throw new BlogException("分类已存在");
            }
            category.setCreateTime(System.currentTimeMillis() / 1000);
            category.setUpdateTime(category.getCreateTime());
            if (articleCategoryMapper.insert(new CategoryDO(category)) != 1) {
                throw new BlogException("创建文章分类失败");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(CategoryDTO category) {
        category.setCreateTime(null);
        category.setUpdateTime(System.currentTimeMillis() / 1000);
        if (articleCategoryMapper.updateById(new CategoryDO(category)) != 1) {
            throw new BlogException("更新文章分类失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id) {
        if (articleCategoryMapper.deleteById(id) != 1) {
            throw new BlogException("删除文章失败");
        }
        return true;
    }
}
