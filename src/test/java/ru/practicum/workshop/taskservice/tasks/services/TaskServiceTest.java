package ru.practicum.workshop.taskservice.tasks.services;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.workshop.taskservice.tasks.dto.FullTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.NewTaskDto;
import ru.practicum.workshop.taskservice.tasks.dto.UpdateTaskDto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.workshop.taskservice.tasks.searchparams.PresentationParameters;
import ru.practicum.workshop.taskservice.tasks.searchparams.SearchParameters;
import ru.practicum.workshop.taskservice.tasks.services.TaskService;
import ru.practicum.workshop.taskservice.util.service.ExternalEntityService;

import java.time.LocalDateTime;

@Transactional
@ActiveProfiles(value = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TaskServiceTest {

    @MockBean
    private final ExternalEntityService externalEntityService;

    private final TaskService taskService;

    @Test
    void createTask() {
        NewTaskDto newTaskDto = new NewTaskDto("first task", "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now().plusMonths(1), 1, 1);
        long authorId = 1L;
        FullTaskDto fullTaskDto = taskService.createTask(authorId, newTaskDto);
        Assertions.assertNotNull(fullTaskDto, "Задача не сохранена в базу данных");
        Assertions.assertEquals(authorId, fullTaskDto.getAuthorId(), "Id автора сохранённой задачи не соответствует ожидаемому");
        Assertions.assertEquals(newTaskDto.getDescription(), fullTaskDto.getDescription(), "Id сохранённой задачи не соответствует ожидаемому");
    }

    @Test
    void updateTask() {
        NewTaskDto newTaskDto = new NewTaskDto("first task", "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now().plusMonths(1), 1, 1);
        long authorId = 1L;
        FullTaskDto fullTaskDto1 = taskService.createTask(authorId, newTaskDto);
        long fullTaskDto1Id = fullTaskDto1.getId();
        LocalDateTime updateForDeadline = LocalDateTime.now().plusYears(1);
        UpdateTaskDto updateTaskDto = new UpdateTaskDto(null, null, updateForDeadline, "IN_PROGRESS", null, null);
        FullTaskDto updateFullTaskDto = taskService.updateTask(authorId, fullTaskDto1Id, updateTaskDto);
        Assertions.assertEquals(fullTaskDto1Id, updateFullTaskDto.getId(), "Id обновлённой задачи не соответствует ожидаемому");
        Assertions.assertEquals(updateForDeadline, updateFullTaskDto.getDeadline(), "Дедлайн обновлённой задачи не соответствует ожидаемому");
        Assertions.assertEquals("IN_PROGRESS", updateFullTaskDto.getStatus(), "Id обновлённой задачи не соответствует ожидаемому");
    }

    @Test
    void getTaskById() {
        NewTaskDto newTaskDto = new NewTaskDto("first task", "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now().plusMonths(1), 1, 1);
        long authorId = 1L;
        FullTaskDto fullTaskDto = taskService.createTask(authorId, newTaskDto);
        FullTaskDto fullTaskDtoFromDB = taskService.getTaskById(fullTaskDto.getId());
        Assertions.assertEquals(fullTaskDto.getDescription(), fullTaskDtoFromDB.getDescription(), "Описание полученной из базы данных задачи не соответствует ожидаемому");
    }

    @Test
    void getTasks() {
        NewTaskDto newTaskDto1 = new NewTaskDto("first task1", "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now().plusMonths(1), 1, 2);
        long authorId1 = 1L;
        NewTaskDto newTaskDto2 = new NewTaskDto("first task2", "Esdvsdkvsdk cbdsur kojvdjv", LocalDateTime.now().plusMonths(1), 1, 1);
        long authorId2 = 2L;
        NewTaskDto newTaskDto3 = new NewTaskDto("first task3", "Wsdcljsdhvsd jpihvjfvn ierjvp", LocalDateTime.now().plusMonths(1), 1, 3);
        taskService.createTask(authorId1, newTaskDto1);
        taskService.createTask(authorId1, newTaskDto2);
        taskService.createTask(authorId2, newTaskDto3);
        PresentationParameters presentationParameters = new PresentationParameters(0, 10);
        SearchParameters searchParameters1 = new SearchParameters(null, null, 1L);
        SearchParameters searchParameters2 = new SearchParameters(2L, 1L, null);
        SearchParameters searchParameters3 = new SearchParameters(null, 1L, null);
        int length1 = taskService.getTasks(searchParameters1, presentationParameters).size();
        int length2 = taskService.getTasks(searchParameters2, presentationParameters).size();
        int length3 = taskService.getTasks(searchParameters3, presentationParameters).size();
        Assertions.assertEquals(2, length1, "Количество найденных задач не соответствует ожидаемому");
        Assertions.assertEquals(1, length2, "Количество найденных задач не соответствует ожидаемому");
        Assertions.assertEquals(3, length3, "Количество найденных задач не соответствует ожидаемому");
    }

    @Test
    void deleteTask() {
        NewTaskDto newTaskDto1 = new NewTaskDto("first task", "Dsfsdfs dssvdfbfdbdf dfbdfbdf", LocalDateTime.now().plusMonths(1), 1, 2);
        long authorId1 = 1L;
        NewTaskDto newTaskDto2 = new NewTaskDto("first task", "Esdvsdkvsdk cbdsur kojvdjv", LocalDateTime.now().plusMonths(1), 1, 1);
        FullTaskDto savedTask1 = taskService.createTask(authorId1, newTaskDto1);
        FullTaskDto savedTask2 = taskService.createTask(authorId1, newTaskDto2);
        PresentationParameters presentationParameters = new PresentationParameters(0, 10);
        SearchParameters searchParameters = new SearchParameters(null, null, null);
        int length1 = taskService.getTasks(searchParameters, presentationParameters).size();
        Assertions.assertEquals(2, length1, "Количество найденных задач не соответствует ожидаемому");
        taskService.deleteTask(authorId1,savedTask1.getId());
        int length2 = taskService.getTasks(searchParameters, presentationParameters).size();
        Assertions.assertEquals(1, length2, "Количество найденных задач не соответствует ожидаемому");
    }
}
