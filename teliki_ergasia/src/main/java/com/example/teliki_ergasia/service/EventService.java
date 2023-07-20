package com.example.teliki_ergasia.service;


import com.example.teliki_ergasia.models.Event;
import com.example.teliki_ergasia.models.User;

import java.util.List;

public interface EventService {
     void eventRegister(Event event);

     void eventUpdate(Event event);

     void deleteEvent(int eventId);
     void updateEvent(int eventId, Event updatedEvent);
     void confirmNotification(int notificationId);

}
