package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserListMapper;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final KafkaProducerService kafkaProducerService;


    @Cacheable(value = "users", key = "#id")
    @PreAuthorize("#id==authentication.principal.id or hasAuthority('ADMIN')")
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id = " + id + " not found"));

        UserDto userDto = userMapper.toUserDto(user);

        maskCards(userDto);

        return userDto;
    }


    @Transactional
    public UserDto createUser(CreateUserRequest createUserRequest) {
        User user = userMapper.toUser(createUserRequest);
        user.setPassword(passwordEncoder.encode(createUserRequest.password()));
        User createdUser = userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(createdUser);

        maskCards(userDto);

        NotificationEvent event = new NotificationEvent(
                userDto.email(),
                userDto.firstname(),
                userDto.lastname(),
                EventType.USER_REGISTERED,
                "Your registration has been successfully confirmed."
        );


        kafkaProducerService.sendMessage(event);

        return userDto;
    }


    @Override
    @CachePut(value = "users", key = "#id")
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

        UserDto userDto = userMapper.toUserDto(savedUser);

        maskCards(userDto);


        return userDto;
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with Id=" + id + " not found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();

        List<UserDto> userDtoList = userListMapper.toUserDtoList(users);

        for (UserDto userDto : userDtoList) {
            maskCards(userDto);
        }


        return userDtoList;
    }



    private void maskCards(UserDto userDto) {
        for(CardDto cardDto : userDto.cards()) {
            CardSecurityUtils.decrypt(cardDto);
            CardSecurityUtils.mask(cardDto);
        }
    }

}
