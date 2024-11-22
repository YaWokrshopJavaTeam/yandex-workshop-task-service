package ru.practicum.workshop.taskservice.tasks.dto;

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
    protected long id;
    protected String title;
    protected String description;
    protected LocalDateTime createdDateTime;
    protected LocalDateTime deadline;
    protected String status;
    protected long assigneeId;
    protected long authorId;
    protected long eventId;
}
