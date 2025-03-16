package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public Result<List<User>> findAll() {
        return Result.success(userService.findAll());
    }
    
    @PostMapping
    public Result<User> save(@RequestBody User user) {
        return Result.success(userService.save(user));
    }
    
    @GetMapping("/{id}")
    public Result<User> findById(@PathVariable Long id) {
        return Result.success(userService.findById(id));
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return Result.success(null);
    }
} 