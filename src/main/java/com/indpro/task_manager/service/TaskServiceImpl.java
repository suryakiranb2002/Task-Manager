package com.indpro.task_manager.service;

import com.indpro.task_manager.entity.Task;
import com.indpro.task_manager.entity.User;
import com.indpro.task_manager.exception.TaskNotFoundException;
import com.indpro.task_manager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements ITaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task createTask(Task task, User user) {
        if (task == null || user == null) {
            throw new IllegalArgumentException("Task and User must not be null");
        }
        task.setCompleted(false);
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask, User user) {
        if (id == null || updatedTask == null || user == null) {
            throw new IllegalArgumentException("Task ID, updatedTask and User must not be null");
        }

        Task task = getTask(id, user);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCategory(updatedTask.getCategory());
        task.setPriority(updatedTask.getPriority());
        task.setCompleted(updatedTask.isCompleted());
        task.setDueDate(updatedTask.getDueDate());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id, User user) {
        if (id == null || user == null) {
            throw new IllegalArgumentException("Task ID and User must not be null");
        }

        Task task = getTask(id, user);
        taskRepository.delete(task);
    }

    @Override
    public Task getTask(Long id, User user) {
        if (id == null || user == null) {
            throw new IllegalArgumentException("Task ID and User must not be null");
        }

        return taskRepository.findById(id)
                .filter(task -> task.getUser().equals(user))
                .orElseThrow(() -> new TaskNotFoundException("Task not found or access denied"));
    }

    @Override
    public List<Task> getAllTasks(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return taskRepository.findByUser(user);
    }

    @Override
    public List<Task> getTasksByCategory(String category, User user) {
        if (!StringUtils.hasText(category) || user == null) {
            throw new IllegalArgumentException("Category must not be empty and User must not be null");
        }

        return taskRepository.findByCategoryAndUser(category, user);
    }

    @Override
    public List<Task> getTasksByCompletion(boolean completed, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return taskRepository.findByCompletedAndUser(completed, user);
    }

    @Override
    public List<Task> searchTasksByTitle(String title, User user) {
        if (!StringUtils.hasText(title) || user == null) {
            throw new IllegalArgumentException("Title must not be empty and User must not be null");
        }

        return taskRepository.findByTitleContainingAndUser(title, user);
    }

    @Override
    public long countAllTasks(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return taskRepository.findByUser(user).size();
    }

    @Override
    public long countCompletedTasks(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return taskRepository.findByCompletedAndUser(true, user).size();
    }

    @Override
    public long countPendingTasks(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        return taskRepository.findByCompletedAndUser(false, user).size();
    }

}
