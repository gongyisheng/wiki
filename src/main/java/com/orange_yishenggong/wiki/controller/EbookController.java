package com.orange_yishenggong.wiki.controller;

import com.orange_yishenggong.wiki.req.EbookQueryReq;
import com.orange_yishenggong.wiki.req.EbookSaveReq;
import com.orange_yishenggong.wiki.resp.CommonResp;
import com.orange_yishenggong.wiki.resp.EbookQueryResp;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.service.EbookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService EbookService;

    @GetMapping("/list")
    public CommonResp list(@Valid EbookQueryReq req){
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = EbookService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req){
        CommonResp resp = new CommonResp<>();
        EbookService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp<>();
        EbookService.delete(id);
        return resp;
    }
}