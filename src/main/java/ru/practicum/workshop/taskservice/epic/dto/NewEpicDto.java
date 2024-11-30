package ru.practicum.workshop.taskservice.epic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

import static ru.practicum.workshop.taskservice.util.EpicDtoValidationConstants.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewEpicDto {
    @NotBlank(message = NAME_NOT_BLANK_ERROR_MESSAGE)
    @Size(min = NAME_MIN_SIZE, max = NAME_MAX_SIZE, message = NAME_SIZE_ERROR_MESSAGE)
    protected String name;
    @NotNull(message = OWNER_ID_NOT_NULL_ERROR_MESSAGE)
    @Positive(message = OWNER_ID_POSITIVE_ERROR_MESSAGE)
    protected Long ownerId;
    @NotNull(message = EVENT_ID_NOT_NULL_ERROR_MESSAGE)
    @Positive(message = EVENT_ID_POSITIVE_ERROR_MESSAGE)
    protected Long eventId;
    @NotNull(message = DEADLINE_NOT_NULL_ERROR_MESSAGE)
    @Future(message = DEADLINE_FUTURE_ERROR_MESSAGE)
    protected LocalDateTime deadline;
}
