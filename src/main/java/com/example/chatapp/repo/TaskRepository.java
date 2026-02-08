package com.example.chatapp.repo;

import com.example.chatapp.model.Task;
import com.example.chatapp.model.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByUserIdOrderByCreatedAtDesc(String userId);

  List<Task> findByUserIdAndStatusOrderByCreatedAtDesc(String userId, TaskStatus status);

  List<Task> findByUserIdAndCategoryOrderByCreatedAtDesc(String userId, String category);

  List<Task> findByUserIdAndStatusAndCategoryOrderByCreatedAtDesc(
      String userId, TaskStatus status, String category);
}