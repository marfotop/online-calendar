package com.example.teliki_ergasia.service.impl;

import com.example.teliki_ergasia.models.User;
import com.example.teliki_ergasia.repository.UserRepository;
import com.example.teliki_ergasia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void userRegister(User user) {
        userRepository.save(user);
    }
}
