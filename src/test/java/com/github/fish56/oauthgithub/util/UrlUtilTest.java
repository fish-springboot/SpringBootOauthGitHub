package com.github.fish56.oauthgithub.util;

import com.alibaba.fastjson.JSONObject;
import com.github.fish56.oauthgithub.OauthGitHubApplicationTests;
import lombok.val;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class UrlUtilTest extends OauthGitHubApplicationTests {

    @Test
    public void queryToMap() {
        String qs = "a=3&b=4";
        val map = UrlUtil.queryToMap(qs);
        System.out.println(JSONObject.toJSONString(map));
        for (String key : map.keySet()){
            assertEquals("3", map.get("a"));
            assertEquals("4", map.get("b"));
        }
    }
    @Test
    public void queryToMap2() {
        String qs = "access_token=ce4541fe0b10ebd894b247cafdf00b4905c40919&scope=&token_type=bearer";
        val map = UrlUtil.queryToMap(qs);
        System.out.println(JSONObject.toJSONString(map));
        // {"access_token":"ce4541fe0b10ebd894b247cafdf00b4905c40919","scope":"","token_type":"bearer"}
        for (String key : map.keySet()){

        }
    }
}