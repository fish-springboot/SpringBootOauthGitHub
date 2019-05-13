package com.github.fish56.oauthgithub.github;

import com.alibaba.fastjson.JSONObject;
import com.github.fish56.oauthgithub.pojo.ActionResponse;
import com.github.fish56.oauthgithub.util.UrlUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Slf4j
@Configuration
public class MyGitHubClient {

    @Value("${github.access_token_url}")
    private String tokenUrl;

    /**
     * 通过Oauth中的code向服务器获取token
     * @return token
     */
    public ActionResponse<String> getTokenFromCode(String code){
        ActionResponse<String> actionResponse = new ActionResponse<>();

        String url = tokenUrl + code;
        log.info("准备获得token：" + url);

        HttpResponse<String> response;
        try{
            response = Unirest.post(url).asString();
        }catch (Exception e) {
            log.error(e.getMessage());
            actionResponse.setErrorMessage(e.getMessage());
            actionResponse.setErrorStatus(500);
            return actionResponse;
        }
        log.info("get response after send code to github: " + response.getBody());

        String token = UrlUtil.queryToMap(response.getBody()).get("access_token");
        if (token == null) {
            log.error("无法获得token：" + response.getBody());
            actionResponse.setErrorMessage(response.getBody());
            actionResponse.setErrorStatus(500);
        }
        log.info("access_token: " + token);
        return actionResponse.setData(token);
    }

    /**
     * 通过token向服务器获取用户信息
     * @return 用户对象
     */
    public ActionResponse<com.github.fish56.oauthgithub.entity.User> getUserFromToken(String token) {
        ActionResponse<com.github.fish56.oauthgithub.entity.User> actionResponse = new ActionResponse<>();

        GitHubClient gitHubClient = new GitHubClient();
        gitHubClient.setOAuth2Token(token);
        UserService userService = new UserService(gitHubClient);
        User user;
        try {
            user = userService.getUser();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("IO异常： " + e.getMessage());
            actionResponse.setErrorMessage(e.getMessage());
            actionResponse.setErrorStatus(500);
            return actionResponse;
        }

        var myUser = new com.github.fish56.oauthgithub.entity.User();
        String userString = JSONObject.toJSONString(user);
        log.info("用户信息是：" + userString);
        myUser = JSONObject.parseObject(userString, com.github.fish56.oauthgithub.entity.User.class);
        myUser.setToken(token);
        actionResponse.setData(myUser);

        return actionResponse;
    }
}
