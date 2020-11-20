package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Repository
public class UserRepository implements ProjectRepository<User> {

    private final Logger logger = Logger.getLogger(UserRepository.class);
    private final List<User> repo = new ArrayList<>();

    @Override
    public List<User> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public HashSet<User> retreiveAllBySearch() {
        return null;
    }

    @Override
    public void store(User user) {

        if (!user.getUsername().isBlank() && !user.getPassword().isBlank()) {
            repo.add(user);
            logger.info("store new user: " + user);
        } else {
            return;
        }
    }

    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove) {
        return false;
    }

    @Override
    public boolean listItemByAuthor(String bookAuthorToList) {
        return false;
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        return false;
    }

}
