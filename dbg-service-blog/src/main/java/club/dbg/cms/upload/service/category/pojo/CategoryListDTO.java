package club.dbg.cms.upload.service.category.pojo;

import java.util.List;

public class CategoryListDTO {
    private List<CategoryDTO> categoryList;
    private Integer total;

    public List<CategoryDTO> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
