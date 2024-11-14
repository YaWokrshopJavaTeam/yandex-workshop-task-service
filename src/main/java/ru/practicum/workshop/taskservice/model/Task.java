package ru.practicum.workshop.taskservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.workshop.taskservice.enums.TaskStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASKS", schema = "PUBLIC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @Column(name = "TASK_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;
    @Column(nullable = false)
    protected String description;
    @Column(name = "CREATED", nullable = false)
    protected LocalDateTime createdDateTime;
    @Column(nullable = false)
    protected LocalDateTime deadline;
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    protected TaskStatus status;
    @Column(name = "ASSIGNEE_ID", nullable = false)
    protected int assigneeId;
    @Column(name = "AUTHOR_ID", nullable = false)
    protected int authorId;
    @Column(name = "EVENT_ID", nullable = false)
    protected int eventId;
}
