package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardSecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserTransaction {

    private final IUserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Transactional()
    public UserDto getUserById(Long id) {
        Random random = new Random();

        userService.createUser(new CreateUserRequest(
                "Alex" + random.nextInt(100),
                "Kor",
                LocalDate.of(1986, 3,3),
                "box" + random.nextInt(100) + "@yandex.ru",
                "123",
                Role.USER
        ));

        int nextInt = random.nextInt(100);

        if(nextInt > 50) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User with Id = " + id + " not found"));

            UserDto userDto = userMapper.toUserDto(user);

            maskCards(userDto);

            return userDto;

        } else {
            throw new RuntimeException();
        }


    }

    private void maskCards(UserDto userDto) {
        for(CardDto cardDto : userDto.cards()) {
            CardSecurityUtils.decrypt(cardDto);
            CardSecurityUtils.mask(cardDto);
        }
    }
}
