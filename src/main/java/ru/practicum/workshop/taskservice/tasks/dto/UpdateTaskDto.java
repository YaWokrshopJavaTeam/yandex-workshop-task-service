package ru.practicum.workshop.taskservice.tasks.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
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
    @Size(min = 10, max = 200, message = "Заголовок обновляемой задачи не может быть меньше 10 или больше 200 символов")
    protected String title;
    @Size(min = 10, max = 2000, message = "Описание обновляемой задачи не может быть меньше 10 или больше 2000 символов")
    protected String description;
    @Future(message = "Дедлайн обновляемой задачи не может быть в прошлом")
    protected LocalDateTime deadline;
    protected String status;
    @Positive(message = "Идентификатор исполнителя не может быть меньше или равен нулю")
    protected Long assigneeId;
    @Positive(message = "Идентификатор события не может быть меньше или равен нулю")
    protected Long eventId;
}
