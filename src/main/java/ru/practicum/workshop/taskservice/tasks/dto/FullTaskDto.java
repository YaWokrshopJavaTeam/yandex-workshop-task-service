package ru.practicum.workshop.taskservice.tasks.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
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
