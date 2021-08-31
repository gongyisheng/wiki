package com.orange_yishenggong.wiki.req;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class DocSaveReq {

    private Long id;

    private Long parent;

    private Long ebookId;

    @NotNull(message = "[名称]不得为空")
    @NotEmpty(message = "[名称]不得为空")
    private String name;

    private Integer sort;

    @NotNull(message = "[viewCount]不得为空")
    @Min(value=0, message = "[viewCount]不得小于0")
    private Integer viewCount;

    @NotNull(message = "[voteCount]不得为空")
    @Min(value=0, message = "[voteCount]不得小于0")
    private Integer voteCount;

    @NotNull(message = "[内容]不得为空")
    @NotEmpty(message = "[内容]不得为空")
    private String content;

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

    public Long getEbookId() {
        return ebookId;
    }

    public void setEbookId(Long ebookId) {
        this.ebookId = ebookId;
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

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DocSaveReq{");
        sb.append("id=").append(id);
        sb.append(", parent=").append(parent);
        sb.append(", ebookId=").append(ebookId);
        sb.append(", name='").append(name).append('\'');
        sb.append(", sort=").append(sort);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", voteCount=").append(voteCount);
        sb.append(", content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
}