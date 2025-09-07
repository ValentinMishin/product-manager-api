package ru.valentin.product_manager_api.service;

import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.valentin.product_manager_api.exception.UserAlreadyExistsException;
import ru.valentin.product_manager_api.model.User;
import ru.valentin.product_manager_api.model.UserRole;
import ru.valentin.product_manager_api.repository.UserRepository;

import java.util.Set;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String username, String password, Set<UserRole> roles) {
        if (userRepository.existsByClientName(username)) {
            throw new UserAlreadyExistsException("Пользователь с логином " + username + " уже существует");
        }

        User user = new User(
                username,
                passwordEncoder.encode(password),
                roles != null ? roles : Set.of(UserRole.ROLE_USER)
        );

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByClientName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public boolean userExists(String username) {
        return userRepository.existsByClientName(username);
    }
}
