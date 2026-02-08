package com.example.chatapp.dto;

import com.example.chatapp.model.TaskStatus;
import jakarta.validation.constraints.Size;

public class TaskUpdateRequest {
  @Size(max = 255)
  private String title;

  @Size(max = 4000)
  private String description;

  @Size(max = 255)
  private String category;

  private TaskStatus status;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public TaskStatus getStatus() {
    return status;
  }

  public void setStatus(TaskStatus status) {
    this.status = status;
  }
}