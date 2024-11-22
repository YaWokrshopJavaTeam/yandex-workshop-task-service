package ru.practicum.workshop.taskservice.epic.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;


import java.time.LocalDateTime;
import java.util.Set;

@Value
@Builder
public class EpicDto {
    Long id;
    String name;
    Long ownerId;
    Long eventId;
    LocalDateTime deadline;
    Set<FullTaskDto> tasks;
}
