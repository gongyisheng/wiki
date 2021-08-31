package com.orange_yishenggong.wiki.service;

import com.orange_yishenggong.wiki.domain.Demo;
import com.orange_yishenggong.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DemoService {
    @Resource
    private DemoMapper DemoMapper;
    public List<Demo> list(){
        return DemoMapper.selectByExample(null);
    }
}
