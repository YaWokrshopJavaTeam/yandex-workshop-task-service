package ru.practicum.workshop.taskservice.tasks.mappers;

import org.mapstruct.*;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.tasks.enums.TaskStatus;
import ru.practicum.workshop.taskservice.tasks.model.Task;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    FullTaskDto toFullTaskDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", expression = "java(java.time.LocalDateTime.now())")
    Task toTask(Long authorId, TaskStatus status, NewTaskDto newTaskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", expression = "java(updateTaskDto.getStatus() != null ? TaskStatus.from(updateTaskDto.getStatus())\n" +
            "                    .orElseThrow(() -> new IllegalArgumentException(\"Unknown state: \" + updateTaskDto.getStatus())) : task.getStatus())")
    Task toUpdatedTask(UpdateTaskDto updateTaskDto, @MappingTarget Task task);
}
