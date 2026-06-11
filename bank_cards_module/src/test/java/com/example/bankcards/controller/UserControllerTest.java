package com.example.bankcards.controller;

import com.example.bankcards.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserControllerTest extends IntegrationTestBase {

    private final MockMvc mockMvc;

    @Test
    @WithUserDetails("js1@gmail.com")
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstname").value("Jonh"))
                .andExpect(jsonPath("$.lastname").value("Smith"))
                .andExpect(jsonPath("$.birthDate").value("2001-01-02"))
                .andExpect(jsonPath("$.email").value("js1@gmail.com"))
                .andExpect(jsonPath("$.role").value("USER"))

                .andExpect(jsonPath("$.cards", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.cards[0].id").value(1))
                .andExpect(jsonPath("$.cards[0].cardNumber").value("**** **** **** 1227"))
                .andExpect(jsonPath("$.cards[0].expiryDate").value("2032-02-01"))
                .andExpect(jsonPath("$.cards[0].balance").value(100777.00))
                .andExpect(jsonPath("$.cards[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$.cards[0].userId").value(1))

                .andExpect(jsonPath("$.cards[1].id").value(2))
                .andExpect(jsonPath("$.cards[1].cardNumber").value("**** **** **** 3155"))
                .andExpect(jsonPath("$.cards[1].expiryDate").value("2032-02-05"))
                .andExpect(jsonPath("$.cards[1].balance").value(5000.00))
                .andExpect(jsonPath("$.cards[1].status").value("ACTIVE"))
                .andExpect(jsonPath("$.cards[1].userId").value(1));

    }
}