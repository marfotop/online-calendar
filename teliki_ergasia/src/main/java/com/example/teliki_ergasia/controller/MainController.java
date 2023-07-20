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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
public class MainController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/register")
    public String register(Model model){
        User user = new User();
        model.addAttribute("user", user);
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        return "register";
    }

    @PostMapping("/registerUser")
    public String registerUser(@ModelAttribute("user") User user, Model model,HttpSession session) {
        String result = null;
        System.out.println(user);
        if (user.getPassword().equals(user.getCpassword())) {
            try {
                userService.userRegister(user);
                session.setAttribute("user", user);
                Event event = new Event();
                model.addAttribute("event", event);
                List<User> allUsers = userRepository.findAll(); // Retrieve all users
                model.addAttribute("allUsers", allUsers); // Add all users to the model
                result = "calendar";
            } catch (Exception e) {
                result = "error";
            }
        }
        return result;
    }

    @GetMapping("/")
    public String login(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "index";
    }

    @GetMapping("/calendar")
    public String showCalendar(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("user");
        model.addAttribute("currentUserUsername", user.getUsername());

        if (user == null) {
            return "redirect:/";
        }
        Event event = new Event();
        model.addAttribute("event", event);
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        return "calendar";
    }

    @PostMapping("/userLogin")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session, HttpServletRequest request) {
        String userName = user.getUsername();

        User userData = userRepository.findByUsername(userName);
        if (userData != null && user.getPassword().equals(userData.getPassword())) {
            session.setAttribute("user", userData);
            Event event = new Event();
            model.addAttribute("event", event);
            model.addAttribute("request", request);

            List<User> allUsers = userRepository.findAll();
            model.addAttribute("allUsers", allUsers);
            model.addAttribute("currentUserUsername", userName);
            return "calendar";
        } else {
            return "error";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidate the user's session
        request.getSession().invalidate();
        // Redirect the user to the login page
        return "redirect:/";
    }

}