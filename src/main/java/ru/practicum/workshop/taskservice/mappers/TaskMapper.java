package ru.practicum.workshop.taskservice.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.workshop.taskservice.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.dto.UpdateTaskDto;
import ru.practicum.workshop.taskservice.enums.TaskStatus;
import ru.practicum.workshop.taskservice.model.Task;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    FullTaskDto toFullTaskDto(Task task);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDateTime", expression = "java(java.time.LocalDateTime.now())")
    Task toTask(int authorId, TaskStatus status, NewTaskDto newTaskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "status", expression = "java(updateTaskDto.getStatus() != null ? TaskStatus.from(updateTaskDto.getStatus())\n" +
            "                    .orElseThrow(() -> new IllegalArgumentException(\"Unknown state: \" + updateTaskDto.getStatus())) : task.getStatus())")
    Task toUpdatedTask(UpdateTaskDto updateTaskDto, @MappingTarget Task task);
}
