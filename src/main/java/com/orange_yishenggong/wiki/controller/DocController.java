package com.orange_yishenggong.wiki.controller;

import com.orange_yishenggong.wiki.req.DocQueryReq;
import com.orange_yishenggong.wiki.req.DocSaveReq;
import com.orange_yishenggong.wiki.resp.CommonResp;
import com.orange_yishenggong.wiki.resp.DocQueryResp;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService DocService;

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId){
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = DocService.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid DocQueryReq req){
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = DocService.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/content/{id}")
    public CommonResp content(@PathVariable Long id){
        CommonResp<String> resp = new CommonResp<>();
        String content = DocService.content(id);
        resp.setContent(content);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        CommonResp resp = new CommonResp<>();
        DocService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr){
        CommonResp resp = new CommonResp<>();
        if(idsStr!=null){
            List<String> stringList = Arrays.asList(idsStr.split("&"));
            List<Long> longList = new ArrayList<>();
            for(String str:stringList){
                longList.add(Long.parseLong(str));
            }
            DocService.delete(longList);
        }
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id){
        CommonResp<String> resp = new CommonResp<>();
        DocService.vote(id);
        return resp;
    }
}