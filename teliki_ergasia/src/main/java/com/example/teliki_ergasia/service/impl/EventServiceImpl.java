package com.example.teliki_ergasia.service.impl;

import com.example.teliki_ergasia.models.Event;
import com.example.teliki_ergasia.models.EventParticipant;
import com.example.teliki_ergasia.models.User;
import com.example.teliki_ergasia.repository.EventParticipantRepository;
import com.example.teliki_ergasia.repository.EventRepository;
import com.example.teliki_ergasia.repository.UserRepository;
import com.example.teliki_ergasia.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @Override
    public void eventRegister(Event event) {
        eventRepository.save(event);
    }

    @Override
    public void eventUpdate(Event event)  {
        eventRepository.save(event);
    }

    @Override
    public void deleteEvent(int eventId) {
        System.out.println(eventId);
        eventRepository.deleteById(eventId);
    }

    @Override
    public void updateEvent(int eventId, Event updatedEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event id: " + eventId));
        // Update the event properties with the values from updatedEvent
        event.setTitle(updatedEvent.getTitle());
        event.setDate(updatedEvent.getDate());
        event.setStartingHour(updatedEvent.getStartingHour());
        event.setEndingHour(updatedEvent.getEndingHour());
        event.setPeople(updatedEvent.getPeople());
        event.setLocation(updatedEvent.getLocation());
        event.setSummary(updatedEvent.getSummary());
        

        // Save the updated event to the database
        eventRepository.save(event);
    }

    @Override
    public void confirmNotification(int notificationId) {
        EventParticipant notification = eventParticipantRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid event id: " + notificationId));
        notification.setConfirmed(true);
        eventParticipantRepository.save(notification);
    }
}