package tech.studease.studeasebackend.service.impl;

import static tech.studease.studeasebackend.util.AuthUtils.getUserFromAuthentication;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.studease.studeasebackend.dto.LoginDto;
import tech.studease.studeasebackend.dto.RegisterDto;
import tech.studease.studeasebackend.dto.UserDto;
import tech.studease.studeasebackend.repository.RoleRepository;
import tech.studease.studeasebackend.repository.UserRepository;
import tech.studease.studeasebackend.repository.entity.Role;
import tech.studease.studeasebackend.repository.entity.User;
import tech.studease.studeasebackend.service.AuthService;
import tech.studease.studeasebackend.service.exception.EmailAlreadyExistsException;
import tech.studease.studeasebackend.service.exception.RoleNotFoundException;
import tech.studease.studeasebackend.service.mapper.UserMapper;
import tech.studease.studeasebackend.util.AuthUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthUtils authUtils;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  private final UserDetailsService userDetailsService;

  @Override
  public String register(RegisterDto registerDto) {
    if (userRepository.existsByEmail(registerDto.getEmail())) {
      throw new EmailAlreadyExistsException(registerDto.getEmail());
    }
    User user =
        User.builder()
            .email(registerDto.getEmail())
            .firstName(registerDto.getFirstName())
            .lastName(registerDto.getLastName())
            .balance(0)
            .isActive(true)
            .password(passwordEncoder.encode(registerDto.getPassword()))
            .build();
    Role userRole =
        roleRepository
            .findByName("ROLE_USER")
            .orElseThrow(() -> new RoleNotFoundException("ROLE_USER"));
    Set<Role> roles = Set.of(userRole);
    user.setRoles(roles);
    userRepository.save(user);
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                registerDto.getEmail(), registerDto.getPassword()));
    return authUtils.generateToken(authentication);
  }

  @Override
  public String login(LoginDto loginDto) {
    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    return authUtils.generateToken(authentication);
  }

  @Override
  public UserDto getCurrentUser() {
    User user = getUserFromAuthentication();
    return userMapper.toUserDto(user);
  }

  @Override
  public Authentication authenticate(String authorizationHeader) {
    String token = authUtils.parseJwt(authorizationHeader);

    if (token != null && authUtils.validateToken(token)) {
      String username = authUtils.extractClaims(token).getSubject();
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      if (userDetails == null) {
        return null;
      }
      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    return SecurityContextHolder.getContext().getAuthentication();
  }
}
