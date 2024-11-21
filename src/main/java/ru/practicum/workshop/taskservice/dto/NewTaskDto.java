package ru.practicum.workshop.taskservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Заголовок создаваемой задачи не может быть пустым")
    @Size(min = 10, max = 200, message = "Заголовок создаваемой задачи не может быть меньше 10 или больше 200 символов")
    protected String title;
    @NotBlank(message = "Описание создаваемой задачи не может быть пустым")
    @Size(min = 10, max = 2000, message = "Описание создаваемой задачи не может быть меньше 10 или больше 2000 символов")
    protected String description;
    @NotNull(message = "Данные по времени завершения создаваемой задачи не могут отсутствовать")
    @Future(message = "Дедлайн создаваемой задачи не может быть в прошлом")
    protected LocalDateTime deadline;
    @Positive(message = "Идентификатор исполнителя не может быть меньше или равен нулю")
    protected int assigneeId;
    @Positive(message = "Идентификатор события не может быть меньше или равен нулю")
    protected int eventId;
}
