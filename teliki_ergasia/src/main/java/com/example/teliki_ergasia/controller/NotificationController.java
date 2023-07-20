package com.example.teliki_ergasia.controller;

import com.example.teliki_ergasia.models.EventParticipant;
import com.example.teliki_ergasia.models.User;
import com.example.teliki_ergasia.repository.EventParticipantRepository;
import com.example.teliki_ergasia.service.EventService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class NotificationController {

    @Autowired
    private EventService eventService;
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

    @GetMapping("/notifications")
    public ResponseEntity<List<EventParticipant>> getNotifications(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int pageSize,
                                                                   HttpSession session){
        User user = (User) session.getAttribute("user");
        //String userId = (user != null) ? String.valueOf(user.getId()) : null;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<EventParticipant> eventsPage;

        eventsPage = eventParticipantRepository.findByUserIdAndConfirmed(user.getId(), false, pageable);
        List<EventParticipant> events = eventsPage.getContent();
        // Fetch the creator information for each event

        return ResponseEntity.ok(events);
    }

    @DeleteMapping("/deleteNotification")
    public ResponseEntity<String> deleteNotification(@RequestParam("id") int notificationId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<EventParticipant> notification = eventParticipantRepository.findById(notificationId);
        eventParticipantRepository.deleteById(notificationId);
        return ResponseEntity.ok("Event rejected successfully");
    }

    @PutMapping("/confirmNotification")
    public ResponseEntity<String> confirmNotification(@RequestParam("id") int notificationId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<EventParticipant> notification = eventParticipantRepository.findById(notificationId);
        eventService.confirmNotification(notificationId);
        return ResponseEntity.ok("Event confirmed successfully");
    }
}
