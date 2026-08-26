package com.example.bankcards.service;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IUserService {
    UserDto findById(Long id);
    UserDto createUser(CreateUserRequest createUserRequest);
    UserDto updateUser(Long id, UpdateUserRequest updateUserRequest);
    void deleteUser(Long id);
    Page<UserDto> findAll(Pageable pageable);
}
