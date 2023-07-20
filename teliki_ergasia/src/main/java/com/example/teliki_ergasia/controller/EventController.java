package com.example.teliki_ergasia.controller;

import com.example.teliki_ergasia.models.Event;
import com.example.teliki_ergasia.models.EventParticipant;
import com.example.teliki_ergasia.models.User;
import com.example.teliki_ergasia.repository.EventParticipantRepository;
import com.example.teliki_ergasia.repository.EventRepository;
import com.example.teliki_ergasia.repository.UserRepository;
import com.example.teliki_ergasia.service.EventService;
import com.example.teliki_ergasia.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EventController {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @ModelAttribute("request")
    public HttpServletRequest getRequest(HttpServletRequest request) {
        return request;
    }

    @ModelAttribute("session")
    public HttpSession getSession(HttpSession session) {
        return session;
    }

    @ModelAttribute("servletContext")
    public ServletContext getServletContext(ServletContext servletContext) {
        return servletContext;
    }

    @ModelAttribute("response")
    public HttpServletResponse getResponse(HttpServletResponse response) {
        return response;
    }

    @GetMapping("/event")
    public String event(Model model){
        Event event = new Event();
        model.addAttribute("event", event);
        return "calendar";
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int pageSize,
                                                 HttpSession session) {

        User user = (User) session.getAttribute("user");
        String userId = (user != null) ? String.valueOf(user.getId()) : null;

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<EventParticipant> eventParticipantPage;
        Page<Event> eventPage;

        if (userId != null) {
            eventParticipantPage = eventParticipantRepository.findByUserId(userId, pageable);
            eventPage = eventRepository.findByCreatorId(Integer.parseInt(userId), pageable);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<EventParticipant> eventParticipants = eventParticipantPage.getContent();
        List<Event> events = new ArrayList<>();

        for (EventParticipant eventParticipant : eventParticipants) {
            events.add(eventParticipant.getEvent());
        }

        List<Event> creatorEvents = eventPage.getContent();
        events.addAll(creatorEvents);

        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/deleteEvent")
    public ResponseEntity<String> deleteEvent(@RequestParam("id") int eventId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Event> event = eventRepository.findById(eventId);
        int userId = user.getId();
        int creatorId = event.get().getCreator().getId();
        if (userId == creatorId){
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok("Event deleted successfully");
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting event. Only creator can delete");
        }
    }
    @PostMapping("/addEvent")
    public String addEvent(@ModelAttribute("event") Event event, @RequestParam("people") String people, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        String result = null;
        System.out.println(event);
        event.setCreator(user);
        String[] peopleArray = people.split(","); // Assuming the usernames are separated by commas

        /*Set<User> participants = new HashSet<>();
        for (String username : peopleArray) {
            User participant = userRepository.findByUsername(username.trim());
            if (participant != null) {
                participants.add(participant);
            }
        }*/

        //event.setParticipants(participants);*/
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        //eventService.eventRegister(event);
        Event savedEvent = eventRepository.save(event);
        for (String username : peopleArray) {
            User participant = userRepository.findByUsername(username.trim());
            if (participant != null) {
                // Create an EventParticipant entity and set the event and participant
                EventParticipant eventParticipant = new EventParticipant();
                eventParticipant.setEvent(savedEvent);
                eventParticipant.setUser(participant);
                eventParticipant.setConfirmed(false);

                // Save the eventParticipant
                eventParticipantRepository.save(eventParticipant);
            }
        }
        model.addAttribute("event", event);
        //Event newEvent = new Event();
        //model.addAttribute("event", newEvent);

        result = "calendar";
        return result;
    }

    @PutMapping("/updateEvent")
    public ResponseEntity<String> updateEvent(@RequestParam("id") int eventId, @RequestBody Event updatedEvent, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Event> event = eventRepository.findById(eventId);
        int userId = user.getId();
        int creatorId = event.get().getCreator().getId();
        if (userId == creatorId){
            eventService.updateEvent(eventId, updatedEvent);
            return ResponseEntity.ok("Event updated successfully");
        }
        else {
            System.out.println("ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating event. Only creator can update");
        }
    }
}
