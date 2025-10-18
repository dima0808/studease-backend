package tech.studease.studeasebackend.service.mapper;

import org.mapstruct.Mapper;
import tech.studease.studeasebackend.dto.UserDto;
import tech.studease.studeasebackend.repository.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toUserDto(User user);
}
