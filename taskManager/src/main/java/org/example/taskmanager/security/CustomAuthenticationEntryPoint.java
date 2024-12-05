package org.example.taskmanager.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Класс для обработки ошибок аутентификации (401 Unauthorized) в случае, если пользователь пытается
 * получить доступ к ресурсу без предварительной аутентификации
 * Этот обработчик будет вызываться, когда пользователь не прошел аутентификацию
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String jsonResponse = "{\"message\": \"" + authException.getMessage() + "\", \"status\": " + HttpServletResponse.SC_UNAUTHORIZED + "}";
        response.getWriter().write(jsonResponse);

    }
}
