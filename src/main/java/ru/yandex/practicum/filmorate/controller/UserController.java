package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || !(user.getEmail().contains("@"))) {
            throw new ValidateException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ")) {
            throw new ValidateException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения не может быть в будущем.");
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getEmail() == null || !(newUser.getEmail().contains("@"))) {
            throw new ValidateException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (newUser.getLogin() == null || newUser.getLogin().contains(" ")) {
            throw new ValidateException("Логин не может быть пустым и содержать пробелы");
        }
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidateException("Дата рождения не может быть в будущем.");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (newUser.getEmail() == null) {
                oldUser.setEmail(oldUser.getEmail());
            } else {
                oldUser.setEmail(newUser.getEmail());
            }
            if (newUser.getName() == null) {
                oldUser.setName(oldUser.getName());
            } else {
                oldUser.setName(newUser.getName());
            }
            if (newUser.getBirthday() == null) {
                oldUser.setBirthday(oldUser.getBirthday());
            } else {
                oldUser.setBirthday(newUser.getBirthday());
            }
            if (newUser.getLogin() == null) {
                oldUser.setLogin(oldUser.getLogin());
            } else {
                oldUser.setLogin(newUser.getLogin());
            }
            return oldUser;
        }
        throw new ValidateException("Пользователь с id = " + newUser.getId() + " не найден");
    }
}
