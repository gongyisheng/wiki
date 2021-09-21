package com.orange_yishenggong.wiki.service;

import com.orange_yishenggong.wiki.mapper.EbookSnapshotMapperCust;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EbookSnapshotService {

    @Resource
    private EbookSnapshotMapperCust ebookSnapshotMapperCust;

    public void genSnapshot(){
        ebookSnapshotMapperCust.genSnapshot();
    }

}
