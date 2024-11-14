package ru.practicum.workshop.taskservice.dto;

import ru.practicum.workshop.taskservice.enums.TaskStatus;

import java.time.LocalDateTime;

public class FullTaskDto {
    protected int id;
    protected String description;
    protected LocalDateTime createdDateTime;
    protected LocalDateTime deadline;
    protected TaskStatus status;
    protected int assigneeId;
    protected int authorId;
    protected int eventId;
}
