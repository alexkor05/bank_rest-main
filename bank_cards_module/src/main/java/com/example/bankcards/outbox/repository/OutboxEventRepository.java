package com.example.bankcards.outbox.repository;

import com.example.bankcards.outbox.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    @Query(value = """
        SELECT *
        FROM outbox_event
        WHERE event_status = 'NEW'
        ORDER BY created_at ASC
        LIMIT :batchSize
        FOR UPDATE SKIP LOCKED
        """
            ,nativeQuery = true)
    List<OutboxEvent> findNewEventsForPublishing(@Param("batchSize") int batchSize);
}
