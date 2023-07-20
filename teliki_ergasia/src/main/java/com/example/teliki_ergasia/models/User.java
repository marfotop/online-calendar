package com.example.teliki_ergasia.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor   // no-args constructor
@AllArgsConstructor   // all-args constructor
@Entity
@Table (name = "users")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String username;
    private String password;
    private String cpassword;

    @JsonIgnore
    @OneToMany(mappedBy = "creator", fetch = FetchType.EAGER)
    private List<Event> createdEvents = new ArrayList<>();

    /*@ManyToMany(mappedBy = "participants", cascade = CascadeType.ALL)
    private List<Event> events;*/
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<EventParticipant> events;

}