package ru.practicum.ewmcore.model.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UserFullDto {
    private Long id;
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;
    @Email(message = "Адрес электронной почты должен быть в формате mail@mail.ru")
    private String email;
}
