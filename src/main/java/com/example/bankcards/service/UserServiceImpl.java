package com.example.bankcards.service;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserListMapper;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService{
    private final UserMapper userMapper;
    private final UserListMapper userListMapper;
    private final UserRepository userRepository;




    public UserDto findById(Long id) {
        User user = userRepository.findByIdWithCards(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id = " + id + " not found"));
        return userMapper.toUserDto(user);
    }

    @Transactional
    public UserDto createUser(CreateUserRequest createUserRequest) {
        User user = userMapper.toUser(createUserRequest);
        User createdUser = userRepository.save(user);
        return userMapper.toUserDto(createdUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id=" + id + " not found"));
        user.setEmail(updateUserRequest.email());
        user.setBirthDate(updateUserRequest.birthDate());
        user.setFirstname(updateUserRequest.firstname());
        user.setLastname(updateUserRequest.lastname());
        user.setRole(updateUserRequest.role());
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id=" + id + " not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return userListMapper.toUserDtoList(users);
    }

}
