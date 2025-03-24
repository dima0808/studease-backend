package tech.studease.studeasebackend.service.impl;

import tech.studease.studeasebackend.domain.UserDetailsImpl;
import tech.studease.studeasebackend.repository.UserRepository;
import tech.studease.studeasebackend.repository.entity.User;
import tech.studease.studeasebackend.service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException(email));
    return UserDetailsImpl.build(user);
  }
}
