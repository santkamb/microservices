package com.microservice.auth_service.service;

import com.microservice.auth_service.configuration.SecurityBeans;
import com.microservice.auth_service.dto.AuthRequest;
import com.microservice.auth_service.dto.RegisterRequest;
import com.microservice.auth_service.entity.User;
import com.microservice.auth_service.exception.InvalidCredentialsException;
import com.microservice.auth_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    Logger log = LoggerFactory.getLogger(AuthService.class);
  private final UserRepository userRepository;
  private final SecurityBeans securityBeans;
    @Autowired
    PasswordEncoder passwordEncoder;
  public AuthService(UserRepository userRepository, SecurityBeans securityBeans) {
      this.userRepository = userRepository;
      this.securityBeans = securityBeans;
  }

  public Optional<User> createdUser(RegisterRequest registerRequest) {
      if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
          throw new RuntimeException("User already exists");
      }
      // Logic to create a user using userRepository and securityBeans
      User user = new User();
      user.setUsername(registerRequest.getUsername());
      user.setPassword(securityBeans.passwordEncoder().encode(registerRequest.getPassword()));
      user.setRole(registerRequest.getRole());
      log.info("User created successfully: " + user.getUsername());
      userRepository.save(user);
      return Optional.of(user);
  }

    public User validateUser(AuthRequest request) {

        User user = userRepository.findByUsername(
                        request.getUsername())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid username or password"));

        boolean validPassword =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!validPassword) {
            throw new RuntimeException("Invalid Password");
        }

        return user;
    }
}

