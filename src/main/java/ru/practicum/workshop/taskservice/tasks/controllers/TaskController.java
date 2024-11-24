package ru.practicum.workshop.taskservice.tasks.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.tasks.searchparams.PresentationParameters;
import ru.practicum.workshop.taskservice.tasks.searchparams.SearchParameters;
import ru.practicum.workshop.taskservice.tasks.services.TaskService;

import java.util.List;

@Validated
@RestController
@RequestMapping(path = "/tasks")
@AllArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FullTaskDto createTask(@RequestHeader("X-Sharer-User-Id") Long authorId,
                                  @Valid @RequestBody NewTaskDto newTaskDto) {
        log.info("Запрос на создание новой задачи от пользователя с id={}", authorId);
        return taskService.createTask(authorId, newTaskDto);
    }

    @PatchMapping("/{taskId}")
    public FullTaskDto updateTask(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable @Positive Long taskId,
                                  @Valid @RequestBody UpdateTaskDto updateTaskDto) {
        log.info("Запрос на обновление задачи с id={} от пользователя с id={}", taskId, userId);
        return taskService.updateTask(userId, taskId, updateTaskDto);
    }

    @GetMapping("/{taskId}")
    public FullTaskDto getTaskById(@PathVariable @Positive Long taskId) {
        log.info("Запрос на получение задачи с id={}", taskId);
        return taskService.getTaskById(taskId);
    }

    @GetMapping
    public List<FullTaskDto> getTasks(@RequestParam(defaultValue = "0") @PositiveOrZero Integer page,
                                      @RequestParam(defaultValue = "10") @Positive Integer size,
                                      @RequestParam(required = false) Long eventId,
                                      @RequestParam(required = false) Long assigneeId,
                                      @RequestParam(required = false) Long authorId) {
        log.info("Запрос на получение задач с необязательными фильтрами");
        SearchParameters searchParameters = new SearchParameters(eventId, assigneeId, authorId);
        PresentationParameters presentationParameters = new PresentationParameters(page, size);
        return taskService.getTasks(searchParameters, presentationParameters);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@RequestHeader("X-Sharer-User-Id") Long authorId,
                           @PathVariable @Positive Long taskId) {
        log.info("Запрос на удаление задачи с id={} от пользователя с id={}", taskId, authorId);
        taskService.deleteTask(authorId, taskId);
    }
}
