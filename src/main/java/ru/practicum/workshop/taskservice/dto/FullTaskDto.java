package ru.practicum.workshop.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FullTaskDto {
    protected int id;
    protected String title;
    protected String description;
    protected LocalDateTime createdDateTime;
    protected LocalDateTime deadline;
    protected String status;
    protected int assigneeId;
    protected int authorId;
    protected int eventId;
}
