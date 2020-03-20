package com.hua.controller;


import com.hua.dto.AccessTokenDTO;
import com.hua.dto.GithubUser;
import com.hua.mapper.UserMapper;
import com.hua.model.User;
import com.hua.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.url}")
    private String redirectUrl;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser!=null){
            User user = new User();
         // UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            //id是long类型，强转一下
            user.setAccountId(String.valueOf(githubUser.getId()));
            //当前的时间，毫秒数
            user.setGmtCreate(System.currentTimeMillis());
            //获取这个时间
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("user",githubUser);
            //登录成功，写入cookie
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }
}
