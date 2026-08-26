package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.outbox.dto.UserRegisteredPayload;
import com.example.bankcards.outbox.entity.AggregateType;
import com.example.bankcards.outbox.service.OutboxService;
import com.example.bankcards.outbox.service.OutboxServiceImpl;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService{
    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OutboxService outboxService;


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

        UserRegisteredPayload payload = new UserRegisteredPayload(
                user.getId(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname()
        );
        outboxService.saveEvent(
                AggregateType.USER,
                user.getId(),
                EventType.USER_REGISTERED,
                payload
        );
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
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> {
                    UserDto userDto = userMapper.toUserDto(user);
                    maskCards(userDto);
                    return userDto;
                });
    }



    private void maskCards(UserDto userDto) {
        for(CardDto cardDto : userDto.cards()) {
            CardSecurityUtils.decrypt(cardDto);
            CardSecurityUtils.mask(cardDto);
        }
    }

}
