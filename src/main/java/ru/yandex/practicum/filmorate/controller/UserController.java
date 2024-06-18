package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.getUserStorage().findAll();
    }

    @GetMapping("/{id}")
    public User find(@PathVariable Long id) {
        return userService.getUserStorage().find(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> findFriends(@PathVariable Long id) {
        return userService.getFriends(userService.getUserStorage().find(id).getFriends());
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.getUserStorage().create(user);
    }

    @PutMapping
    public User amend(@Valid @RequestBody User user) {
        return userService.getUserStorage().amend(user);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void amend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public void delete(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriends(id, friendId);
    }
}