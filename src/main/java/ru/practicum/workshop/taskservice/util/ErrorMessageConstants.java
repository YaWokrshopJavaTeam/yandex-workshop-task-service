package ru.practicum.workshop.taskservice.util;

import java.util.Set;

public final class ErrorMessageConstants {
    public static final String FORBIDDEN_DELETE_EPIC_MESSAGE = "Удалить эпик может только его ответственный.";
    public static final String FORBIDDEN_ADD_TASK_EPIC_MESSAGE = "Добавить задачи в эпик может только его ответственный.";
    public static final String FORBIDDEN_UPDATE_EPIC_MESSAGE = "Обновить эпик может только его ответственный.";


    public static String getNotFoundAddingTasks(Set<Long> notFoundTasks) {
        return "Добавление задач невозможно. Задачи с id " + notFoundTasks + " не найдены.";
    }

    public static String getConflictAddTasks(Set<Long> tasks, long eventId) {
        return "Задачи с id " + tasks + " не относятся к мероприятию с id " + eventId + ".";
    }

    public static String getNotFoundEpic(long epicId) {
        return String.format("Эпик с id = %d не найден", epicId);
    }
}
