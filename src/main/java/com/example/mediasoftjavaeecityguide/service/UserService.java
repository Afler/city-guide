package com.example.mediasoftjavaeecityguide.service;

import com.example.mediasoftjavaeecityguide.model.User;
import com.example.mediasoftjavaeecityguide.repository.CommentRepository;
import com.example.mediasoftjavaeecityguide.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    void post() {
        if (userRepository.findByUsername("user").isEmpty()) {
            User user = new User(
                    "user",
                    passwordEncoder.encode("user"),
                    Set.of(new SimpleGrantedAuthority("USER")));

            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }
}
