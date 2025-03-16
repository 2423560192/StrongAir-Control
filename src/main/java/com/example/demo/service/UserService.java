package com.example.demo.service;

import com.example.demo.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User save(User user);
    User findById(Long id);
    void deleteById(Long id);
} 