package ru.practicum.workshop.taskservice.util;

public final class EpicDtoValidationConstants {
    public static final String NAME_NOT_BLANK_ERROR_MESSAGE = "Epic's name shouldn't be blank";
    public static final int NAME_MIN_SIZE = 1;
    public static final int NAME_MAX_SIZE = 250;
    public static final String NAME_SIZE_ERROR_MESSAGE = "Epic's name shouldn't be less then "
            + NAME_MIN_SIZE + " and more than "+ NAME_MAX_SIZE + " characters";

    public static final String OWNER_ID_NOT_NULL_ERROR_MESSAGE = "Owner's id shouldn't be null";
    public static final String OWNER_ID_POSITIVE_ERROR_MESSAGE = "Owner's id should be positive";

    public static final String EVENT_ID_NOT_NULL_ERROR_MESSAGE = "Event's id shouldn't be null";
    public static final String EVENT_ID_POSITIVE_ERROR_MESSAGE = "Event's id should be positive";

    public static final String DEADLINE_NOT_NULL_ERROR_MESSAGE = "Deadline shouldn't be null";
    public static final String DEADLINE_FUTURE_ERROR_MESSAGE = "Deadline should be in future";
}
