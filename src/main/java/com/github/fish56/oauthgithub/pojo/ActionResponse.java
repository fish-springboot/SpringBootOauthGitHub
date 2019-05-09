package com.github.fish56.oauthgithub.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ActionResponse<T> {
    private String errorMessage;

    // 发生错误时，服务器返回的状态码由具体的方法来确定
    private Integer errorStatus;

    // 正常请求的数据
    private T data;

    // 正常时的状态码由Controller层决定

    public boolean isSuccess(){
        return errorMessage == null;
    }
    public boolean hasError(){
        return errorMessage != null;
    }
}
