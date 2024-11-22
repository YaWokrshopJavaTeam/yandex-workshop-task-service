package ru.practicum.workshop.taskservice.tasks.services;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.tasks.searchparams.PresentationParameters;
import ru.practicum.workshop.taskservice.tasks.searchparams.SearchParameters;

import java.util.List;

public interface TaskService {
    @Transactional
    FullTaskDto createTask(long authorId, NewTaskDto newTaskDto);

    @Transactional
    FullTaskDto updateTask(long userId, long taskId, UpdateTaskDto updateTaskDto);

    @Transactional(readOnly = true)
    FullTaskDto getTaskById(long taskId);

    @Transactional(readOnly = true)
    List<FullTaskDto> getTasks(SearchParameters searchParameters,
                               PresentationParameters presentationParameters);

    @Transactional
    void deleteTask(long authorId, long taskId);
}
