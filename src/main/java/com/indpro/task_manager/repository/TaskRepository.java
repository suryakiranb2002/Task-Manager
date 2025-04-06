package com.indpro.task_manager.repository;

import com.indpro.task_manager.entity.Task;
import com.indpro.task_manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByCategoryAndUser(String category, User user);
    List<Task> findByCompletedAndUser(boolean completed, User user);
    List<Task> findByTitleContainingAndUser(String title, User user);
    List<Task> findByUserAndCompletedFalse(User user);

}
