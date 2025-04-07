package dev.kursovoy.mapper;

import dev.kursovoy.DTO.UserResponse;
import dev.kursovoy.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "username", source = "user.cred.username")
    UserResponse userToUserResponse(User user);
}
