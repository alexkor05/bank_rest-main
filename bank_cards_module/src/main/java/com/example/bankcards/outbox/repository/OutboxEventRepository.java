package com.example.bankcards.outbox.repository;

import com.example.bankcards.outbox.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}
