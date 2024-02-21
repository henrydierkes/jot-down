package com.astar.ratingbackend.Controller;

import com.astar.ratingbackend.Entity.User;
import com.astar.ratingbackend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/get")
    public List<User> getUser(){
        return userService.getAllUsers();
    }
}
