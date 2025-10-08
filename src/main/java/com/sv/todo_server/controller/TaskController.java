package com.sv.todo_server.controller;

import com.sv.todo_server.dto.request.CreateTaskRequest;
import com.sv.todo_server.dto.request.UpdateStatusRequest;
import com.sv.todo_server.dto.request.UpdateTitleRequest;
import com.sv.todo_server.model.Task;
import com.sv.todo_server.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/all")

    public List<Task> getAllTasks() {
          return taskService.getAllTasks();
    };

    @GetMapping
    public Page<Task> getPaginatedTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean completed
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return taskService.getPaginatedTasks(completed, pageable);
    }


    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
     return taskService.getTaskById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task createTask(@Valid @RequestBody CreateTaskRequest request) {
           return taskService.createTask(request);
    }

    @PatchMapping("/{id}/title")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTitleRequest request) {
        return taskService.updateTitle(id, request);
    }
    @PatchMapping("/{id}/status")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return taskService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable Long id) {
                taskService.deleteTask(id);
    }

    @PatchMapping("/toggle-statuses")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void toggleAllStatuses() {
        taskService.toggleAllStatuses();
    }

    @DeleteMapping("/delete-completed")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllCompletedTasks() {
        taskService.removeAllCompletedTasks();
    }
}
