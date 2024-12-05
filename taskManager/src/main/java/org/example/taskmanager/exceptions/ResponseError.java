package org.example.taskmanager.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * Класс для представления ошибки в ответах, отправляемых клиенту
 * Содержит информацию о сообщении об ошибке, статусе и времени возникновения ошибки
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ResponseError {

    private String message;
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;

}
