package ru.practicum.workshop.taskservice.epic.service;

import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;

import java.util.Set;

public interface EpicService {
    EpicDto createEpic(NewEpicDto dto);

    EpicDto updateEpic(long epicId, long ownerId, UpdateEpicDto dto);

    EpicDto addTasks(long epicId, long ownerId, Set<Long> taskIds);

    EpicDto getEpicById(long id);

    void deleteEpicById(long epicId, long ownerId);
}
