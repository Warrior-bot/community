package com.hua.service;

import com.hua.mapper.UserMapper;
import com.hua.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createUpdate(User user) {
        User dbuser = userMapper.findByAccountId(user.getAccountId());
        //这里是蠢了，明明是没有user信息的时候在插入，有了在更新
        if (dbuser == null) {
            //插入
            //当前的时间，毫秒数
            user.setGmtCreate(System.currentTimeMillis());
            //插入这个时间
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //更新
            dbuser.setGmtModified(System.currentTimeMillis());
            dbuser.setAvatarUrl(user.getAvatarUrl());
            dbuser.setName(user.getName());
            dbuser.setToken(user.getToken());
            userMapper.update(dbuser);
        }
    }
}
