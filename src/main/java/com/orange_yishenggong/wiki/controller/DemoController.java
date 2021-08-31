package com.orange_yishenggong.wiki.controller;

import com.orange_yishenggong.wiki.domain.Demo;
import com.orange_yishenggong.wiki.service.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoService DemoService;

    @GetMapping("/list")
    public List<Demo> list(){
        return DemoService.list();
    }
}
