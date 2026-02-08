package com.example.chatapp.dto;

import com.example.chatapp.model.TaskStatus;
import java.time.Instant;

public class TaskResponse {
  private Long id;
  private String title;
  private String description;
  private String category;
  private TaskStatus status;
  private Instant createdAt;
  private Instant updatedAt;

  public TaskResponse(
      Long id,
      String title,
      String description,
      String category,
      TaskStatus status,
      Instant createdAt,
      Instant updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.category = category;
    this.status = status;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getCategory() {
    return category;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }
}