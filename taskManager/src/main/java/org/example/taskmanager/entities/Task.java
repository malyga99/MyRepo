package org.example.taskmanager.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.taskmanager.validation.OnCreate;
import org.example.taskmanager.validation.OnUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "title")
    @NotNull(message = "Заголовок не должен быть пустым!", groups = OnCreate.class)
    @Size(min = 5, max = 255, message = "Минимальный размер заголовка - 5 символов, максимальный - 255", groups = {OnCreate.class, OnUpdate.class})
    private String title;

    @Column(name = "description")
    @NotNull(message = "Описание не должно быть пустым!", groups = OnCreate.class)
    @Size(min = 5, max = 255, message = "Минимальный размер описания - 5 символов, максимальный - 255", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "createdat")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
