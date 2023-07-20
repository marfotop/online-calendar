package com.example.teliki_ergasia.repository;

import com.example.teliki_ergasia.models.Event;
import com.example.teliki_ergasia.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {
    Event findByTitle(String title);
    Page<Event> findByParticipantsId(String userId, Pageable pageable);
    Page<Event> findByCreatorIdOrParticipantsId(String userId, String userId1, Pageable pageable);

    Page<Event> findByCreatorId(int parseInt, Pageable pageable);
}
