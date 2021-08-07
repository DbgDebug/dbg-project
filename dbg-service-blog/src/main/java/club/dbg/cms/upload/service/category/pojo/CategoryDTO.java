package club.dbg.cms.upload.service.category.pojo;

import club.dbg.cms.upload.domain.CategoryDO;

public class CategoryDTO {
    public CategoryDTO() {

    }

    public CategoryDTO(CategoryDO category) {
        this.id = category.getId();
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createTime = category.getCreateTime();
        this.updateTime = category.getUpdateTime();
        this.status = category.getStatus();
    }

    private Integer id;

    private String categoryId;

    private String categoryName;

    private Long createTime;

    private Long updateTime;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
