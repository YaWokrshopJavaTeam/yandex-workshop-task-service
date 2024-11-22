package ru.practicum.workshop.taskservice.tasks.searchparams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PresentationParameters {
    protected Integer page;
    protected Integer size;
}
