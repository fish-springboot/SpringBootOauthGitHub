package com.github.fish56.oauthgithub.github;

import com.github.fish56.oauthgithub.OauthGitHubApplicationTests;
import com.github.fish56.oauthgithub.entity.User;
import com.github.fish56.oauthgithub.pojo.ActionResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.Assert.*;

public class MyGitHubClientTest extends OauthGitHubApplicationTests {
    @Autowired
    private MyGitHubClient myGitHubClient;

    @Test
    public void getUserFromToken() throws IOException {
        String token = "f99f4531f28852340a38b70f9338d8aec21c2961";
        ActionResponse<User> actionResponse = myGitHubClient.getUserFromToken(token);
        System.out.println(actionResponse.getData());
    }

    @Test
    public void getTokenFromCode() {
    }

    @Test
    public void getUserFromToken1() {
    }
}