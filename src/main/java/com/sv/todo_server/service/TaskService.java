package com.sv.todo_server.service;

import com.sv.todo_server.dto.request.CreateTaskRequest;
import com.sv.todo_server.dto.request.UpdateTaskRequest;
import com.sv.todo_server.exception.*;
import com.sv.todo_server.model.Task;
import com.sv.todo_server.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Page<Task> getPaginatedTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Page<Task> getTasksByStatus(boolean completed, Pageable pageable) {
        return taskRepository.findByCompleted(completed, pageable);
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, UpdateTaskRequest taskDetails) {
        return getTaskById(id).map(existingTask -> {

            if (taskDetails.getTitle() != null) {
                existingTask.setTitle(taskDetails.getTitle());
            }
            if (taskDetails.getCompleted() != null) {
                existingTask.setCompleted(taskDetails.getCompleted());
            }
            return taskRepository.save(existingTask);
        });
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    public void toggleAllStatuses() {
        List<Task> allTasks = getAllTasks();

        boolean newStatus = allTasks.stream()
                .anyMatch(task -> !task.isCompleted());

        allTasks.forEach(task-> {
            task.setCompleted(newStatus);
            taskRepository.save(task);
        });
    }

    public void removeAllCompletedTasks () {
        List<Task> completedTasks = taskRepository.findByCompletedTrue();
        taskRepository.deleteAll(completedTasks);
    }

}
