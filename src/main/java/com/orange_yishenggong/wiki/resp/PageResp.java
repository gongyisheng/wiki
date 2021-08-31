package com.orange_yishenggong.wiki.resp;

import java.util.List;

public class PageResp<T> {
    private long total;

    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageResp{");
        sb.append("total=").append(total);
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }
}