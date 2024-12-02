package ru.practicum.workshop.taskservice.epic.mappers;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;
import ru.practicum.workshop.taskservice.epic.model.Epic;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EpicMapperTest {
    private final EpicMapper epicMapper;

    private static Epic epic;
    private static long id = 1L;

    @BeforeAll
    static void beforeAll() {
        epic = Epic.builder()
                .id(id++)
                .name("name")
                .ownerId(id++)
                .eventId(id)
                .deadline(LocalDateTime.now().plusHours(8))
                .tasks(new HashSet<>())
                .build();
    }

    private void assertEqualsEpic(Epic expectedEpic, Epic resultEpic) {
        assertAll(
                "Проверка соответствия ожидаемого Epic с полученным",
                () -> assertEquals(expectedEpic.getId(), resultEpic.getId()),
                () -> assertEquals(expectedEpic.getName(), resultEpic.getName()),
                () -> assertEquals(expectedEpic.getOwnerId(), resultEpic.getOwnerId()),
                () -> assertEquals(expectedEpic.getEventId(), resultEpic.getEventId()),
                () -> assertEquals(expectedEpic.getDeadline(), resultEpic.getDeadline()),
                () -> assertEquals(expectedEpic.getTasks(), resultEpic.getTasks())
        );
    }

    @DisplayName("Маппинг DTO при создании в модель Epic")
    @Test
    void toEntity() {
        NewEpicDto dto = new NewEpicDto(epic.getName(), epic.getOwnerId(), epic.getEventId(), epic.getDeadline());

        Epic expectedEpic = epic.toBuilder()
                .id(null)
                .tasks(null)
                .build();
        Epic resultEpic = epicMapper.toEntity(dto);

        assertNotNull(resultEpic);
        assertEqualsEpic(expectedEpic, resultEpic);
    }

    @DisplayName("Маппинг модели Epic в EpicDto")
    @Test
    void toDto() {
        EpicDto expectedDto = EpicDto.builder()
                .id(epic.getId())
                .name(epic.getName())
                .ownerId(epic.getOwnerId())
                .eventId(epic.getEventId())
                .deadline(epic.getDeadline())
                .tasks(new HashSet<>())
                .build();

        EpicDto resultDto = epicMapper.toDto(epic);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
    }

    @DisplayName("Обновление модели Epic с помощью UpdateEpicDto")
    @Test
    void updateEpic() {
        UpdateEpicDto dto = new UpdateEpicDto("other name", ++id, LocalDateTime.now().plusDays(2));
        Epic expectedEpic = epic.toBuilder()
                .name(dto.getName())
                .ownerId(dto.getOwnerId())
                .deadline(dto.getDeadline())
                .build();

        Epic updatedEpic = epicMapper.updateEpic(epic, dto);

        assertNotNull(updatedEpic);
        assertEqualsEpic(expectedEpic, updatedEpic);
    }

    @DisplayName("Обновление модели Epic с помощью UpdateEpicDto - name = null")
    @Test
    void updateEpicWithNullName() {
        UpdateEpicDto dto = new UpdateEpicDto(null, ++id, LocalDateTime.now().plusDays(2));
        Epic expectedEpic = epic.toBuilder()
                .name(epic.getName())
                .ownerId(dto.getOwnerId())
                .deadline(dto.getDeadline())
                .build();

        Epic updatedEpic = epicMapper.updateEpic(epic, dto);

        assertNotNull(updatedEpic);
        assertEqualsEpic(expectedEpic, updatedEpic);
    }

    @DisplayName("Обновление модели Epic с помощью UpdateEpicDto - ownerId = null")
    @Test
    void updateEpicWithNullOwnerId() {
        UpdateEpicDto dto = new UpdateEpicDto("other name", null, LocalDateTime.now().plusDays(2));
        Epic expectedEpic = epic.toBuilder()
                .name(dto.getName())
                .ownerId(epic.getOwnerId())
                .deadline(dto.getDeadline())
                .build();

        Epic updatedEpic = epicMapper.updateEpic(epic, dto);

        assertNotNull(updatedEpic);
        assertEqualsEpic(expectedEpic, updatedEpic);
    }

    @DisplayName("Обновление модели Epic с помощью UpdateEpicDto - deadline = null")
    @Test
    void updateEpicWithNullDeadline() {
        UpdateEpicDto dto = new UpdateEpicDto("other name", ++id, null);
        Epic expectedEpic = epic.toBuilder()
                .name(dto.getName())
                .ownerId(dto.getOwnerId())
                .deadline(epic.getDeadline())
                .build();

        Epic updatedEpic = epicMapper.updateEpic(epic, dto);

        assertNotNull(updatedEpic);
        assertEqualsEpic(expectedEpic, updatedEpic);
    }
}