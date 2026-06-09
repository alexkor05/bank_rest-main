package com.example.bankcards.mapper;

import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CardListMapper.class)
public interface UserMapper {
    UserDto toUserDto(User user);

    User toUser(CreateUserRequest createUserRequest);
}
