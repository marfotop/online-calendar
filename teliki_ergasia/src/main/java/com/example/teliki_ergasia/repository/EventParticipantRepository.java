package com.example.teliki_ergasia.repository;

import com.example.teliki_ergasia.models.EventParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventParticipantRepository extends JpaRepository<EventParticipant, Integer> {
    EventParticipant findByEventIdAndUserId(int eventId, int userId);
    Page<EventParticipant> findByUserIdAndConfirmed(int userId, boolean confirmed, Pageable pageable);

    Page<EventParticipant> findByUserId(String userId, Pageable pageable);
}


