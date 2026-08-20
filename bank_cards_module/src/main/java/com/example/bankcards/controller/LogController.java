package com.example.bankcards.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {
    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping("/test")
    public String test() {
        logger.info("Пользователь вызвал ендпоинт тест");
        return "Log создан";
    }

    @GetMapping("/errortest")
    public String errorTest(){
        logger.error("Тестовая ошибка приложения");
        return "Ошибка записана в лог";
    }
}
