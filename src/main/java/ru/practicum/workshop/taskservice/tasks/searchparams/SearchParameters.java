package ru.practicum.workshop.taskservice.tasks.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchParameters {
    protected Long eventId;
    protected Long assigneeId;
    protected Long authorId;
}
