package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(Long filmId) {
        return filmStorage.find(filmId);
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.find(filmId);
        User user = userStorage.find(userId);
        if (film != null && user != null) {
            if (film.getLikes() == null) {
                film.setLikes(new HashSet<>());
            }
            film.getLikes().add(user.getId());
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = filmStorage.find(filmId);
        User user = userStorage.find(userId);
        if (film != null && user != null && film.getLikes().contains(user.getId())) {
            film.getLikes().remove(user.getId());
        }
    }

    public List<Film> getPopularFilms(Integer filmQuantity) {
        List<Film> films = filmStorage.findAll();
        return films.stream()
                .sorted((film1, film2) -> Integer.compare((film2 == null || film2.getLikes() == null) ? 0 :
                        film2.getLikes().size(), (film1 == null || film1.getLikes() == null) ? 0 :
                        film1.getLikes().size()))
                .limit(filmQuantity)
                .collect(Collectors.toList());
    }

    public Film update(Film film) {
        return filmStorage.amend(film);
    }
}