package com.orange_yishenggong.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.orange_yishenggong.wiki.domain.User;
import com.orange_yishenggong.wiki.domain.UserExample;
import com.orange_yishenggong.wiki.exception.BusinessException;
import com.orange_yishenggong.wiki.exception.BusinessExceptionCode;
import com.orange_yishenggong.wiki.mapper.UserMapper;
import com.orange_yishenggong.wiki.req.UserLoginReq;
import com.orange_yishenggong.wiki.req.UserQueryReq;
import com.orange_yishenggong.wiki.req.UserResetPasswordReq;
import com.orange_yishenggong.wiki.req.UserSaveReq;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.resp.UserLoginResp;
import com.orange_yishenggong.wiki.resp.UserQueryResp;
import com.orange_yishenggong.wiki.util.CopyUtil;
import com.orange_yishenggong.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    @Resource
    private UserMapper UserMapper;
    @Resource
    private SnowFlake snowFlake;

    public PageResp<UserQueryResp> list(UserQueryReq req){
        //固定写法
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();//相当于where条件,内部类
        //持久层返回
        if(!ObjectUtils.isEmpty(req.getLoginName())){
            criteria.andLoginNameEqualTo(req.getLoginName());
        }
        //分页+查询
        PageHelper.startPage(req.getPage(),req.getSize());
        List<User> userList = UserMapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数:{}",pageInfo.getTotal());//总行数
        LOG.info("总页数:{}",pageInfo.getPages());//总页数

        //服务层返回
        List<UserQueryResp> respList = CopyUtil.copyList(userList, UserQueryResp.class);
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(respList);

        return pageResp;
    }

    public void save(UserSaveReq req){
        User user = CopyUtil.copy(req, User.class);
        if(ObjectUtils.isEmpty(req.getId())){
            //判断用户名有无重复
            if(ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))){
                //新增
                user.setId(snowFlake.nextId());
                UserMapper.insert(user);
            } else {
                //提示用户名重复
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else{
            //更新
            user.setLoginName(null);
            user.setPassword(null);
            UserMapper.updateByPrimaryKeySelective(user);
        }
    }

    public void delete(Long id){
        UserMapper.deleteByPrimaryKey(id);
    }

    public User selectByLoginName(String loginName){
        //固定写法
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();//相当于where条件,内部类
        //持久层返回
        criteria.andLoginNameEqualTo(loginName);
        List<User> userList = UserMapper.selectByExample(userExample);

        if(CollectionUtils.isEmpty(userList)){
            return null;
        } else {
            return userList.get(0);
        }
    }

    public void resetPassword(UserResetPasswordReq req){
        User user = CopyUtil.copy(req, User.class);
        UserMapper.updateByPrimaryKeySelective(user);
    }
    //登录
    public UserLoginResp login(UserLoginReq req){
        User userDB = selectByLoginName(req.getLoginName());
        if (ObjectUtils.isEmpty(userDB)) {
            LOG.info("用户名不存在，{}",req.getLoginName());
            //用户名不存在
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            //比对密码
            if(userDB.getPassword().equals(req.getPassword())){
                UserLoginResp userLoginResp = CopyUtil.copy(userDB,UserLoginResp.class);
                return userLoginResp;
            } else {
                LOG.info("密码输入不正确，用户名：{}，输入密码：{}，数据库密码：{}",req.getLoginName(),req.getPassword(),userDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}
