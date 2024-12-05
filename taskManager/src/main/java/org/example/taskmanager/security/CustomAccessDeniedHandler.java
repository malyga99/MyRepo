package org.example.taskmanager.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Класс для обработки ошибок доступа (403 Forbidden) в случае, если у пользователя нет прав на выполнение запроса
 * Этот обработчик будет вызываться в случае, если пользователь пытается получить доступ к ресурсу, для которого
 * у него недостаточно прав
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String jsonResponse = "{\"message\": \"" + accessDeniedException.getMessage() + "\", \"status\": " + HttpServletResponse.SC_FORBIDDEN + "}";
        response.getWriter().write(jsonResponse);
    }
}
