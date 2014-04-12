package com.pineapps.choreit.service;

import com.pineapps.choreit.domain.User;
import com.pineapps.choreit.repository.UserRepository;

import java.util.List;

public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addUser(String name, String emailId) {
        userRepository.insert(new User(name, emailId));
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    public User getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }
}
