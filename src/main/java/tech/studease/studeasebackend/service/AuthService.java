package tech.studease.studeasebackend.service;

import tech.studease.studeasebackend.dto.LoginDto;
import tech.studease.studeasebackend.dto.RegisterDto;
import tech.studease.studeasebackend.dto.UserDto;

public interface AuthService {

  String register(RegisterDto registerDto);

  String login(LoginDto loginDto);

  UserDto getCurrentUser();
}
