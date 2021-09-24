package com.orange_yishenggong.wiki.resp;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class StatisticResp {

    @JsonFormat(pattern="MM-dd", timezone = "GMT+8")
    private Date date;

    private Long viewCount;

    private Long voteCount;

    private Long viewIncrease;

    private Long voteIncrease;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getViewCount() {
        return viewCount;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Long voteCount) {
        this.voteCount = voteCount;
    }

    public Long getViewIncrease() {
        return viewIncrease;
    }

    public void setViewIncrease(Long viewIncrease) {
        this.viewIncrease = viewIncrease;
    }

    public Long getVoteIncrease() {
        return voteIncrease;
    }

    public void setVoteIncrease(Long voteIncrease) {
        this.voteIncrease = voteIncrease;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", date=").append(date);
        sb.append(", viewCount=").append(viewCount);
        sb.append(", voteCount=").append(voteCount);
        sb.append(", viewIncrease=").append(viewIncrease);
        sb.append(", voteIncrease=").append(voteIncrease);
        sb.append("]");
        return sb.toString();
    }
}