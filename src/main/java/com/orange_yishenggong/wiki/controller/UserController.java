package com.orange_yishenggong.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.orange_yishenggong.wiki.req.UserLoginReq;
import com.orange_yishenggong.wiki.req.UserQueryReq;
import com.orange_yishenggong.wiki.req.UserResetPasswordReq;
import com.orange_yishenggong.wiki.req.UserSaveReq;
import com.orange_yishenggong.wiki.resp.CommonResp;
import com.orange_yishenggong.wiki.resp.PageResp;
import com.orange_yishenggong.wiki.resp.UserLoginResp;
import com.orange_yishenggong.wiki.resp.UserQueryResp;
import com.orange_yishenggong.wiki.service.UserService;
import com.orange_yishenggong.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService UserService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowFlake snowFlake;

    private static final Logger Log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/list")
    public CommonResp list(@Valid UserQueryReq req){
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = UserService.list(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        UserService.save(req);
        return resp;
    }

    @PostMapping("/resetPassword")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>();
        UserService.resetPassword(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id){
        CommonResp resp = new CommonResp<>();
        UserService.delete(id);
        return resp;
    }

    @PostMapping("/login")
    public CommonResp login(@Valid @RequestBody UserLoginReq req){
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = UserService.login(req);

        Long token = snowFlake.nextId();
        Log.info("生成单点登录token:{},并放入redis",token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token){
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        Log.info("从redis中删除token:{}",token);
        return resp;
    }

}