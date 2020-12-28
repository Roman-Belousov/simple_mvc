package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.UserService;
import org.example.web.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(value = "/users")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public String users(Model model) {
        logger.info("got user list");
        model.addAttribute("user", new User());
        model.addAttribute("userList", userService.getAllUsers());
        return "user_list";
    }

    @PostMapping("/save")
    public String saveUser(User user) {
        userService.saveUser(user);
        logger.info("current repository size: " + userService.getAllUsers().size());
        return "redirect:/users/list";
    }
    }