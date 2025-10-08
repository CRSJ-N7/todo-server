package com.sv.todo_server.service;

import com.sv.todo_server.dto.request.CreateTaskRequest;
import com.sv.todo_server.dto.request.UpdateStatusRequest;
import com.sv.todo_server.dto.request.UpdateTitleRequest;
import com.sv.todo_server.exception.*;
import com.sv.todo_server.model.Task;
import com.sv.todo_server.repository.TaskRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Page<Task> getPaginatedTasks(Boolean completed, Pageable pageable) { // блять да почему Boolean с большой буквы
        if (completed == null) {
            return taskRepository.findAll(pageable);
        } else {
            return taskRepository.findByCompleted(completed, pageable);
        }
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        return taskRepository.save(task);
    }


    public Task updateStatus(Long id, UpdateStatusRequest request) {
       Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
       task.setCompleted(request.isCompleted());
       return taskRepository.save(task);
    };

    public Task updateTitle(Long id, UpdateTitleRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new TaskNotFoundException(id));
        task.setTitle(request.getTitle());
        return taskRepository.save(task);
    };

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
