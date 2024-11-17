package ru.practicum.workshop.taskservice.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTaskDto {
    @Size(min = 10, max = 2000)
    protected String description;
    protected LocalDateTime deadline;
    protected String status;
    protected Integer assigneeId;
    protected Integer eventId;
}
