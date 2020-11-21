package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final ProjectRepository<User> userRepo;
    private  Logger logger = Logger.getLogger(LoginService.class);

    public LoginService(ProjectRepository<User> userRepo) {
        this.userRepo = userRepo;
    }

    public boolean authenticate(LoginForm loginform) {
        logger.info("try auth with user-form: " + loginform);
        for (User user : userRepo.retreiveAll()) {
            if (user.getUsername().equals(loginform.getUsername()) && user.getPassword().equals(loginform.getPassword())) {
                return true;
            }
        }
        return false;
       }
    }


