package com.hua.provider;

import com.alibaba.fastjson.JSON;
import com.hua.dto.AccessTokenDTO;
import com.hua.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){

             MediaType mediaType = MediaType.get("application/json; charset=utf-8");
             OkHttpClient client = new OkHttpClient();
             RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
             Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                //解析出来的access_token=e91f761c275968f24dca5c7d095d8b924533f98a&scope=user&token_type=bearer 拆分开
                String token = string.split("&")[0].split("=")[1];
                return  token;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //一个大坑，  access_token  少打了一个c  然后全为空
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {

        }
        return null;

    }


}
