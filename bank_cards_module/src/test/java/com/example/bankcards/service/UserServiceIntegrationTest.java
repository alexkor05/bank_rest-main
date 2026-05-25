package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Status;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@SqlGroup({
        @Sql(scripts = "classpath:sql/clean.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(scripts = "classpath:sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
})
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class UserServiceIntegrationTest {

    private final UserServiceImpl userService;

    @Test
    @WithUserDetails("js1@gmail.com")
    void findById(){
        UserDto actualResult = userService.findById(1L);

        UserDto expectedResult =
                new UserDto(1L,
                        "Jonh",
                        "Smith",
                        LocalDate.of(2001, 1,2),
                        "js1@gmail.com",
                        Role.USER,
                        List.of(
                            new CardDto(1L, "**** **** **** 1227", LocalDate.of(2032, 2, 1), BigDecimal.valueOf(10077700, 2), Status.ACTIVE, 1L),
                            new CardDto(2L, "**** **** **** 3155", LocalDate.of(2032, 2, 5), BigDecimal.valueOf(500000, 2), Status.ACTIVE, 1L)
                        ));

        Assertions.assertEquals(expectedResult, actualResult);
    }


    @Test
    void findAll() {
        Assertions.assertTrue(true);
    }
}
