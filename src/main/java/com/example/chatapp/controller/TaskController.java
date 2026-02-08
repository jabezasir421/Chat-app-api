package com.example.chatapp.controller;

import com.example.chatapp.dto.TaskCreateRequest;
import com.example.chatapp.dto.TaskResponse;
import com.example.chatapp.dto.TaskUpdateRequest;
import com.example.chatapp.model.TaskStatus;
import com.example.chatapp.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<TaskResponse> createTask(
      @RequestHeader("X-User-Id") String userId,
      @Valid @RequestBody TaskCreateRequest request) {
    validateUserId(userId);
    TaskResponse response = taskService.createTask(userId, request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  public List<TaskResponse> listTasks(
      @RequestHeader("X-User-Id") String userId,
      @RequestParam(name = "status", required = false) TaskStatus status,
      @RequestParam(name = "category", required = false) String category) {
    validateUserId(userId);
    return taskService.listTasks(userId, status, category);
  }

  @GetMapping("/{id}")
  public TaskResponse getTask(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long id) {
    validateUserId(userId);
    return taskService.getTask(userId, id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
  }

  @PutMapping("/{id}")
  public TaskResponse updateTask(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long id,
      @Valid @RequestBody TaskUpdateRequest request) {
    validateUserId(userId);
    return taskService.updateTask(userId, id, request)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(
      @RequestHeader("X-User-Id") String userId,
      @PathVariable Long id) {
    validateUserId(userId);
    boolean deleted = taskService.deleteTask(userId, id);
    if (!deleted) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
    }
    return ResponseEntity.noContent().build();
  }

  private void validateUserId(String userId) {
    if (userId == null || userId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "X-User-Id header required");
    }
  }
}
