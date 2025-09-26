package com.sv.todo_server.repository;

import com.sv.todo_server.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCompletedTrue();
    Page<Task> findAll(Pageable pageable); // этр же можно и удалить?
    Page<Task> findByCompleted(boolean completed, Pageable pageable);

}
