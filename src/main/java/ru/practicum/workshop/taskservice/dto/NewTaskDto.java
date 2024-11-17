package ru.practicum.workshop.taskservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class NewTaskDto {
    @NotBlank
    @Size(min = 10, max = 2000)
    protected String description;
    @Future
    protected LocalDateTime deadline;
    @Positive
    protected int assigneeId;
    @Positive
    protected int eventId;
}
