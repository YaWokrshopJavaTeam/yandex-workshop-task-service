package ru.practicum.workshop.taskservice.tasks.enums;

import java.util.Optional;

public enum TaskStatus {
    NEW, IN_PROGRESS, DONE;

    public static Optional<TaskStatus> from(String stringStatus) {
        for (TaskStatus status : values()) {
            if (status.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(status);
            }
        }
        return Optional.empty();
    }
}
