package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardSecurityService cardSecurityService;


    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findById() {
        Card card = new Card(1L,
                "1fc458296ffdb259d75430012720f3c10cbeb4602f461caabf9a2c47b6b67c20c39dac4f3b51d3a93fefc57cf164deaf",
                BigDecimal.valueOf(100777.00),
                LocalDate.of(2032, 2, 1),
                Status.ACTIVE,
                new User(1L, null, null, null, null, null, null, null));

        User user = new User(
                1L,
                "John",
                "Smith",
                LocalDate.of(2001, 1, 2),
                "js1@gmail.com",
                "$2a$10$Ztg08Gh6cZoKqzmFUJI1DOqp4ny3PPuyuJT2SuWkZo/mwDZXYdpdO",
                Role.USER,
                List.of(card)
        );

        CardDto encryptedCardDto = new CardDto(1L,
                "1fc458296ffdb259d75430012720f3c10cbeb4602f461caabf9a2c47b6b67c20c39dac4f3b51d3a93fefc57cf164deaf",
                LocalDate.of(2032, 2, 1),
                BigDecimal.valueOf(100777.00),
                Status.ACTIVE,
                1L);
        UserDto userDtoFromMapper = new UserDto(
                1L, "John", "Smith",
                LocalDate.of(2001, 1, 2),
                "js1@gmail.com",
                Role.USER,
                List.of(encryptedCardDto)
        );
        CardDto decryptedCardDto = new CardDto(1L,
                "2111213188871227",
                LocalDate.of(2032, 2, 1),
                BigDecimal.valueOf(100777.00),
                Status.ACTIVE,
                1L);
        CardDto maskedCardDto = new CardDto(1L,
                "**** **** **** 1227",
                LocalDate.of(2032, 2, 1),
                BigDecimal.valueOf(100777.00),
                Status.ACTIVE,
                1L);

        UserDto expectedDto = new UserDto(
                1L,
                "John",
                "Smith",
                LocalDate.of(2001, 1, 2),
                "js1@gmail.com",
                Role.USER,
                List.of(maskedCardDto)
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(user)).thenReturn(userDtoFromMapper);



        when(cardSecurityService.decrypt(any(CardDto.class))).thenAnswer(invocation -> {
            CardDto dto = invocation.getArgument(0);
            dto.setCardNumber("2111213188871227");
            return dto;
        });

        when(cardSecurityService.mask(any(CardDto.class))).thenAnswer(invocation -> {
            CardDto dto = invocation.getArgument(0);
            dto.setCardNumber("**** **** **** 1227");
            return dto;
        });

        UserDto actualResult = userService.findById(1L);
        Assertions.assertThat(actualResult).isEqualTo(expectedDto);


    }

    @Test
    void findById_UserNotFound_ThrowsException() {
        when(userRepository.findById(100L)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> userService.findById(100L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User with Id = " + 100L + " not found");
    }
}
