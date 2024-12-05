package org.example.taskmanager.exceptions.handlers;

import org.example.taskmanager.exceptions.EntityNotFoundException;
import org.example.taskmanager.exceptions.ResponseError;
import org.example.taskmanager.exceptions.StatusNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class TaskHandler {

    /**
     * Обрабатывает исключение EntityNotFoundException.
     * Возвращает ответ с кодом 404 (Not Found) и сообщением об ошибке
     *
     * @param ex исключение, содержащее информацию о несуществующей сущности
     * @return Ответ с ошибкой, код 404
     */
    @ExceptionHandler
    public ResponseEntity<ResponseError> EntityNotFoundExceptionHandler(EntityNotFoundException ex) {
        // Формируем объект ответа с ошибкой, включая сообщение, код ошибки и время
        ResponseError responseError = ResponseError.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    /**
     * Обрабатывает исключение MethodArgumentNotValidException.
     * Это исключение возникает при нарушении валидации данных, переданных в запросе.
     * Возвращает ответ с кодом 400 (Bad Request) и списком ошибок полей.
     *
     * @param ex исключение, содержащее информацию о нарушении валидации
     * @return Ответ с ошибкой, код 400 и список ошибок валидации
     */
    @ExceptionHandler
    public ResponseEntity<ResponseError> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        // Составляем список всех ошибок валидации по полям
        List<String> listOfFieldErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            listOfFieldErrors.add(fieldError.getDefaultMessage());
        }

        // Объединяем все ошибки в одну строку
        String errors = String.join(",", listOfFieldErrors);
        // Формируем объект ответа с ошибками, включая список ошибок, код ошибки и время
        ResponseError responseError = ResponseError.builder()
                .message(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .time(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

}
