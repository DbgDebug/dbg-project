package club.dbg.cms.blog.domain;

import club.dbg.cms.blog.service.category.pojo.CategoryDTO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName(value = "Category")
public class CategoryDO {

    public CategoryDO() {}

    public CategoryDO(CategoryDTO category) {
        this.id = category.getId();
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.createTime = category.getCreateTime();
        this.updateTime = category.getUpdateTime();
        this.status = category.getStatus();
    }

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(value = "CategoryId")
    private String categoryId;

    @TableField(value = "CategoryName")
    private String categoryName;

    @TableField(value = "CreateTime")
    private Long createTime;

    @TableField(value = "UpdateTime")
    private Long updateTime;

    @TableField(value = "Status")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
