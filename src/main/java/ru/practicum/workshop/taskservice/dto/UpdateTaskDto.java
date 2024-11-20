package ru.practicum.workshop.taskservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    protected String title;
    protected String description;
    protected LocalDateTime deadline;
    protected String status;
    protected Integer assigneeId;
    protected Integer eventId;
}
