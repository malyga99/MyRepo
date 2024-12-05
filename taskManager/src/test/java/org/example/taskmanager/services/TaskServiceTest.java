package org.example.taskmanager.services;

import org.example.taskmanager.entities.Task;
import org.example.taskmanager.exceptions.EntityNotFoundException;
import org.example.taskmanager.exceptions.StatusNotFoundException;
import org.example.taskmanager.repositories.TaskRepository;
import org.example.taskmanager.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    private void assertFieldsHaveNotChange(Task task, Long id, String title, String description, String status) {
        assertEquals(id, task.getId());
        assertEquals(title, task.getTitle());
        assertEquals(description, task.getDescription());
        assertEquals(status, task.getStatus());
    }

    @Test
    public void TaskService_FindAll_ReturnTwoTasks_IfExist() {
        Task task1 = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Second task")
                .description("Description about second task")
                .status("In progress")
                .build();

        Pageable mockPageable = PageRequest.of(0, 3);
        List<Task> mockList = List.of(task1, task2);
        Page<Task> tasks = new PageImpl<>(mockList, mockPageable, mockList.size());
        when(taskRepository.findAll(mockPageable)).thenReturn(tasks);

        Page<Task> pageTasks = taskService.findAll(mockPageable);

        assertEquals(2, pageTasks.getContent().size());

        assertEquals(2, pageTasks.getTotalElements());
        assertEquals(1, pageTasks.getTotalPages());

        assertFieldsHaveNotChange(pageTasks.getContent().get(0), 1L, "First task", "Description about first task", "In progress");
        assertFieldsHaveNotChange(pageTasks.getContent().get(1), 2L, "Second task", "Description about second task", "In progress");

        verify(taskRepository, only()).findAll(mockPageable);

    }

    @Test
    public void TaskService_FindAll_ReturnEmptyPage_IfNotExist() {
        Pageable mockPageable = PageRequest.of(0, 3);
        List<Task> mockList = List.of();
        Page<Task> tasks = new PageImpl<>(mockList, mockPageable, mockList.size());

        when(taskRepository.findAll(mockPageable)).thenReturn(tasks);

        Page<Task> pageTasks = taskService.findAll(mockPageable);

        assertEquals(0, pageTasks.getContent().size());

        assertEquals(0, pageTasks.getTotalElements());
        assertEquals(0, pageTasks.getTotalPages());

        verify(taskRepository, only()).findAll(mockPageable);
    }

    @Test
    public void TaskService_FindById_ReturnOneTask_IfExist() {
        Task task1 = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));

        Task taskById = taskService.findById(1L);

        assertFieldsHaveNotChange(task1, 1L, "First task", "Description about first task", "In progress");

        verify(taskRepository, only()).findById(1L);

    }

    @Test
    public void TaskService_FindById_ThrowException_IfNotExist() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.findById(1L));
    }

    @Test
    public void TaskService_FindByStatus_ReturnTwoTasks() {
        Task task1 = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Second task")
                .description("Description about second task")
                .status("In progress")
                .build();

        Pageable mockPageable = PageRequest.of(0, 3);
        List<Task> mockList = List.of(task1, task2);
        Page<Task> tasks = new PageImpl<>(mockList, mockPageable, mockList.size());

        when(taskRepository.findByStatus("In progress", mockPageable)).thenReturn(tasks);

        Page<Task> tasksByStatus = taskService.findByStatus("In progress", mockPageable);

        assertEquals(2, tasks.getContent().size());

        assertEquals(2, tasksByStatus.getTotalElements());
        assertEquals(1, tasksByStatus.getTotalPages());

        assertFieldsHaveNotChange(tasksByStatus.getContent().get(0), 1L, "First task", "Description about first task", "In progress");
        assertFieldsHaveNotChange(tasksByStatus.getContent().get(1), 2L, "Second task", "Description about second task", "In progress");

        verify(taskRepository, only()).findByStatus("In progress", mockPageable);
    }


    @Test
    public void TaskService_Create_ReturnCreatedTask() {
        Task task1 = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        when(taskRepository.save(task1)).thenReturn(task1);

        ArgumentCaptor<Task> captorTask = ArgumentCaptor.forClass(Task.class);

        taskService.create(task1);

        verify(taskRepository, only()).save(captorTask.capture());

        Task task = captorTask.getValue();


        assertFieldsHaveNotChange(task, 1L, "First task", "Description about first task", "In progress");


    }

    @Test
    public void TaskService_Delete_DeleteTask() {
        Task task1 = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));


        ArgumentCaptor<Task> captorTask = ArgumentCaptor.forClass(Task.class);

        taskService.delete(1L);
        verify(taskRepository, times(1)).delete(captorTask.capture());
        Task task = captorTask.getValue();

        assertFieldsHaveNotChange(task, 1L, "First task", "Description about first task", "In progress");


    }

    @Test
    public void TaskService_Update_UpdateTaskWithThreeFields() {
        Task taskToUpdate = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        Task taskFromRequest = Task.builder()
                .title("Second task")
                .description("Description about second task")
                .status("Finished")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));

        taskService.update(1L, taskFromRequest);

        assertFieldsHaveNotChange(taskToUpdate, 1L, "Second task", "Description about second task", "Finished");

        verify(taskRepository, never()).save(any(Task.class));


    }

    @Test
    public void TaskService_Update_UpdateTaskWithTwoFields() {
        Task taskToUpdate = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        Task taskFromRequest = Task.builder()
                .description("Description about second task")
                .status("Finished")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));

        taskService.update(1L, taskFromRequest);

        assertFieldsHaveNotChange(taskToUpdate, 1L, "First task", "Description about second task", "Finished");

        verify(taskRepository, never()).save(any(Task.class));


    }

    @Test
    public void TaskService_Update_UpdateTaskWithOneField() {
        Task taskToUpdate = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        Task taskFromRequest = Task.builder()
                .description("Description about second task")
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));

        taskService.update(1L, taskFromRequest);

        assertFieldsHaveNotChange(taskToUpdate, 1L, "First task", "Description about second task", "In progress");

        verify(taskRepository, never()).save(any(Task.class));


    }

    @Test
    public void TaskService_Update_UpdateTaskWithoutFields() {
        Task taskToUpdate = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();

        Task taskFromRequest = Task.builder()
                .build();

        when(taskRepository.findById(1L)).thenReturn(Optional.of(taskToUpdate));

        taskService.update(1L, taskFromRequest);

        assertFieldsHaveNotChange(taskToUpdate, 1L, "First task", "Description about first task", "In progress");

        verify(taskRepository, never()).save(any(Task.class));


    }

}