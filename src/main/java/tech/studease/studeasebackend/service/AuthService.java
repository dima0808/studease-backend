package tech.studease.studeasebackend.service;

import tech.studease.studeasebackend.dto.LoginDto;
import tech.studease.studeasebackend.dto.RegisterDto;

public interface AuthService {

  String register(RegisterDto registerDto);

  String login(LoginDto loginDto);
}
