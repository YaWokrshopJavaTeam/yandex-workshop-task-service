package ru.practicum.workshop.taskservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {
    protected String description;
    protected LocalDateTime deadline;
    protected String status;
    protected Integer assigneeId;
    protected Integer eventId;
}
