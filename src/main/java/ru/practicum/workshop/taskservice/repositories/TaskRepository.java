package ru.practicum.workshop.taskservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.workshop.taskservice.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
