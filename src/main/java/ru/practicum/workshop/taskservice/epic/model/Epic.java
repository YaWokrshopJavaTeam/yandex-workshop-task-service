package ru.practicum.workshop.taskservice.epic.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.workshop.taskservice.tasks.model.Task;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "EPICS", schema = "PUBLIC")
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Epic {
    @Id
    @Column(name = "EPIC_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    @Column(name = "OWNER_ID", nullable = false)
    protected long ownerId;
    @Column(name = "EVENT_ID", nullable = false)
    protected long eventId;
    protected LocalDateTime deadline;
    @OneToMany(mappedBy = "epicId")
    protected Set<Task> tasks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Epic epic = (Epic) o;
        return id == epic.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownerId=" + ownerId +
                ", eventId=" + eventId +
                ", deadline=" + deadline +
                '}';
    }
}
