package ru.practicum.workshop.taskservice.tasks.model;

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
import org.hibernate.Hibernate;
import ru.practicum.workshop.taskservice.tasks.enums.TaskStatus;

import java.time.LocalDateTime;
import java.util.Objects;

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
    protected long id;
    @Column(nullable = false)
    protected String title;
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
    protected long assigneeId;
    @Column(name = "AUTHOR_ID", nullable = false)
    protected long authorId;
    @Column(name = "EVENT_ID", nullable = false)
    protected long eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
