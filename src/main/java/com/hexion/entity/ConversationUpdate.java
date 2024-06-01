package com.hexion.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 会话更新实体类，描述了会话成员变化的信息。
 */
public class ConversationUpdate {
    @JsonProperty("id") // JSON字段名为"id"
    private String id; // 会话ID

    @JsonProperty("membersAdded") // JSON字段名为"membersAdded"
    private List<Member> membersAdded; // 已添加到会话的成员列表

}