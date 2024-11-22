package ru.practicum.workshop.taskservice.epic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;
import ru.practicum.workshop.taskservice.epic.model.Epic;
import ru.practicum.workshop.taskservice.tasks.mappers.TaskMapper;

@Mapper(uses = {TaskMapper.class}, componentModel = "spring")
public interface EpicMapper {
    @Mapping(target = "id", expression = "java(null)")
    Epic toEntity(NewEpicDto dto);

    EpicDto toDto(Epic epic);

    @Mapping(target = "name", defaultExpression = "java(epic.getName())")
    @Mapping(target = "ownerId", defaultExpression = "java(epic.getOwnerId())")
    @Mapping(target = "deadline", defaultExpression = "java(epic.getDeadline())")
    Epic updateEpic(@MappingTarget Epic epic, UpdateEpicDto dto);
}
