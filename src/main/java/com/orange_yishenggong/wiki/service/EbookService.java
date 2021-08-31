package com.orange_yishenggong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange_yishenggong.wiki.domain.Ebook;
import com.orange_yishenggong.wiki.domain.EbookExample;
import com.orange_yishenggong.wiki.mapper.EbookMapper;
import com.orange_yishenggong.wiki.req.EbookQueryReq;
import com.orange_yishenggong.wiki.req.EbookSaveReq;
import com.orange_yishenggong.wiki.resp.EbookQueryResp;
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
public class EbookService {
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);
    @Resource
    private EbookMapper EbookMapper;
    @Resource
    private SnowFlake snowFlake;

    public PageResp<EbookQueryResp> list(EbookQueryReq req){
        //固定写法
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();//相当于where条件,内部类
        //持久层返回
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }
        if(!ObjectUtils.isEmpty(req.getCategoryId2())){
            criteria.andCategory2IdEqualTo(req.getCategoryId2());
        }
        //分页+查询
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Ebook> ebookList = EbookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数:{}",pageInfo.getTotal());//总行数
        LOG.info("总页数:{}",pageInfo.getPages());//总页数

        //服务层返回
        List<EbookQueryResp> respList = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    public void save(EbookSaveReq req){
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            ebook.setId(snowFlake.nextId());
            EbookMapper.insert(ebook);
        } else{
            //更新
            EbookMapper.updateByPrimaryKey(ebook);
        }
    }

    public void delete(Long id){
        EbookMapper.deleteByPrimaryKey(id);
    }
}
