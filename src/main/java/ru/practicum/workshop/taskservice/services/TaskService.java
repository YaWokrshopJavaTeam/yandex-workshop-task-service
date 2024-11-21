package ru.practicum.workshop.taskservice.services;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.taskservice.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.searchparams.PresentationParameters;
import ru.practicum.workshop.taskservice.searchparams.SearchParameters;

import java.util.List;

public interface TaskService {
    @Transactional
    FullTaskDto createTask(int authorId, NewTaskDto newTaskDto);

    @Transactional
    FullTaskDto updateTask(int userId, int taskId, UpdateTaskDto updateTaskDto);

    @Transactional(readOnly = true)
    FullTaskDto getTaskById(int taskId);

    @Transactional(readOnly = true)
    List<FullTaskDto> getTasks(SearchParameters searchParameters,
                               PresentationParameters presentationParameters);

    @Transactional
    void deleteTask(int authorId, int taskId);
}
