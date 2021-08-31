package com.orange_yishenggong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange_yishenggong.wiki.domain.Content;
import com.orange_yishenggong.wiki.domain.Doc;
import com.orange_yishenggong.wiki.domain.DocExample;
import com.orange_yishenggong.wiki.exception.BusinessException;
import com.orange_yishenggong.wiki.exception.BusinessExceptionCode;
import com.orange_yishenggong.wiki.mapper.ContentMapper;
import com.orange_yishenggong.wiki.mapper.DocMapper;
import com.orange_yishenggong.wiki.mapper.DocMapperCust;
import com.orange_yishenggong.wiki.req.DocQueryReq;
import com.orange_yishenggong.wiki.req.DocSaveReq;
import com.orange_yishenggong.wiki.resp.DocQueryResp;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.util.CopyUtil;
import com.orange_yishenggong.wiki.util.RedisUtil;
import com.orange_yishenggong.wiki.util.RequestContext;
import com.orange_yishenggong.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);
    @Resource
    private DocMapper DocMapper;
    @Resource
    private DocMapperCust DocMapperCust;
    @Resource
    private ContentMapper ContentMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WsService wsService;
    @Resource
    private SnowFlake snowFlake;

    public List<DocQueryResp> all(Long ebookId){
        //固定写法
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = DocMapper.selectByExample(docExample);

        //服务层返回
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        return respList;
    }

    public PageResp<DocQueryResp> list(DocQueryReq req){
        //固定写法
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();//相当于where条件,内部类

        //持久层返回
        criteria.andEbookIdEqualTo(req.getEbookId());
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike("%"+req.getName()+"%");
        }

        //分页+查询
        PageHelper.startPage(req.getPage(),req.getSize());
        List<Doc> docList = DocMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数:{}",pageInfo.getTotal());//总行数
        LOG.info("总页数:{}",pageInfo.getPages());//总页数

        //服务层返回
        List<DocQueryResp> respList = CopyUtil.copyList(docList, DocQueryResp.class);
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    public String content(Long id){
        //查询
        Content content = ContentMapper.selectByPrimaryKey(id);
        //文档阅读数+1
        DocMapperCust.incrViewCount(id);
        if(ObjectUtils.isEmpty(content)){
            return "";
        } else {
            return content.getContent();
        }
    }

    @Transactional
    public void save(DocSaveReq req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //新增
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            DocMapper.insert(doc);

            content.setId(doc.getId());
            ContentMapper.insert(content);
        } else{
            //更新
            DocMapper.updateByPrimaryKey(doc);//不带大字段
            int count = ContentMapper.updateByPrimaryKeyWithBLOBs(content);//专门用于大字段的更新方式
            if(count == 0){
                ContentMapper.insert(content);
            }
        }
    }

    public void delete(Long id){
        DocMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<Long> ids){
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();//相当于where条件,内部类
        criteria.andIdIn(ids);
        DocMapper.deleteByExample(docExample);
    }
    //点赞
    public void vote(Long id){
        //文档阅读数+1
        String ip = RequestContext.getRemoteAddr();
        if(redisUtil.validateRepeat("DOC_VOTE"+id+"_"+ip,3600*24)){
            DocMapperCust.incrVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        //推送消息
        Doc docDB = DocMapper.selectByPrimaryKey(id);
        String log_id = MDC.get("LOG_ID");
        wsService.sendInfo("[" + docDB.getName() + "]被点赞！",log_id);
    }

    public void updateEbookInfo(){
        DocMapperCust.updateEbookInfo();
    }
}
