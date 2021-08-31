package com.orange_yishenggong.wiki.controller;

import com.orange_yishenggong.wiki.req.CategoryQueryReq;
import com.orange_yishenggong.wiki.req.CategorySaveReq;
import com.orange_yishenggong.wiki.resp.CategoryQueryResp;
import com.orange_yishenggong.wiki.resp.CommonResp;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService CategoryService;

    @GetMapping("/all")
    public CommonResp all(){
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = CategoryService.all();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req){
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = CategoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req){
        CommonResp resp = new CommonResp<>();
        CategoryService.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp<>();
        CategoryService.delete(id);
        return resp;
    }
}