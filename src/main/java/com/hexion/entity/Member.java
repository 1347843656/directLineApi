package com.hexion.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 成员实体类，代表会话中的一个成员。
 */
public class Member {
    @JsonProperty("id") // JSON字段名为"id"
    private String id; // 成员ID

    @JsonProperty("name") // JSON字段名为"name"
    private String name; // 成员名称

    // 省略getter和setter方法
}