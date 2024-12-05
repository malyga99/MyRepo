package org.example.taskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.taskmanager.entities.Task;
import org.example.taskmanager.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;


@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskServiceImpl taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    private Task task;

    private Task task2;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        task = Task.builder()
                .id(1L)
                .title("First task")
                .description("Description about first task")
                .status("In progress")
                .build();
        task2 = Task.builder()
                .id(2L)
                .title("Second task")
                .description("Description about second task")
                .status("In progress")
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void taskController_findById_returnTaskById() throws Exception {

        when(taskService.findById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/v1/tasks/{taskId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("First task"))
                .andExpect(jsonPath("$.description").value("Description about first task"))
                .andExpect(jsonPath("$.status").value("In progress"));

        verify(taskService, only()).findById(1L);
    }

    @Test
    public void taskController_create_createdTask() throws Exception {
        String taskFromJSON = objectMapper.writeValueAsString(task);
        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(taskFromJSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/tasks/" + task.getId()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("First task"))
                .andExpect(jsonPath("$.description").value("Description about first task"))
                .andExpect(jsonPath("$.status").value("In progress"));

        verify(taskService, only()).create(task);
    }

    @Test
    public void taskController_delete_deleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/{taskId}", 1L))
                .andExpect(status().isNoContent());
        verify(taskService, only()).delete(1L);
    }

    @Test
    public void taskController_update_updateTask() throws Exception {
        String taskFromJSON = objectMapper.writeValueAsString(task);
        mockMvc.perform(put("/api/v1/tasks/{taskId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(taskFromJSON))
                .andExpect(status().isNoContent());


        verify(taskService, only()).update(1L, task);


    }

    @Test
    public void taskController_findAll_returnTwoTasks() throws Exception {
        Pageable mockPageable = PageRequest.of(0, 3);
        List<Task> tasks = List.of(task, task2);
        Page<Task> mockPage = new PageImpl<>(tasks, mockPageable, tasks.size());
        when(taskService.findAll(mockPageable)).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/tasks")
                        .param("numberPage", "0")
                        .param("sizePage", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))

                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("First task"))
                .andExpect(jsonPath("$.content[0].description").value("Description about first task"))
                .andExpect(jsonPath("$.content[0].status").value("In progress"))

                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].title").value("Second task"))
                .andExpect(jsonPath("$.content[1].description").value("Description about second task"))
                .andExpect(jsonPath("$.content[1].status").value("In progress"));

    }

    @Test
    public void taskController_findByStatus_returnTasksByStatus() throws Exception {
        Pageable mockPageable = PageRequest.of(0, 3);
        List<Task> tasks = List.of(task, task2);
        Page<Task> mockPage = new PageImpl<>(tasks, mockPageable, tasks.size());

        when(taskService.findByStatus("In progress", mockPageable)).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/tasks/status/{taskStatus}", "In progress")
                        .param("numberPage", "0")
                        .param("sizePage", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))

                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("First task"))
                .andExpect(jsonPath("$.content[0].description").value("Description about first task"))
                .andExpect(jsonPath("$.content[0].status").value("In progress"))

                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.content[1].title").value("Second task"))
                .andExpect(jsonPath("$.content[1].description").value("Description about second task"))
                .andExpect(jsonPath("$.content[1].status").value("In progress"));

        verify(taskService, only()).findByStatus("In progress", mockPageable);

    }



}