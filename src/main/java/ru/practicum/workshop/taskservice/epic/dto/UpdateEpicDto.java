package ru.practicum.workshop.taskservice.epic.dto;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.time.LocalDateTime;

import static ru.practicum.workshop.taskservice.epic.dto.EpicDtoValidationConstants.*;

@Value
public class UpdateEpicDto {
    @Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE, message = NAME_SIZE_ERROR_MESSAGE)
    String name;
    @Positive(message = OWNER_ID_POSITIVE_ERROR_MESSAGE)
    Long ownerId;
    @Future(message = DEADLINE_FUTURE_ERROR_MESSAGE)
    LocalDateTime deadline;
}
