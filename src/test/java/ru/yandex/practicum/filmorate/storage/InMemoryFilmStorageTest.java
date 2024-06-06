package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;


public class InMemoryFilmStorageTest extends ru.yandex.practicum.filmorate.storage.FilmStorageTest<InMemoryFilmStorage> {
    InMemoryFilmStorage filmStorage;

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        super.filmStorage = filmStorage;
    }

    @Test
    void create() {
        super.create();
    }
}