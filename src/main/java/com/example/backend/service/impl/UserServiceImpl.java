package com.example.backend.service.impl;

import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + id));
    }

    // Посмотреть тему прокси. Отличия findById от getById
    // findById() делает реальный SQL-запрос сразу
    // getReferenceById(), возвращает объект-заглушку (прокси - содержит только id), т.е. делает это лениво.
    // Загрузит данные из БД только при первом обращении.

}
