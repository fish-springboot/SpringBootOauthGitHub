package com.github.fish56.oauthgithub.config;

import com.github.fish56.axois.Axios;
import com.github.fish56.axois.AxiosOkHttp;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public OkHttpClient okHttpClient(){
        OkHttpClient okHttpClient = new OkHttpClient();
        return okHttpClient;
    }
    @Bean
    public Axios axios(){
        Axios axios = new AxiosOkHttp();
        return axios;
    }
}
