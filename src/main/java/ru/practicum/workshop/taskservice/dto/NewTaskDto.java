package ru.practicum.workshop.taskservice.dto;

import java.time.LocalDateTime;

public class NewTaskDto {
    protected String description;
    protected LocalDateTime deadline;
    protected int assigneeId;
    protected int eventId;
}
