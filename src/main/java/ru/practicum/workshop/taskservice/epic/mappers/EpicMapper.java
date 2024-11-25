package ru.practicum.workshop.taskservice.epic.mappers;

import org.mapstruct.*;
import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;
import ru.practicum.workshop.taskservice.epic.model.Epic;
import ru.practicum.workshop.taskservice.tasks.mappers.TaskMapper;

import java.util.HashSet;

@Mapper(uses = {TaskMapper.class}, componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = {HashSet.class})
public interface EpicMapper {
    @Mapping(target = "id", expression = "java(null)")
    Epic toEntity(NewEpicDto dto);

    @Mapping(target = "tasks", defaultExpression = "java(new HashSet<>())")
    EpicDto toDto(Epic epic);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Epic updateEpic(@MappingTarget Epic epic, UpdateEpicDto dto);
}
