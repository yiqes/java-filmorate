package ru.yandex.practicum.filmorate.model.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Long id;
    @NotBlank
    @Email
    String email;
    String login;
    String name;
    @Past
    LocalDate birthday;
    Set<Long> friends = new HashSet<>();
}