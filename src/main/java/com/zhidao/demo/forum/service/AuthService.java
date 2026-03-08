package com.zhidao.demo.forum.service;

import com.zhidao.demo.forum.model.User;
import com.zhidao.demo.forum.repo.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String displayName, String rawPassword) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User u = new User();
        u.setUsername(username);
        u.setDisplayName(displayName != null && !displayName.isBlank() ? displayName : username);
        u.setPasswordHash(passwordEncoder.encode(rawPassword));
        return userRepository.save(u);
    }

    public Optional<User> authenticate(String username, String rawPassword) {
        return userRepository.findByUsername(username).filter(u -> passwordEncoder.matches(rawPassword, u.getPasswordHash()));
    }
}
