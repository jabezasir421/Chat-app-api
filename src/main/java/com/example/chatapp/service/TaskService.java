package com.example.chatapp.service;

import com.example.chatapp.dto.TaskCreateRequest;
import com.example.chatapp.dto.TaskResponse;
import com.example.chatapp.dto.TaskUpdateRequest;
import com.example.chatapp.model.Task;
import com.example.chatapp.model.TaskStatus;
import com.example.chatapp.repo.TaskRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Transactional
  public TaskResponse createTask(String userId, TaskCreateRequest request) {
    Task task = new Task(userId, request.getTitle(), request.getDescription(), request.getCategory());
    Instant now = Instant.now();
    task.setCreatedAt(now);
    task.setUpdatedAt(now);
    Task saved = taskRepository.save(task);
    return toResponse(saved);
  }

  @Transactional(readOnly = true)
  public List<TaskResponse> listTasks(String userId, TaskStatus status, String category) {
    List<Task> tasks;
    if (status != null && category != null && !category.isBlank()) {
      tasks = taskRepository.findByUserIdAndStatusAndCategoryOrderByCreatedAtDesc(
          userId, status, category);
    } else if (status != null) {
      tasks = taskRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status);
    } else if (category != null && !category.isBlank()) {
      tasks = taskRepository.findByUserIdAndCategoryOrderByCreatedAtDesc(userId, category);
    } else {
      tasks = taskRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    return tasks.stream().map(this::toResponse).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public Optional<TaskResponse> getTask(String userId, Long id) {
    return taskRepository.findById(id)
        .filter(task -> task.getUserId().equals(userId))
        .map(this::toResponse);
  }

  @Transactional
  public Optional<TaskResponse> updateTask(String userId, Long id, TaskUpdateRequest request) {
    Optional<Task> existingOpt = taskRepository.findById(id)
        .filter(task -> task.getUserId().equals(userId));
    if (existingOpt.isEmpty()) {
      return Optional.empty();
    }
    Task task = existingOpt.get();
    if (request.getTitle() != null) {
      task.setTitle(request.getTitle());
    }
    if (request.getDescription() != null) {
      task.setDescription(request.getDescription());
    }
    if (request.getCategory() != null) {
      task.setCategory(request.getCategory());
    }
    if (request.getStatus() != null) {
      task.setStatus(request.getStatus());
    }
    task.setUpdatedAt(Instant.now());
    Task saved = taskRepository.save(task);
    return Optional.of(toResponse(saved));
  }

  @Transactional
  public boolean deleteTask(String userId, Long id) {
    Optional<Task> existingOpt = taskRepository.findById(id)
        .filter(task -> task.getUserId().equals(userId));
    if (existingOpt.isEmpty()) {
      return false;
    }
    taskRepository.delete(existingOpt.get());
    return true;
  }

  private TaskResponse toResponse(Task task) {
    return new TaskResponse(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getCategory(),
        task.getStatus(),
        task.getCreatedAt(),
        task.getUpdatedAt());
  }
}