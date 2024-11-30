package ru.practicum.workshop.taskservice.epic.dto;

import lombok.*;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;


import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
public class EpicDto {
    protected Long id;
    protected String name;
    protected Long ownerId;
    protected Long eventId;
    protected LocalDateTime deadline;
    protected Set<FullTaskDto> tasks;
}
