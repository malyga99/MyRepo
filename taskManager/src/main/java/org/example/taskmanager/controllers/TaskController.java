package org.example.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.entities.Task;
import org.example.taskmanager.services.TaskService;
import org.example.taskmanager.validation.OnCreate;
import org.example.taskmanager.validation.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "TaskController")
@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Метод для создания объекта Pageable, который используется для пагинации
     *
     * @param numberPage Номер страницы
     * @param sizePage Размер страницы
     * @return Pageable объект
     */
    private Pageable createPageable(int numberPage, int sizePage) {
        return PageRequest.of(numberPage, sizePage);
    }

    /**
     * Метод для получения всех задач с пагинацией
     *
     * @param numberPage Номер страницы (0 по умолчанию)
     * @param sizePage Размер страницы (3 по умолчанию)
     * @return ResponseEntity с объектом Page, содержащим все задачи на странице
     */
    @Operation(
            summary = "Получить список задач с пагинацией",
            description = "Возвращает страницу задач с возможностью настройки номера страницы и размера страницы",
            tags = {"Tasks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список задач успешно получен"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён"
            )
    })
    @GetMapping
    public ResponseEntity<Page<Task>> findAll(
            @RequestParam(value = "numberPage", defaultValue = "0") int numberPage,
            @RequestParam(value = "sizePage", defaultValue = "3") int sizePage) {
        Pageable pageable = createPageable(numberPage, sizePage);
        return ResponseEntity.ok(taskService.findAll(pageable));
    }


    /**
     * Метод для получения задачи по ID
     *
     * @param id Идентификатор задачи
     * @return ResponseEntity, с найденной задачей по ID
     */
    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает задачу по указанному идентификатору, если она существует",
            tags = {"Tasks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Задача успешно найдена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача с указанным ID не найдена"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён"
            )
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> findById(@PathVariable("taskId") Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    /**
     * Метод для получения задач по статусу с пагинацией
     *
     * @param status Статус задачи
     * @param numberPage Номер страницы (0 по умолчанию)
     * @param sizePage Размер страницы (3 по умолчанию)
     * @return ResponseEntity с объектом Page, содержащим задачи с указанным статусом
     */
    @Operation(
            summary = "Получить задачи по статусу с пагинацией",
            description = "Возвращает страницу задач с указанным статусом, с возможностью настройки номера и размера страницы",
            tags = {"Tasks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Список задач успешно получен"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задачи с указанным статусом не найдены"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён"
            )
    })
    @GetMapping("/status/{taskStatus}")
    public ResponseEntity<Page<Task>> findByStatus(
            @PathVariable("taskStatus") String status,
            @RequestParam(value = "numberPage", defaultValue = "0") int numberPage,
            @RequestParam(value = "sizePage", defaultValue = "3") int sizePage) {

        Pageable pageable = createPageable(numberPage, sizePage);
        return ResponseEntity.ok(taskService.findByStatus(status, pageable));
    }

    /**
     * Метод для создания новой задачи
     *
     * @param task Задача, которая будет создана
     * @return ResponseEntity, с созданной задачей и статусом "Created"
     */
    @Operation(
            summary = "Создаёт новую задачу",
            description = "Создаёт новую задачу с указанными данными и возвращает созданную задачу по её ID",
            tags = {"Tasks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Задача успешно создана"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные для создания задачи"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён"
            )
    })
    @PostMapping
    public ResponseEntity<Task> create(@Validated(OnCreate.class) @RequestBody Task task){
        taskService.create(task);
        return ResponseEntity.created(URI.create("/api/v1/tasks/" + task.getId())).body(task);
    }

    /**
     * Метод для удаления задачи по ID
     *
     * @param id Идентификатор задачи
     * @return ResponseEntity с пустым телом и статусом "No content"
     */
    @Operation(
            summary = "Удаляет задачу",
            description = "Удаляет задачу по указанному ID",
            tags = {"Tasks"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Задача успешно удалена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Такой задачи не существует"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён"
            )
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> delete(@PathVariable("taskId") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Метод для обновления задачи
     *
     * @param id Идентификатор задачи, которую необходимо обновить
     * @param task Новый объект задачи с обновленными данными
     * @return ResponseEntity с пустым телом и статусом "No content"
     */
    @Operation(
            summary = "Обновить существующую задачу",
            description = "Обновляет задачу с указанным ID, заменяя её данные переданными в запросе",
            tags = {"Tasks"}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Задача успешно обновлена"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Задача с указанным ID не найдена"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные для обновления задачи"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не авторизован"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Доступ запрещён"
            )
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<Void> update (
            @PathVariable("taskId") Long id,
            @Validated(OnUpdate.class)
            @RequestBody Task task
    ) {
        taskService.update(id, task);
        return ResponseEntity.noContent().build();
    }
}
