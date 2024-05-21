package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {
	@Test
	public void testCreateFilmWithValidData() {
		Film film = new Film();
		FilmController filmController = new FilmController();
		film.setName("Test Film");
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2020, 1, 1));
		film.setDuration((120L));

		Film createdFilm = filmController.create(film);

		assertEquals(createdFilm.getName(), "Test Film");
		assertEquals(createdFilm.getDescription(), "Test Description");
		assertEquals(createdFilm.getReleaseDate(), LocalDate.of(2020, 1, 1));
		assertEquals(createdFilm.getDuration(), 120);
	}

	@Test
	public void testCreateFilmWithInvalidName() {
		Film film = new Film();
		FilmController filmController = new FilmController();
		film.setName(null);
		film.setDescription("Test Description");
		film.setReleaseDate(LocalDate.of(2020, 1, 1));
		film.setDuration(120L);

		assertThrows(ValidateException.class, () -> filmController.create(film));
	}
}
