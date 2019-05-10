package com.github.fish56.oauthgithub.controller;

import com.github.fish56.oauthgithub.entity.User;
import com.github.fish56.oauthgithub.github.MyGitHubClient;
import com.github.fish56.oauthgithub.pojo.ActionResponse;
import com.github.fish56.oauthgithub.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Configuration
@Slf4j
public class RootController {
    @Value("${github.auth_url}")
    private String authUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyGitHubClient myGitHubClient;

    @RequestMapping("/github/login")
    public Object login(@RequestParam String code, HttpServletResponse response) throws Exception{
        ActionResponse<String> actionResponse = myGitHubClient.getTokenFromCode(code);

        if (actionResponse.hasError()) {
            log.error("获得token时出错了: " + actionResponse.getErrorMessage());
            return ResponseEntity.status(actionResponse.getErrorStatus())
                    .body(actionResponse.getErrorMessage());
        }

        String token = actionResponse.getData();
        log.info("token: " + token);

        ActionResponse<User> userActionResponse = myGitHubClient.getUserFromToken(token);
        if (userActionResponse.hasError()) {
            log.error("通过token获得用户信息时出错了: " + actionResponse.getErrorMessage());
            return ResponseEntity.status(userActionResponse.getErrorStatus())
                    .body(userActionResponse.getErrorMessage());
        }

        // 将用户信息保存到数据库
        userRepository.save(userActionResponse.getData());

        log.info("请求完成");
        Cookie cookie = new Cookie("github_t", token);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(20000);
        cookie.setPath("/"); // 不设置的话默认为当前路由/github，肯定是不行的
        response.addCookie(cookie);

        // 返回数据，并设置token
        return ResponseEntity.status(200)
                .body(userActionResponse.getData());
    }

    @RequestMapping("/**")
    public ResponseEntity root(@CookieValue(required = false) String github_t, HttpServletRequest response){
        // 对于未登录用户，跳转到登录页面
        if (github_t == null) {
            log.info("为携带token的用户来访问了");
            return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                    .header("Location", authUrl).build();
        }
        log.info("token为" + github_t + "尝试登录");

        // 对于携带token的用户，验证token的有效性
        User user = userRepository.findByToken(github_t);
        if (user == null) {
            log.info("登录失败");
            return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT)
                    .header("Location", authUrl).build();
        }

        // 用户合法的话，返回数据库中所有用户的列表
        return ResponseEntity.status(200).body(userRepository.findAll());
    }
}
