package com.hua.controller;


import com.hua.dto.AccessTokenDTO;
import com.hua.dto.GithubUser;
import com.hua.mapper.UserMapper;
import com.hua.model.User;
import com.hua.provider.GithubProvider;
import com.hua.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response){
        //调用github第三方必须要得
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUrl);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        //如果githubUser不等于空，说明有值，然后加入到数据库中
        if (githubUser!=null&&githubUser.getId()!=null){
            //登录之后获取用户信息
            User user = new User();
            //UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法
            //user.setToken(UUID.randomUUID().toString());
            String token = UUID.randomUUID().toString();
            //自动生成的token放入到user的token里面
            user.setToken(token);
            //把github的名字放入数据库中user表的名字
            user.setName(githubUser.getName());
            //id是long类型，强转一下
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setAvatarUrl(githubUser.getAvatar_url());
            userService.createUpdate(user);
            //把token加入到cookie里面
            //如果没有登录，那么肯定不会有token
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }

}
