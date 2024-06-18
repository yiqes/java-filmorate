package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Long, Film> films = new HashMap<>();

    private Long id = 1L;
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    private static final String WRONG_ID = "нет фильма с таким id";

    @Override
    public List<Film> findAll() {
        List<Film> filmList = new ArrayList<>(films.values());
        log.debug("Текущее количесвто фильмов: {}", filmList.size());
        return filmList;
    }

    @Override
    public Film find(Long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundException(WRONG_ID);
        }
    }

    @Override
    public Film create(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительным числом");
        }
        film.setId(id);
        films.put(id, film);
        id++;
        log.debug("Добавлен фильм: {}", film);
        return film;
    }

    @Override
    public Film amend(Film film) {
        Film oldFilm;
        if (films.containsKey(film.getId())) {
            oldFilm = films.get(film.getId());
            films.put(film.getId(), film);
            log.debug("Фильм {} изменен на {}", oldFilm, film);
        } else {
            throw new NotFoundException(WRONG_ID);
        }
        return film;
    }

    @Override
    public void delete(Film film) {
        if (films.containsKey(film.getId())) {
            films.remove(film.getId());
            log.debug("Фильм {} удалён", film);
        } else {
            throw new NotFoundException(WRONG_ID);
        }
    }
}