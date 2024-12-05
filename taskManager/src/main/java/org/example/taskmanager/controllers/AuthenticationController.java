package org.example.taskmanager.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.taskmanager.auth.AuthRequest;
import org.example.taskmanager.auth.AuthenticationResponse;
import org.example.taskmanager.auth.RegisterRequest;
import org.example.taskmanager.services.AuthenticationService;
import org.example.taskmanager.validation.OnCreate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthenticationController")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     *
     * Метод для регистрации нового пользователя.
     *
     * @param request данные для регистрации пользователя.
     * @return ответ с JWT токеном, если регистрация прошла успешно.
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создаёт нового пользователя на основе предоставленных данных и возвращает JWT токен"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Регистрация прошла успешно, возвращён JWT токен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные для регистрации"
            )
    })
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Validated(OnCreate.class) @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     *
     * Метод для аутентификации пользователя.
     *
     * @param request учетные данные для аутентификации пользователя.
     * @return ответ с JWT токеном, если аутентификация прошла успешно.
     */
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Проверяет предоставленные учетные данные и возвращает JWT токен, если аутентификация успешна"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Аутентификация прошла успешно, возвращён JWT токен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные для регистрации"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Пользователь не существует или некорректные учетные данные"
            )
    })
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authentication(
            @Validated(OnCreate.class) @RequestBody AuthRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authentication(request));
    }
}
