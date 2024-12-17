package ru.practicum.workshop.taskservice.epic.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;
import ru.practicum.workshop.taskservice.epic.service.EpicService;

import java.util.Set;

import static ru.practicum.workshop.taskservice.util.EpicControllerValidationConstants.*;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/tasks/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EpicDto createEpic(@Valid @RequestBody NewEpicDto dto) {
        log.info("Запрос на создание нового эпика {}", dto);
        return epicService.createEpic(dto);
    }

    @PatchMapping("/{epicId}")
    public EpicDto updateEpic(@RequestHeader("X-Owner-Id")
                                  @Positive(message = POSITIVE_OWNER_ID_MESSAGE)
                                  Long ownerId,
                              @PathVariable
                              @Positive(message = POSITIVE_EPIC_ID_MESSAGE)
                              Long epicId,
                              @Valid @RequestBody UpdateEpicDto dto) {
        log.info("Запрос на обновление эпика с id={} от пользователя с id={}", epicId, ownerId);
        return epicService.updateEpic(epicId, ownerId, dto);
    }

    @PostMapping("/{epicId}")
    public EpicDto addTasksToEpic(@RequestHeader("X-Owner-Id")
                                      @Positive(message = POSITIVE_OWNER_ID_MESSAGE)
                                      Long ownerId,
                                  @PathVariable
                                  @Positive(message = POSITIVE_EPIC_ID_MESSAGE)
                                  Long epicId,
                                  @RequestParam Set<Long> taskIds) {
        log.info("Запрос на добавление задач с id {} в эпик с id={} от пользователя с id={}", taskIds, epicId, ownerId);
        return epicService.addTasks(epicId, ownerId, taskIds);
    }

    @DeleteMapping("/{epicId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEpicById(@RequestHeader("X-Owner-Id")
                                  @Positive(message = POSITIVE_OWNER_ID_MESSAGE)
                                  Long ownerId,
                                  @PathVariable
                                  @Positive(message = POSITIVE_EPIC_ID_MESSAGE)
                                  Long epicId) {
        log.info("Запрос на удаление эпика с id {} от пользователя с id={}", epicId, ownerId);
        epicService.deleteEpicById(epicId, ownerId);
    }

    @GetMapping("/{epicId}")
    public EpicDto getEpicById(@PathVariable
                               @Positive(message = POSITIVE_EPIC_ID_MESSAGE)
                               Long epicId) {
        log.info("Запрос на получение эпика с id {}", epicId);
        return epicService.getEpicById(epicId);
    }
}
