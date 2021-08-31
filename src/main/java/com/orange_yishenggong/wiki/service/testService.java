package com.orange_yishenggong.wiki.service;

import com.orange_yishenggong.wiki.domain.test;
import com.orange_yishenggong.wiki.mapper.testMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class testService {
    @Resource
    private testMapper testMapper;
    public List<test> list(){
        return testMapper.list();
    }
}
