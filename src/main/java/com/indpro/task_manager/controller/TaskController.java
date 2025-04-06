package com.indpro.task_manager.controller;

import com.indpro.task_manager.entity.Task;
import com.indpro.task_manager.entity.User;
import com.indpro.task_manager.service.ITaskService;
import com.indpro.task_manager.wrapper.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;

    private User extractUser(@AuthenticationPrincipal Object principal) {
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUserEntity();
        }
        throw new RuntimeException("Invalid user principal");
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.createTask(task, extractUser(principal)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.updateTask(id, task, extractUser(principal)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal Object principal) {
        taskService.deleteTask(id, extractUser(principal));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.getTask(id, extractUser(principal)));
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.getAllTasks(extractUser(principal)));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Task>> getByCategory(@PathVariable String category, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.getTasksByCategory(category, extractUser(principal)));
    }

    @GetMapping("/status")
    public ResponseEntity<List<Task>> getByStatus(@RequestParam boolean completed, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.getTasksByCompletion(completed, extractUser(principal)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchByTitle(@RequestParam String title, @AuthenticationPrincipal Object principal) {
        return ResponseEntity.ok(taskService.searchTasksByTitle(title, extractUser(principal)));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(@AuthenticationPrincipal Object principal) {
        User user = extractUser(principal);
        return ResponseEntity.ok(
                Map.of(
                        "total", taskService.countAllTasks(user),
                        "completed", taskService.countCompletedTasks(user),
                        "pending", taskService.countPendingTasks(user)
                )
        );
    }
}


