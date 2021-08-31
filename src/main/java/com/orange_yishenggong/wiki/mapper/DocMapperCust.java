package com.orange_yishenggong.wiki.mapper;

import org.apache.ibatis.annotations.Param;

//Mapper都是用来存接口的
public interface DocMapperCust {
    public void incrViewCount(@Param("id") Long id);
    public void incrVoteCount(@Param("id") Long id);
    public void updateEbookInfo();
}