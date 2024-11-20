package ru.practicum.workshop.taskservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.workshop.taskservice.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.searchparams.PresentationParameters;
import ru.practicum.workshop.taskservice.searchparams.SearchParameters;
import ru.practicum.workshop.taskservice.services.TaskService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TaskController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskControllerIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public TaskService taskService;

    @Test
    void createTask() throws Exception {
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        NewTaskDto newTaskDto = new NewTaskDto("Dsfsdfs dssvdfbfdbdf dfbdfbdf", deadline, 1, 1);
        FullTaskDto fullTaskDto = new FullTaskDto(1, "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now(), deadline, "NEW", 1,1,1);

        when(taskService.createTask(any(Integer.class), any(NewTaskDto.class))).thenReturn(fullTaskDto);

        mockMvc.perform(post("/tasks")
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(newTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(fullTaskDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description", is(fullTaskDto.getDescription())));
    }

    @Test
    void createTask_whenDescriptionInvalid_thenThrowException() throws Exception {
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        NewTaskDto newTaskDto = new NewTaskDto("D", deadline, 1, 1);

        mockMvc.perform(post("/tasks")
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(newTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask() throws Exception {
        UpdateTaskDto updateTaskDto = new UpdateTaskDto("Completely updated first task", null, "IN_PROGRESS", null, null);
        FullTaskDto fullTaskDto = new FullTaskDto(1, "Completely updated first task", LocalDateTime.now(), LocalDateTime.now().plusMonths(1), "IN_PROGRESS", 1,1,1);

        when(taskService.updateTask(any(Integer.class), any(Integer.class), any(UpdateTaskDto.class)))
                .thenReturn(fullTaskDto);

        mockMvc.perform(patch("/tasks/{taskId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(updateTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(fullTaskDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description", is(fullTaskDto.getDescription())))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.status", is(fullTaskDto.getStatus())));
    }

    @Test
    void updateTask_whenTaskIdInvalid_thenThrowException() throws Exception {
        UpdateTaskDto updateTaskDto = new UpdateTaskDto(null, null, "NO_PROGRESS", null, null);

        mockMvc.perform(patch("/tasks/{taskId}", -1)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(updateTaskDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTaskById() throws Exception {
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        FullTaskDto fullTaskDto = new FullTaskDto(1, "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now(), deadline, "NEW", 1,1,1);

        when(taskService.getTaskById(any(Integer.class)))
                .thenReturn(fullTaskDto);

        mockMvc.perform(get("/tasks/{taskId}", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id", is(fullTaskDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.description", is(fullTaskDto.getDescription())));
    }

    @Test
    void getTaskById_whenPageInvalid_thenThrowException() throws Exception {
        mockMvc.perform(get("/tasks/{taskId}", -1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTasks_whenPageInvalid_thenThrowException() throws Exception {
        mockMvc.perform(get("/tasks")
                        .param("page", String.valueOf(-1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTasks_whenSizeInvalid_thenThrowException() throws Exception {
        mockMvc.perform(get("/tasks")
                        .param("size", String.valueOf(-1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getTasks() throws Exception {
        LocalDateTime deadline = LocalDateTime.now().plusMonths(1);
        FullTaskDto fullTaskDto = new FullTaskDto(1, "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now(), deadline, "NEW", 1,1,1);

        when(taskService.getTasks(any(SearchParameters.class), any(PresentationParameters.class)))
                .thenReturn(List.of(fullTaskDto));

        mockMvc.perform(get("/tasks")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id", is(fullTaskDto.getId()), Integer.class))
                .andExpect(jsonPath("$[0].description").exists())
                .andExpect(jsonPath("$[0].description", is(fullTaskDto.getDescription())));
    }

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/{taskId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_whenTaskIdInvalid_thenThrowException() throws Exception {
        mockMvc.perform(delete("/tasks/{taskId}", -1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}