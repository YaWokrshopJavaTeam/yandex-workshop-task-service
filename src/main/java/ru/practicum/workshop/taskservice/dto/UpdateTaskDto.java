package ru.practicum.workshop.taskservice.dto;

import ru.practicum.workshop.taskservice.enums.TaskStatus;

import java.time.LocalDateTime;

public class UpdateTaskDto {
    protected String description;
    protected LocalDateTime deadline;
    protected TaskStatus status;
    protected int assigneeId;
    protected int eventId;
}
