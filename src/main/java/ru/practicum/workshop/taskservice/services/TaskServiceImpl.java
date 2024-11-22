package ru.practicum.workshop.taskservice.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.taskservice.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.enums.TaskStatus;
import ru.practicum.workshop.taskservice.mappers.TaskMapper;
import ru.practicum.workshop.taskservice.model.Task;
import ru.practicum.workshop.taskservice.repositories.TaskRepository;
import ru.practicum.workshop.taskservice.searchparams.PresentationParameters;
import ru.practicum.workshop.taskservice.searchparams.SearchParameters;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public FullTaskDto createTask(int authorId, NewTaskDto newTaskDto) {
        return taskMapper.toFullTaskDto(taskRepository.save(taskMapper.toTask(authorId, TaskStatus.NEW, newTaskDto)));
    }

    @Override
    @Transactional
    public FullTaskDto updateTask(int userId, int taskId, UpdateTaskDto updateTaskDto) {
        Task updatingTask = taskRepository.getReferenceById(taskId);
        if (updatingTask.getAuthorId() != userId && updatingTask.getAssigneeId() != userId) {
            throw new IllegalArgumentException("Обновлять задачу могут только автор или исполнитель");
        }
        Task updatedTask = taskMapper.toUpdatedTask(updateTaskDto, updatingTask);
        return taskMapper.toFullTaskDto(taskRepository.save(updatedTask));
    }

    @Override
    @Transactional(readOnly = true)
    public FullTaskDto getTaskById(int taskId) {
        return taskMapper.toFullTaskDto(taskRepository.getReferenceById(taskId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FullTaskDto> getTasks(SearchParameters searchParameters,
                                      PresentationParameters presentationParameters) {
        Pageable pageable = PageRequest.of(presentationParameters.getPage() / presentationParameters.getSize(),
                presentationParameters.getSize());
        Integer eventId = null;
        if (searchParameters.getEventId() != null) {
            eventId = searchParameters.getEventId();
        }
        Integer assigneeId = null;
        if (searchParameters.getAssigneeId() != null) {
            assigneeId = searchParameters.getAssigneeId();
        }
        Integer authorId = null;
        if (searchParameters.getAuthorId() != null) {
            authorId = searchParameters.getAuthorId();
        }
        Page<Task> tasks;
        tasks = taskRepository.findByParameters(eventId, assigneeId, authorId, pageable);
        return tasks
                .stream()
                .map(taskMapper::toFullTaskDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTask(int authorId, int taskId) {
        Task task = taskRepository.getReferenceById(taskId);
        if (task.getAuthorId() != authorId) {
            throw new IllegalArgumentException("Удалить задачу может только её автор.");
        }
        taskRepository.delete(task);
    }
}
