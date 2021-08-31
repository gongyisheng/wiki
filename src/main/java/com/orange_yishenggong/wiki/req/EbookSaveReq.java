package com.orange_yishenggong.wiki.req;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class EbookSaveReq {
    private Long id;

    @NotNull(message = "[名称]不得为空")
    @NotEmpty(message = "[名称]不得为空")
    @Length(max = 50, message = "[名称]长度不得超过50字")
    private String name;

    private Long category1Id;

    private Long category2Id;

    @Length(max = 1000, message = "[名称]长度不得超过1000字")
    private String description;

    private String cover;

    private Integer docCount;

    private Integer viewCount;

    private Integer voteCount;

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

    public Long getCategory1Id() {
        return category1Id;
    }

    public void setCategory1Id(Long category1Id) {
        this.category1Id = category1Id;
    }

    public Long getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(Long category2Id) {
        this.category2Id = category2Id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getDocCount() {
        return docCount;
    }

    public void setDocCount(Integer docCount) {
        this.docCount = docCount;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EbookSaveReq{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", category1Id=").append(category1Id);
        sb.append(", category2Id=").append(category2Id);
        sb.append(", description='").append(description).append('\'');
        sb.append(", cover='").append(cover).append('\'');
        sb.append(", docCount=").append(docCount);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", voteCount=").append(voteCount);
        sb.append('}');
        return sb.toString();
    }
}