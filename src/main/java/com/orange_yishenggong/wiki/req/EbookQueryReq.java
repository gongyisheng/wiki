package com.orange_yishenggong.wiki.req;

public class EbookQueryReq extends PageReq{
    private Long id;

    private String name;

    private Long categoryId2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EbookQueryReq{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", categoryId2=").append(categoryId2);
        sb.append('}');
        return sb.toString();
    }
}