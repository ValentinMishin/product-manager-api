package ru.valentin.product_manager_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import ru.valentin.product_manager_api.model.UserRole;

import java.util.Set;

@Schema(description = "DTO для регистрации клиента API")
public class UserRegistrationDto {

    @Schema(description = "Обязательное не пустое имя пользователя")
    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;

    @Schema(description = "Обязательный не пустой пароль для пользователя")
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;

    private Set<UserRole> roles;

    public UserRegistrationDto() {}

    public UserRegistrationDto(String username, String password, Set<UserRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<UserRole> getRoles() { return roles; }
    public void setRoles(Set<UserRole> roles) { this.roles = roles; }
}
