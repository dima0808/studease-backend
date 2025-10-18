package tech.studease.studeasebackend.web;

import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.studease.studeasebackend.dto.LoginDto;
import tech.studease.studeasebackend.dto.RegisterDto;
import tech.studease.studeasebackend.dto.UserDto;
import tech.studease.studeasebackend.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterDto registerDto) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("token", authService.register(registerDto)));
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginDto loginDto) {
    return ResponseEntity.ok(Map.of("token", authService.login(loginDto)));
  }

  @GetMapping("/current")
  public ResponseEntity<UserDto> getCurrentUser() {
    return ResponseEntity.ok(authService.getCurrentUser());
  }
}
