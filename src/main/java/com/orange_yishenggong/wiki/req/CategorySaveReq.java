package com.orange_yishenggong.wiki.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CategorySaveReq {
    private Long id;

    @NotNull(message = "[父分类]不得为空")
    @Min(value = 0, message = "[父分类]仅接受正数")
    private Long parent;

    @NotNull(message = "[名称]不得为空")
    @NotEmpty(message = "[名称]不得为空")
    private String name;

    @NotNull(message = "[顺序]不得为空")
    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", parent=").append(parent);
        sb.append(", name=").append(name);
        sb.append(", sort=").append(sort);
        sb.append("]");
        return sb.toString();
    }
}