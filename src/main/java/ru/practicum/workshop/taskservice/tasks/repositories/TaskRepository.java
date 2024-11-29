package ru.practicum.workshop.taskservice.tasks.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.workshop.taskservice.tasks.model.Task;

import java.util.List;
import java.util.Set;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task AS t " +
            "WHERE (t.eventId = :eventId OR :eventId IS null) " +
                "AND (t.assigneeId = :assigneeId OR :assigneeId IS null) " +
                "AND (t.authorId = :authorId OR :authorId IS null)")
    Page<Task> findByParameters(@Param("eventId") Long eventId,
                                @Param("assigneeId") Long assigneeId,
                                @Param("authorId") Long authorId, Pageable pageable);

    @Query("SELECT DISTINCT t.id FROM Task AS t WHERE t.eventId <> :eventId AND t IN :tasks")
    Set<Long> findTaskIdByIdInAndEventIdNot(@Param("tasks") List<Task> tasks,
                                            @Param("eventId") Long eventId);
}
