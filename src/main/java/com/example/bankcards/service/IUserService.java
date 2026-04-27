package com.example.bankcards.service;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UserDto;

public interface IUserService {
    UserDto findById(Long id);
    UserDto createUser(CreateUserRequest createUserRequest);
}
