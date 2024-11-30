package ru.practicum.workshop.taskservice.epic.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.practicum.workshop.taskservice.epic.dto.EpicDto;
import ru.practicum.workshop.taskservice.epic.dto.NewEpicDto;
import ru.practicum.workshop.taskservice.epic.dto.UpdateEpicDto;
import ru.practicum.workshop.taskservice.epic.service.EpicService;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static ru.practicum.workshop.taskservice.util.EpicControllerValidationConstants.POSITIVE_EPIC_ID_MESSAGE;
import static ru.practicum.workshop.taskservice.util.EpicControllerValidationConstants.POSITIVE_OWNER_ID_MESSAGE;
import static ru.practicum.workshop.taskservice.util.EpicDtoValidationConstants.*;

@WebMvcTest(controllers = EpicController.class)
@RequiredArgsConstructor(onConstructor_= @Autowired)
class EpicControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;

    @MockBean
    private final EpicService epicService;

    private EpicDto epicDto;
    private NewEpicDto newEpicDto;
    private UpdateEpicDto updateEpicDto;
    private MockHttpServletResponse response;
    private MvcResult result;
    private Long id = 1L;

    private MockHttpServletResponse createEpicResponse(NewEpicDto dto) throws Exception {
        result = mvc.perform(post("/tasks/epics")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return result.getResponse();
    }

    @DisplayName("Создать эпик")
    @Test
    void createEpic() throws Exception {
        newEpicDto = new NewEpicDto("name", id++, id++, LocalDateTime.now().plusHours(id));
        epicDto = EpicDto.builder()
                .id(id)
                .name(newEpicDto.getName())
                .ownerId(newEpicDto.getOwnerId())
                .eventId(newEpicDto.getEventId())
                .deadline(newEpicDto.getDeadline())
                .tasks(new HashSet<>())
                .build();

        when(epicService.createEpic(any(NewEpicDto.class))).thenReturn(epicDto);

        response = createEpicResponse(newEpicDto);
        EpicDto receivedDto = mapper.readValue(response.getContentAsString(), EpicDto.class);

        assertEquals(201, response.getStatus());
        assertEquals(epicDto, receivedDto);

        verify(epicService, times(1)).createEpic(any(NewEpicDto.class));
        verifyNoMoreInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto name @NotBlank")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_BlankName() throws Exception {
        newEpicDto = new NewEpicDto(null, id++, id++, LocalDateTime.now().plusHours(id));

        response = createEpicResponse(newEpicDto);

        String responseNullContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseNullContent.contains(NAME_NOT_BLANK_ERROR_MESSAGE));

        NewEpicDto dtoBlankName = new NewEpicDto(" ".repeat(NAME_MIN_SIZE + 1), id++,
                id++, LocalDateTime.now().plusHours(id));

        MockHttpServletResponse responseBlankName = createEpicResponse(dtoBlankName);

        String responseBlankContent = responseBlankName.getContentAsString();
        assertEquals(400, responseBlankName.getStatus());
        assertTrue(responseBlankContent.contains(NAME_NOT_BLANK_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto name @Size")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_IncorrectSizeName() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MAX_SIZE + 1), id++, id++,
                LocalDateTime.now().plusHours(id));

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(NAME_SIZE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto ownerId @NotNull")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_NullOwnerId() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), null, id++, LocalDateTime.now().plusHours(id));

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(OWNER_ID_NOT_NULL_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto ownerId @Positive")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_NegativeOwnerId() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), -1L,
                id++, LocalDateTime.now().plusHours(id));

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(OWNER_ID_POSITIVE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto eventId @NotNull")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_NullEventId() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++,
                null, LocalDateTime.now().plusHours(id));

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(EVENT_ID_NOT_NULL_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto eventId @Positive")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_NegativeEventId() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++,
                -1L, LocalDateTime.now().plusHours(id));

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(EVENT_ID_POSITIVE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto deadline @NotNull")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_NullDeadline() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++,
                id++, null);

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(DEADLINE_NOT_NULL_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация NewEpicDto deadline @Future")
    @Test
    void shouldThrowBadRequest_WhenCreateEpic_PresentPastDeadline() throws Exception {
        newEpicDto = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++,
                id++, LocalDateTime.now());

        response = createEpicResponse(newEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(DEADLINE_FUTURE_ERROR_MESSAGE));

        NewEpicDto dtoWithPastDeadline = new NewEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++,
                id++, LocalDateTime.now().minusMinutes(1));

        MockHttpServletResponse responsePastDeadline = createEpicResponse(dtoWithPastDeadline);

        String responsePastContent = responsePastDeadline.getContentAsString();
        assertEquals(400, responsePastDeadline.getStatus());
        assertTrue(responsePastContent.contains(DEADLINE_FUTURE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    private UpdateEpicDto createUpdateDto() {
        return new UpdateEpicDto("other name", id++, LocalDateTime.now().plusHours(id));
    }

    private EpicDto createEpicDtoFromUpdateDto(UpdateEpicDto updateEpicDto) {
        return EpicDto.builder()
                .id(id++)
                .name(updateEpicDto.getName())
                .ownerId(updateEpicDto.getOwnerId())
                .eventId(id++)
                .deadline(updateEpicDto.getDeadline())
                .tasks(new HashSet<>())
                .build();
    }

    private MockHttpServletResponse updateEpicResponse(Long ownerId, Long epicId, UpdateEpicDto dto) throws Exception {
        result = mvc.perform(patch("/tasks/epics/" + epicId)
                        .content(mapper.writeValueAsString(dto))
                        .header("X-Owner-Id", ownerId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return result.getResponse();
    }

    @DisplayName("Обновить эпик")
    @Test
    void updateEpic() throws Exception {
        updateEpicDto = createUpdateDto();
        epicDto = createEpicDtoFromUpdateDto(updateEpicDto);
        final Long ownerId = id++;
        final Long epicId = epicDto.getId();

        when(epicService.updateEpic(anyLong(), anyLong(), any(UpdateEpicDto.class))).thenReturn(epicDto);

        response = updateEpicResponse(ownerId, epicId, updateEpicDto);

        EpicDto receivedDto = mapper.readValue(response.getContentAsString(), EpicDto.class);

        assertEquals(200, response.getStatus());
        assertEquals(epicDto, receivedDto);

        verify(epicService, times(1))
                .updateEpic(anyLong(), anyLong(), any(UpdateEpicDto.class));
        verifyNoMoreInteractions(epicService);
    }

    @DisplayName("Валидация обновления эпика - requestHeader OwnerId @Positive")
    @Test
    void shouldThrowBadRequest_WhenUpdateEpic_NegativeOwnerIdRequest() throws Exception {
        updateEpicDto = createUpdateDto();
        epicDto = createEpicDtoFromUpdateDto(updateEpicDto);
        final Long ownerId = -1L;
        final Long epicId = epicDto.getId();

        response = updateEpicResponse(ownerId, epicId, updateEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_OWNER_ID_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация обновления эпика - PathVariable epicId @Positive")
    @Test
    void shouldThrowBadRequest_WhenUpdateEpic_NegativeEpicIdRequest() throws Exception {
        updateEpicDto = createUpdateDto();
        epicDto = createEpicDtoFromUpdateDto(updateEpicDto);
        final Long ownerId = id++;
        final Long epicId = -1L;

        response = updateEpicResponse(ownerId, epicId, updateEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_EPIC_ID_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация UpdateEpicDto name @Size")
    @Test
    void shouldThrowBadRequest_WhenUpdateEpic_IncorrectSizeName() throws Exception {
        updateEpicDto = new UpdateEpicDto("n".repeat(NAME_MAX_SIZE + 1), id++, LocalDateTime.now().plusHours(id));
        final Long ownerId = id++;
        final Long epicId = id++;

        response = updateEpicResponse(ownerId, epicId, updateEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(NAME_SIZE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация UpdateEpicDto ownerId @Positive")
    @Test
    void shouldThrowBadRequest_WhenUpdateEpic_NegativeOwnerId() throws Exception {
        updateEpicDto = new UpdateEpicDto("n".repeat(NAME_MIN_SIZE + 1), -1L,
                LocalDateTime.now().plusHours(id));
        final long ownerId = id++;
        final long epicId = id++;

        response = updateEpicResponse(ownerId, epicId, updateEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(OWNER_ID_POSITIVE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация UpdateEpicDto deadline @Future")
    @Test
    void shouldThrowBadRequest_WhenUpdateEpic_PresentPastDeadline() throws Exception {
        updateEpicDto = new UpdateEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++, LocalDateTime.now());
        final long ownerId = id++;
        final long epicId = id++;

        response = updateEpicResponse(ownerId, epicId, updateEpicDto);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(DEADLINE_FUTURE_ERROR_MESSAGE));

        UpdateEpicDto dtoWithPastDeadline = new UpdateEpicDto("n".repeat(NAME_MIN_SIZE + 1), id++,
                LocalDateTime.now().minusMinutes(1));

        MockHttpServletResponse responsePastDeadline = updateEpicResponse(ownerId, epicId, dtoWithPastDeadline);

        String responsePastContent = responsePastDeadline.getContentAsString();
        assertEquals(400, responsePastDeadline.getStatus());
        assertTrue(responsePastContent.contains(DEADLINE_FUTURE_ERROR_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация обновления эпика - requestHeader ownerId отсутствует")
    @Test
    void shouldThrowBadRequest_WhenUpdateEpic_OwnerIdNotExist() throws Exception {
        updateEpicDto = createUpdateDto();
        final long epicId = id++;

        response = mvc.perform(patch("/tasks/epics/" + epicId)
                        .content(mapper.writeValueAsString(updateEpicDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains("X-Owner-Id"));

        verifyNoInteractions(epicService);
    }

    private MockHttpServletResponse addTasksResponse(Long ownerId, Long epicId, Set<Long> taskIds) throws Exception {
        String taskIdsParam = String.valueOf(taskIds).substring(1, String.valueOf(taskIds).length() - 1);
        result = mvc.perform(post("/tasks/epics/" + epicId)
                        .header("X-Owner-Id", ownerId)
                        .param("taskIds", taskIdsParam)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return result.getResponse();
    }

    private FullTaskDto createFullTaskDto() {
        return new FullTaskDto(id++, "title", "description",
                LocalDateTime.now().minusHours(id), LocalDateTime.now().plusHours(id), "New", id, id++, id);
    }

    @DisplayName("Обновить задачи в эпике")
    @Test
    void addTasksToEpic() throws Exception {
        FullTaskDto task1 = createFullTaskDto();
        FullTaskDto task2 = createFullTaskDto();
        FullTaskDto task3 = createFullTaskDto();
        Set<Long> taskIds = new HashSet<>(Set.of(task1.getId(), task2.getId(), task3.getId()));
        epicDto = EpicDto.builder()
                .id(id)
                .name("n".repeat(NAME_MIN_SIZE + 1))
                .ownerId(id)
                .eventId(id++)
                .deadline(LocalDateTime.now().plusHours(id))
                .tasks(new HashSet<>(Set.of(task1, task2, task3)))
                .build();
        final Long ownerId = epicDto.getOwnerId();
        final Long epicId = epicDto.getId();

        when(epicService.addTasks(anyLong(), anyLong(), anySet())).thenReturn(epicDto);

        response = addTasksResponse(ownerId, epicId, taskIds);

        EpicDto receivedDto = mapper.readValue(response.getContentAsString(), EpicDto.class);

        assertEquals(200, response.getStatus());
        assertEquals(epicDto, receivedDto);

        verify(epicService, times(1)).addTasks(anyLong(), anyLong(), anySet());
        verifyNoMoreInteractions(epicService);
    }

    @DisplayName("Валидация добавления задач в эпик - requestHeader OwnerId @Positive")
    @Test
    void shouldThrowBadRequest_WhenAddTasks_NegativeOwnerIdRequest() throws Exception {
        Set<Long> taskIds = new HashSet<>(Set.of(id++, id++, id++));

        final Long ownerId = -1L;
        final Long epicId = id;

        response = addTasksResponse(ownerId, epicId, taskIds);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_OWNER_ID_MESSAGE));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация добавления задач в эпик - requestHeader ownerId отсутствует")
    @Test
    void shouldThrowBadRequest_WhenAddTasks_OwnerIdNotExist() throws Exception {
        Set<Long> taskIds = new HashSet<>(Set.of(id++, id++, id++));

        final Long epicId = id;

        String taskIdsParam = String.valueOf(taskIds).substring(1, String.valueOf(taskIds).length() - 1);

        response = mvc.perform(post("/tasks/epics/" + epicId)
                        .param("taskIds", taskIdsParam)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains("X-Owner-Id"));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация добавления задач в эпик - RequestParam taskIds отсутствуют")
    @Test
    void shouldThrowBadRequest_WhenAddTasks_TaskIdsNotExist() throws Exception {
        final Long ownerId = id;
        final Long epicId = id;

        response = mvc.perform(post("/tasks/epics/" + epicId)
                        .header("X-Owner-Id", ownerId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains("taskIds"));

        verifyNoInteractions(epicService);
    }

    @DisplayName("Валидация добавления задач в эпик - PathVariable epicId @Positive")
    @Test
    void shouldThrowBadRequest_WhenAddTasks_NegativeEpicIdRequest() throws Exception {
        Set<Long> taskIds = new HashSet<>(Set.of(id++, id++, id++));

        final Long ownerId = id;
        final Long epicId = -1L;

        response = addTasksResponse(ownerId, epicId, taskIds);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_EPIC_ID_MESSAGE));
    }

    private MockHttpServletResponse deleteEpicResponse(Long ownerId, Long epicId) throws Exception {
        result = mvc.perform(delete("/tasks/epics/" + epicId)
                        .header("X-Owner-Id", ownerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return result.getResponse();
    }

    @DisplayName("Удалить эпик по id")
    @Test
    void deleteEpicById() throws Exception {
        doNothing().when(epicService).deleteEpicById(anyLong(), anyLong());

        response = deleteEpicResponse(id, id);

        assertEquals(204, response.getStatus());

        verify(epicService, times(1))
                .deleteEpicById(anyLong(), anyLong());
        verifyNoMoreInteractions(epicService);
    }

    @DisplayName("Валидация удаления эпика - requestHeader OwnerId @Positive")
    @Test
    void shouldThrowBadRequest_WhenDeleteEpicById_NegativeOwnerIdRequest() throws Exception {
        final Long ownerId = -1L;
        final Long epicId = id;

        response = deleteEpicResponse(ownerId, epicId);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_OWNER_ID_MESSAGE));
    }

    @DisplayName("Валидация удаления эпика - requestHeader ownerId отсутствует")
    @Test
    void shouldThrowBadRequest_WhenDeleteEpicById_OwnerIdNotExist() throws Exception {
        final Long epicId = id;

        response = mvc.perform(delete("/tasks/epics/" + epicId)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains("X-Owner-Id"));
    }

    @DisplayName("Валидация удаления эпика - PathVariable epicId @Positive")
    @Test
    void shouldThrowBadRequest_WhenDeleteEpicById_NegativeEpicIdRequest() throws Exception {
        final Long ownerId = id;
        final Long epicId = -1L;

        response = deleteEpicResponse(ownerId, epicId);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_EPIC_ID_MESSAGE));
    }

    private MockHttpServletResponse getEpicResponse(Long epicId) throws Exception {
        result = mvc.perform(get("/tasks/epics/" + epicId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        return result.getResponse();
    }

    @DisplayName("Получение эпика по id")
    @Test
    void getEpicById() throws Exception {
        epicDto = EpicDto.builder()
                .id(id)
                .name("n".repeat(NAME_MIN_SIZE + 1))
                .ownerId(id)
                .eventId(id++)
                .deadline(LocalDateTime.now().plusHours(id))
                .tasks(new HashSet<>())
                .build();
        final Long epicId = epicDto.getId();

        when(epicService.getEpicById(anyLong())).thenReturn(epicDto);

        response = getEpicResponse(epicId);

        EpicDto receivedDto = mapper.readValue(response.getContentAsString(), EpicDto.class);

        assertEquals(200, response.getStatus());
        assertEquals(epicDto, receivedDto);

        verify(epicService, times(1)).getEpicById(anyLong());
        verifyNoMoreInteractions(epicService);
    }

    @DisplayName("Валидация полчения эпика по id - PathVariable epicId @Positive")
    @Test
    void shouldThrowBadRequest_WhenGetEpicById_NegativeEpicIdRequest() throws Exception {
        final Long epicId = -1L;

        response = getEpicResponse(epicId);

        String responseContent = response.getContentAsString();
        assertEquals(400, response.getStatus());
        assertTrue(responseContent.contains(POSITIVE_EPIC_ID_MESSAGE));
    }
}