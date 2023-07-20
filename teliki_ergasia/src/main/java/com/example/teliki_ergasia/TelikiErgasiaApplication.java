package com.example.teliki_ergasia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TelikiErgasiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelikiErgasiaApplication.class, args);
    }
    //test

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "Name", defaultValue = "Maria") String name){
        return "Hello " + name;
    }
}
