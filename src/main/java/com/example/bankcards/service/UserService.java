package com.example.bankcards.service;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto findById(Long id) {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id = " + id + " not found"));
        return userMapper.toUserDto(user);
    }

    public UserDto createUser(CreateUserRequest createUserRequest) {
        User user = userMapper.toUser(createUserRequest);
        User createdUser = userRepository.save(user);
        return userMapper.toUserDto(createdUser);
    }
}
