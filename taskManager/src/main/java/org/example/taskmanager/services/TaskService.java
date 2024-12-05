package org.example.taskmanager.services;

import org.example.taskmanager.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Page<Task> findAll(Pageable pageable);
    Task findById(Long id);
    Page<Task> findByStatus(String status, Pageable pageable);
    void create(Task task);
    void delete(Long id);
    void update(Long id, Task task);


}
