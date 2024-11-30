package ru.practicum.workshop.taskservice.epic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.workshop.taskservice.epic.model.Epic;


public interface EpicRepository extends JpaRepository<Epic, Long> {
}
