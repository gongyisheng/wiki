package com.orange_yishenggong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange_yishenggong.wiki.domain.Category;
import com.orange_yishenggong.wiki.domain.CategoryExample;
import com.orange_yishenggong.wiki.mapper.CategoryMapper;
import com.orange_yishenggong.wiki.req.CategoryQueryReq;
import com.orange_yishenggong.wiki.req.CategorySaveReq;
import com.orange_yishenggong.wiki.resp.CategoryQueryResp;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.util.CopyUtil;
import com.orange_yishenggong.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryService {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);
    @Resource
    private CategoryMapper CategoryMapper;
    @Resource
    private SnowFlake snowFlake;

    public List<CategoryQueryResp> all(){
        //固定写法
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = CategoryMapper.selectByExample(categoryExample);

        //服务层返回
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return respList;
    }

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){
        //固定写法
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();//相当于where条件,内部类

        //持久层返回
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }

        //分页+查询
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Category> categoryList = CategoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数:{}",pageInfo.getTotal());//总行数
        LOG.info("总页数:{}",pageInfo.getPages());//总页数

        //服务层返回
        List<CategoryQueryResp> respList = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    public void save(CategorySaveReq req){
        Category category = CopyUtil.copy(req, Category.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            category.setId(snowFlake.nextId());
            CategoryMapper.insert(category);
        } else{
            //更新
            CategoryMapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id){
        CategoryMapper.deleteByPrimaryKey(id);
    }
}
