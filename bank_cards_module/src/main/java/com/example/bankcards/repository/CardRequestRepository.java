package com.example.bankcards.repository;

import com.example.bankcards.entity.CardRequest;
import com.example.bankcards.entity.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRequestRepository extends JpaRepository<CardRequest, Long> {


    Long countByCardIdAndStatus(Long cardId, RequestStatus requestStatus);

    List<CardRequest> findByUserId(Long userId);

    List<CardRequest> findAllByStatus(RequestStatus status);
}
