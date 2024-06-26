package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 1L;
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    private static final String WRONG_ID = "неверный номер ID";

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>(users.values());
        log.debug("Текущее количесвто пользователей: {}", users.size());
        return userList;
    }

    @Override
    public List<User> getUsersByIds(Set<Long> ids) {
        List<User> userList = new ArrayList<>();
        for (Long id : ids) {
            if (users.containsKey(id)) {
                userList.add(users.get(id));
            }
        }
        return userList;
    }

    @Override
    public User create(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелов");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("День рождения не может быть в будущем.");
        }
        if (user.getName().isBlank() && user.getLogin() == null) {
            throw new ValidationException("поля имя и логин не могу быть пустыми одновременно");
        }
        user.setName(checkAndReturnName(user));
        user.setId(id);
        users.put(id, user);
        id++;
        log.debug("Добавлен пользователь: {}", user);
        return user;
    }

    @Override
    public User amend(User user) {
        User oldUser;
        user.setName(checkAndReturnName(user));
        if (users.containsKey(user.getId())) {
            oldUser = users.get(user.getId());
            users.put(user.getId(), user);
            log.debug("Пользователь {} изменен на {}", oldUser, user);
        } else {
            throw new NotFoundException(WRONG_ID);
        }
        return user;
    }

    @Override
    public void delete(User user) {
        user.setName(checkAndReturnName(user));
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            log.debug("Пользователь {} удалён", user);
        } else {
            throw new NotFoundException(WRONG_ID);
        }
    }

    @Override
    public User find(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(WRONG_ID);
        }
    }

    private String checkAndReturnName(User user) {
        if (Objects.isNull(user.getName()) || user.getName().isBlank()) {
            return user.getLogin();
        } else {
            return user.getName();
        }
    }
}