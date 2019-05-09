package com.github.fish56.oauthgithub.github;

import com.github.fish56.axois.Axios;
import com.github.fish56.axois.response.ResponseEntity;
import com.github.fish56.oauthgithub.pojo.ActionResponse;
import com.github.fish56.oauthgithub.util.UrlUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class MyGitHubClient {
    private static Logger log = LoggerFactory.getLogger(MyGitHubClient.class);

    @Value("${github.access_token_url}")
    private String tokenUrl;

    @Autowired
    private Axios axios;

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
            log.warn("IO异常： " + e.getMessage());
            actionResponse.setErrorMessage(e.getMessage());
            actionResponse.setErrorStatus(500);
            return actionResponse;
        }

        var myUser = new com.github.fish56.oauthgithub.entity.User();
        myUser.setLogin(user.getLogin());
        myUser.setToken(token);
        myUser.setEmail(user.getEmail());
        actionResponse.setData(myUser);

        return actionResponse;
    }
}
