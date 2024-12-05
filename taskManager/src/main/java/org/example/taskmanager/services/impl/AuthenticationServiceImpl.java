package org.example.taskmanager.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskmanager.auth.AuthRequest;
import org.example.taskmanager.auth.AuthenticationResponse;
import org.example.taskmanager.auth.RegisterRequest;
import org.example.taskmanager.security.JwtService;
import org.example.taskmanager.entities.User;
import org.example.taskmanager.repositories.UserRepository;
import org.example.taskmanager.services.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     *
     * Метод для регистрации нового пользователя
     * Он создает нового пользователя, кодирует его пароль и сохраняет его в базе данных
     * После успешной регистрации генерируется JWT-токен для аутентификации пользователя
     *
     * @param registerRequest объект, содержащий данные для регистрации нового пользователя.
     * @return объект AuthenticationResponse, содержащий JWT-токен для аутентификации.
     */
    @Override
    public AuthenticationResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .role(registerRequest.getRole())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        userRepository.save(user);

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }

    /**
     * Метод для аутентификации пользователя.
     * Проверяет введенные данные пользователя (email и пароль), и если они верны, генерирует JWT-токен
     *
     * @param request объект, содержащий email и пароль пользователя.
     * @return объект AuthenticationResponse, содержащий JWT-токен для аутентификации.
     * @throws org.springframework.security.authentication.BadCredentialsException если email или пароль неверны.
     */
    @Override
    public AuthenticationResponse authentication(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
