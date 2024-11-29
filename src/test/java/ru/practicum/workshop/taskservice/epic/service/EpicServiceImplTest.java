package ru.practicum.workshop.taskservice.epic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;
import ru.practicum.workshop.taskservice.epic.mappers.EpicMapper;
import ru.practicum.workshop.taskservice.epic.model.Epic;
import ru.practicum.workshop.taskservice.epic.repositories.EpicRepository;
import ru.practicum.workshop.taskservice.exceptions.ConflictException;
import ru.practicum.workshop.taskservice.exceptions.ForbiddenException;
import ru.practicum.workshop.taskservice.tasks.enums.TaskStatus;
import ru.practicum.workshop.taskservice.tasks.model.Task;
import ru.practicum.workshop.taskservice.tasks.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.workshop.taskservice.util.ErrorMessageConstants.*;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_= @Autowired)
public class EpicServiceImplTest {
    private final EpicService epicService;
    private final EpicMapper epicMapper;
    private final EpicRepository epicRepository;
    private final TaskRepository taskRepository;

    private EpicDto epicDto;
    private Epic epic;
    private Long id = 1L;
    private Long taskId = 1L;

    private Epic getNewEpic() {
        return Epic.builder()
                .name("name")
                .ownerId(id++)
                .eventId(id++)
                .deadline(LocalDateTime.now().plusDays(id))
                .build();
    }

    @DisplayName("Создать эпик")
    @Test
    void createEpic() {
        NewEpicDto creationDto = new NewEpicDto("name", id++, id, LocalDateTime.now().plusDays(id));

        epicDto = epicService.createEpic(creationDto);

        Epic expectedEpic = epicMapper.toEntity(creationDto).toBuilder().id(epicDto.getId()).build();
        EpicDto expectedDto = epicMapper.toDto(expectedEpic);

        assertNotNull(epicDto);
        assertEquals(expectedDto, epicDto);
    }

    @DisplayName("Обновить эпик")
    @Test
    void updateEpic() {
        epic = epicRepository.save(getNewEpic());

        UpdateEpicDto updatingDto = new UpdateEpicDto("other name", id++, LocalDateTime.now().plusMonths(id));
        Epic expectedEpic = epic.toBuilder()
                .name(updatingDto.getName())
                .ownerId(updatingDto.getOwnerId())
                .deadline(updatingDto.getDeadline())
                .build();
        EpicDto expectedDto = epicMapper.toDto(expectedEpic);

        epicDto = epicService.updateEpic(epic.getId(), epic.getOwnerId(), updatingDto);

        assertNotNull(epicDto);
        assertEquals(expectedDto, epicDto);
    }

    @DisplayName("Ошибка Not Found при обновлении эпика")
    @Test
    void shouldThrow_EntityNotFound_WhenUpdateEpic() {
        UpdateEpicDto updatingDto = new UpdateEpicDto("other name", id++, LocalDateTime.now().plusMonths(id));
        long epicId = Long.MAX_VALUE;
        final Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            epicService.updateEpic(epicId, id, updatingDto);
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getNotFoundEpic(epicId));
    }

    @DisplayName("Ошибка Forbidden при обновлении эпика не ответственным")
    @Test
    void shouldThrow_Forbidden_WhenUpdateEpic() {
        epic = epicRepository.save(getNewEpic());

        UpdateEpicDto updatingDto = new UpdateEpicDto("other name", id++, LocalDateTime.now().plusMonths(id));

        long ownerId = Long.MAX_VALUE;
        final Exception exception = assertThrows(ForbiddenException.class, () -> {
            epicService.updateEpic(epic.getId(), ownerId, updatingDto);
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, FORBIDDEN_UPDATE_EPIC_MESSAGE);
    }

    private Task createTask(long eventId) {
        return taskRepository.save(new Task(taskId++, "title", "description",
                LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(8),
                TaskStatus.NEW, id++, id++, eventId));
    }

    @DisplayName("Добавить задачи в эпик")
    @Test
    void addTasks() {
        epic = epicRepository.save(getNewEpic());
        long ownerId = epic.getOwnerId();
        long eventId = epic.getEventId();

        Task task1 = createTask(eventId);
        Task task2 = createTask(eventId);

        Epic expectedEpic = epic.toBuilder()
                .tasks(new HashSet<>(List.of(task1, task2))).build();
        EpicDto expectedDto = epicMapper.toDto(expectedEpic);

        EpicDto resultDto = epicService.addTasks(epic.getId(), ownerId,
                new HashSet<>(Set.of(task1.getId(), task2.getId())));

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
    }

    @DisplayName("Добавить задачи в эпик с задачами")
    @Test
    void addTasks_WhenTasksNotEmpty() {
        epic = epicRepository.save(getNewEpic());
        long ownerId = epic.getOwnerId();
        long eventId = epic.getEventId();

        Task task1 = createTask(eventId);
        Task task2 = createTask(eventId);

        EpicDto dtoWith2Tasks = epicService.addTasks(epic.getId(), ownerId,
                new HashSet<>(Set.of(task1.getId(), task2.getId())));

        assertNotNull(dtoWith2Tasks);
        assertEquals(2, dtoWith2Tasks.getTasks().size());

        Task task3 = createTask(eventId);
        Task task4 = createTask(eventId);

        Epic expectedEpic = epic.toBuilder()
                .tasks(new HashSet<>(List.of(task1, task2, task3, task4)))
                .build();
        EpicDto expectedDto = epicMapper.toDto(expectedEpic);

        EpicDto resultDto = epicService.addTasks(epic.getId(), ownerId,
                new HashSet<>(Set.of(task3.getId(), task4.getId())));

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
    }

    @DisplayName("Ошибка Not Found при добавлении несуществующих задач в эпик")
    @Test
    void shouldThrow_EntityNotFound_Tasks_WhenAddTasks() {
        epic = epicRepository.save(getNewEpic());
        long ownerId = epic.getOwnerId();
        long eventId = epic.getEventId();
        long notExistingTaskId = Long.MAX_VALUE;
        Set<Long> notFoundTasks = new HashSet<>(List.of(notExistingTaskId));

        Task task1 = createTask(eventId);

        final Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            epicService.addTasks(epic.getId(), ownerId, new HashSet<>(Set.of(task1.getId(), notExistingTaskId)));
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getNotFoundAddingTasks(notFoundTasks));
    }

    @DisplayName("Ошибка Not Found при добавлении задач в несуществующий эпик")
    @Test
    void shouldThrow_EntityNotFound_Event_WhenAddTasks() {
        long ownerId = id++;
        long eventId = id++;
        long epicId = Long.MAX_VALUE;

        Task task1 = taskRepository.save(new Task(id++, "title", "description",
                LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(8),
                TaskStatus.NEW, id++, id++, eventId));

        final Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            epicService.addTasks(epicId, ownerId, new HashSet<>(Set.of(task1.getId())));
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getNotFoundEpic(epicId));
    }

    @DisplayName("Ошибка Conflict при добавлении задач с другим eventId в эпик")
    @Test
    void shouldThrow_Conflict_WhenAddTasks() {
        epic = epicRepository.save(getNewEpic());
        long ownerId = epic.getOwnerId();
        long eventId = Long.MAX_VALUE;

        Task task1 = createTask(eventId);
        Task task2 = createTask(epic.getEventId());
        Set<Long> otherEventTasks = new HashSet<>(List.of(task1.getId()));

        final Exception exception = assertThrows(ConflictException.class, () -> {
            epicService.addTasks(epic.getId(), ownerId, new HashSet<>(Set.of(task1.getId(), task2.getId())));
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getConflictAddTasks(otherEventTasks, epic.getEventId()));
    }

    @DisplayName("Ошибка Forbidden при добавлении задач в эпик не автором")
    @Test
    void shouldThrow_Forbidden_WhenAddTasks() {
        epic = epicRepository.save(getNewEpic());
        long ownerId = Long.MAX_VALUE;
        long eventId = epic.getEventId();

        Task task1 = createTask(eventId);
        Task task2 = createTask(eventId);

        final Exception exception = assertThrows(ForbiddenException.class, () -> {
            epicService.addTasks(epic.getId(), ownerId, new HashSet<>(Set.of(task1.getId(), task2.getId())));
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, FORBIDDEN_ADD_TASK_EPIC_MESSAGE);
    }

    @DisplayName("Получить эпик по id")
    @Test
    void getEpicById() {
        epic = epicRepository.save(getNewEpic());
        EpicDto expectedDto = epicMapper.toDto(epic);

        epicDto = epicService.getEpicById(epic.getId());

        assertNotNull(epicDto);
        assertEquals(expectedDto, epicDto);
    }

    @DisplayName("Ошибка Not Found при получении эпика по id")
    @Test
    void shouldThrow_EntityNotFound_WhenGetEpicById() {
        long epicId = Long.MAX_VALUE;

        final Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            epicService.getEpicById(epicId);
        });

        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getNotFoundEpic(epicId));
    }

    @DisplayName("Удалить эпик по id")
    @Test
    void deleteEpicById() {
        epic = epicRepository.save(getNewEpic());

        epicService.deleteEpicById(epic.getId(), epic.getOwnerId());

        // Проверка отсутствия эпика в БД
        final Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            epicService.getEpicById(epic.getId());
        });
        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getNotFoundEpic(epic.getId()));
    }

    @DisplayName("Ошибка Not Found при удалении несуществующего эпика по id")
    @Test
    void shouldThrow_EntityNotFound_WhenDeleteEpicById() {
        long epicId = Long.MAX_VALUE;
        epic = epicRepository.save(getNewEpic());

        final Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            epicService.deleteEpicById(epicId, epic.getOwnerId());
        });
        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, getNotFoundEpic(epicId));
    }

    @DisplayName("Ошибка Not Found при удалении эпика по id не ответственным")
    @Test
    void shouldThrow_Forbidden_WhenDeleteEpicById() {
        long ownerId = Long.MAX_VALUE;
        epic = epicRepository.save(getNewEpic());

        final Exception exception = assertThrows(ForbiddenException.class, () -> {
            epicService.deleteEpicById(epic.getId(), ownerId);
        });
        final String actualMessage = exception.getMessage();
        assertEquals(actualMessage, FORBIDDEN_DELETE_EPIC_MESSAGE);
    }
}