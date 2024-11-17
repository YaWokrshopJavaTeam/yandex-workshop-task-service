package ru.practicum.workshop.taskservice.mappers;

import ru.practicum.workshop.taskservice.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.enums.TaskStatus;
import ru.practicum.workshop.taskservice.model.Task;

import java.time.LocalDateTime;

public class TaskMapper {
    public static Task toTask(int authorId, NewTaskDto newTaskDto) {
        return new Task(
                0,
                newTaskDto.getDescription() != null ? newTaskDto.getDescription() : "",
                LocalDateTime.now(),
                newTaskDto.getDeadline() != null ? newTaskDto.getDeadline() : LocalDateTime.now().plusYears(100),
                TaskStatus.NEW,
                newTaskDto.getAssigneeId(),
                authorId,
                newTaskDto.getEventId()
        );
    }

    public static FullTaskDto toFullTaskDto(Task task) {
        return new FullTaskDto(
                task.getId(),
                task.getDescription(),
                task.getCreatedDateTime(),
                task.getDeadline(),
                task.getStatus(),
                task.getAssigneeId(),
                task.getAuthorId(),
                task.getEventId()
        );
    }
}
