package ru.practicum.workshop.taskservice.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.workshop.taskservice.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task AS t " +
            "WHERE (t.eventId = :eventId OR :eventId IS null) " +
                "AND (t.assigneeId = :assigneeId OR :assigneeId IS null) " +
                "AND (t.authorId = :authorId OR :authorId IS null)")
    Page<Task> findByParameters(@Param("eventId") Integer eventId,
                                @Param("assigneeId") Integer assigneeId,
                                @Param("authorId") Integer authorId, Pageable pageable);
}
