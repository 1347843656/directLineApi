package com.hexion.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 令牌响应实体类，对应Direct Line API返回的认证令牌信息。
 */
public class TokenResponse {
    @JsonProperty("token") // JSON字段名为"token"
    private String token; // 访问Direct Line的令牌

    @JsonProperty("expires_in") // JSON字段名为"expires_in"
    private int expiresIn; // 令牌过期时间（单位：秒）

    // 省略getter和setter方法
}


