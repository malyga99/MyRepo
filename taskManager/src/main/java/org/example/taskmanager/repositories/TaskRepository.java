package org.example.taskmanager.repositories;

import org.example.taskmanager.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAll(Pageable pageable);

    Page<Task> findByStatus(String status, Pageable pageable);
}
