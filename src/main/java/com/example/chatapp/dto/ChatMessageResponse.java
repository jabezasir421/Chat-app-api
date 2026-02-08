package com.example.chatapp.dto;

import java.time.Instant;

public class ChatMessageResponse {
  private Long id;
  private String senderId;
  private String content;
  private Instant createdAt;

  public ChatMessageResponse(Long id, String senderId, String content, Instant createdAt) {
    this.id = id;
    this.senderId = senderId;
    this.content = content;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public String getSenderId() {
    return senderId;
  }

  public String getContent() {
    return content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}