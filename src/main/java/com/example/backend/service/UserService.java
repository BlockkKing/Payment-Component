package com.example.backend.service;
import com.example.backend.model.User;


public interface UserService {
    User findById(Long id);
}
