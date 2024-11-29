package ru.practicum.workshop.taskservice.epic.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

import static ru.practicum.workshop.taskservice.util.EpicDtoValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UpdateEpicDto {
    @Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE, message = NAME_SIZE_ERROR_MESSAGE)
    protected String name;
    @Positive(message = OWNER_ID_POSITIVE_ERROR_MESSAGE)
    protected Long ownerId;
    @Future(message = DEADLINE_FUTURE_ERROR_MESSAGE)
    protected LocalDateTime deadline;
}
