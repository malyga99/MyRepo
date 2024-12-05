package org.example.taskmanager.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.taskmanager.validation.OnCreate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank(message = "Email не должен быть пустым!", groups = OnCreate.class)
    @Size(min = 5, max = 50, message = "Email должен быть в пределах от 5 до 50 символов! ", groups = OnCreate.class)
    @Email(message = "Email должен быть правильного формата! Например, someemail@email.com", groups = OnCreate.class)
    private String email;

    @NotBlank(message = "Пароль не должен быть пустым!", groups = OnCreate.class)
    @Size(min = 5, max = 50, message = "Пароль должен быть в пределах от 5 до 50 символов! ", groups = OnCreate.class)
    private String password;
}
