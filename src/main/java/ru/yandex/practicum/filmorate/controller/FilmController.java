package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.ValidateException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/films")
public class FilmController {
    private static Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("FilmController");
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.setLevel(Level.INFO);
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidateException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidateException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза - не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidateException("Продолжительность фильма должна быть положительным числом");
        }
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        if (newFilm.getName() == null) {
            throw new ValidateException("Название не может быть пустым");
        }
        if (newFilm.getDescription().length() > 200) {
            throw new ValidateException("Максимальная длина описания - 200 символов");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidateException("Дата релиза - не раньше 28 декабря 1895 года");
        }
        if (newFilm.getDuration() <= 0) {
            throw new ValidateException("Продолжительность фильма должна быть положительным числом");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            if (newFilm.getName() == null) {
                oldFilm.setName(oldFilm.getName());
            } else {
                oldFilm.setName(newFilm.getName());
            }
            if (newFilm.getDescription() == null) {
                oldFilm.setDescription(oldFilm.getDescription());
            } else {
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getDuration() == null) {
                oldFilm.setDuration(oldFilm.getDuration());
            } else {
                oldFilm.setDuration(newFilm.getDuration());
            }
            if (newFilm.getReleaseDate() == null) {
                oldFilm.setReleaseDate(oldFilm.getReleaseDate());
            } else {
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            return oldFilm;
        }
        throw new ValidateException("Фильм с id = " + newFilm.getId() + " не найден");
    }
}
