package com.github.fish56.oauthgithub.github;

import com.github.fish56.oauthgithub.entity.User;

public interface MyGitHubClientService {
    /**
     * 通过Oauth中的code向服务器获取token
     * @return token
     */
    public String getTokenFromCode();

    /**
     * 通过token向服务器获取用户信息
     * @return 用户对象
     */
    public User getUserFromToken();
}
