package org.example.taskmanager.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.entities.Task;
import org.example.taskmanager.exceptions.EntityNotFoundException;
import org.example.taskmanager.repositories.TaskRepository;
import org.example.taskmanager.services.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;


    /**
     * Получает задачу по ID, если она существует, иначе выбрасывает исключение
     *
     * @param id ID задачи
     * @return Задача
     * @throws EntityNotFoundException Если задача с таким ID не найдена
     */
    private Task getOrThrow(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Задачи с таким айди: " + id + " не существует!"));
    }

    /**
     * Находит все задачи с пагинацией
     *
     * @param pageable Параметры пагинации
     * @return Страница задач
     */
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    /**
     * Находит задачу по ID
     *
     * @param id ID задачи
     * @return Задача
     * @throws EntityNotFoundException Если задача с таким ID не найдена
     */
    public Task findById(Long id) {
        return getOrThrow(id);
    }

    /**
     * Находит все задачи с заданным статусом и пагинацией
     *
     * @param status   Статус задачи
     * @param pageable Параметры пагинации
     * @return Страница задач с заданным статусом
     */
    public Page<Task> findByStatus(String status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    /**
     * Создает новую задачу
     *
     * @param task Объект задачи для создания
     */
    public void create(Task task) {
        taskRepository.save(task);
    }

    /**
     * Удаляет задачу по ID
     *
     * @param id ID задачи
     * @throws EntityNotFoundException Если задача с таким ID не найдена
     */
    public void delete(Long id) {
        Task task = getOrThrow(id);
        taskRepository.delete(task);
    }

    /**
     * Обновляет существующую задачу по ID
     * Обновляются только те поля, которые не равны null
     *
     * @param id ID задачи для обновления
     * @param task Объект задачи с новыми значениями
     * @throws EntityNotFoundException Если задача с таким ID не найдена
     */
    @Transactional
    public void update(Long id, Task task) {
        Task updateTask = getOrThrow(id);

        if (task.getTitle() != null) {
            updateTask.setTitle(task.getTitle());
        }


        if (task.getDescription() != null) {
            updateTask.setDescription(task.getDescription());
        }

        if (task.getStatus() != null) {
            updateTask.setStatus(task.getStatus());
        }
    }
}
