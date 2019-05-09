package com.github.fish56.oauthgithub.github;

import com.github.fish56.oauthgithub.entity.User;

public interface MyGitHubClientService {
    public String getTokenFromCode();
    public User getUserFromToken();
}
