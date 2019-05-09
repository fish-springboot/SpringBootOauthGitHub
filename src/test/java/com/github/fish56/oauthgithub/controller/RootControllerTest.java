package com.github.fish56.oauthgithub.controller;

import com.github.fish56.oauthgithub.OauthGitHubApplicationTests;
import org.junit.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.Assert.*;

public class RootControllerTest extends OauthGitHubApplicationTests {

    @Test
    public void root() throws Exception{

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/");
        mockMvc.perform(builder)
                .andDo(MockMvcResultHandlers.print());
    }
}