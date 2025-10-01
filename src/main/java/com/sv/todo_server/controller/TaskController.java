package com.sv.todo_server.controller;

import com.sv.todo_server.dto.request.CreateTaskRequest;
import com.sv.todo_server.dto.request.UpdateTaskRequest;
import com.sv.todo_server.exception.TaskNotFoundException;
import com.sv.todo_server.model.Task;
import com.sv.todo_server.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("all")
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<Page<Task>> getPaginatedTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean completed
    ) {
       Pageable pageable = PageRequest.of(page, size);
       if (completed != null) {
           Page<Task> tasks = taskService.getTasksByStatus(completed, pageable);
           return ResponseEntity.ok(tasks);

       } else {
           Page<Task> tasks = taskService.getPaginatedTasks(pageable);
           return ResponseEntity.ok(tasks);

       }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        try {
            Task createdTask = taskService.createTask(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskRequest taskDetails) {
        Optional<Task> updatedTask = taskService.updateTask(id, taskDetails);
        return updatedTask.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try { // а надо ли try catch если я в сервисе делаю throws
            taskService.deleteTask(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @PatchMapping("/toggle-statuses")
    public ResponseEntity<Void> toggleAllStatuses() {
        try {
            taskService.toggleAllStatuses();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete-completed")
    public ResponseEntity<Void> removeAllCompletedTasks() {
        try {
            taskService.removeAllCompletedTasks();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
