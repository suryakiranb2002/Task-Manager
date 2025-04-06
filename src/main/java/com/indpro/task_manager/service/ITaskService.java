package com.indpro.task_manager.service;

import com.indpro.task_manager.entity.Task;
import com.indpro.task_manager.entity.User;

import java.util.List;

public interface ITaskService {
    Task createTask(Task task, User user);
    Task updateTask(Long id, Task updatedTask, User user);
    void deleteTask(Long id, User user);
    Task getTask(Long id, User user);
    List<Task> getAllTasks(User user);
    List<Task> getTasksByCategory(String category, User user);
    List<Task> getTasksByCompletion(boolean completed, User user);
    List<Task> searchTasksByTitle(String title, User user);
    long countAllTasks(User user);
    long countCompletedTasks(User user);
    long countPendingTasks(User user);
}
