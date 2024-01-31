package com.shop.backend.mappers;
import com.shop.backend.dtos.SignUpDto;
import com.shop.backend.dtos.UserDto;
import com.shop.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "verified", ignore = true)
    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto userDto);
}