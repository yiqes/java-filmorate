# Filmorate
================

Проект фильмотеки с возможностью оценки фильмов пользователями и объединением пользователей дружескими связями.

## Схема базы данных
-------------------
![Database schema.](/src/main/resources/static/DBModel-ER-Diagram.png)

| Таблица          | Описание                                                            |
|------------------|---------------------------------------------------------------------|
| **_user_**       | Пользователи ресурса                                                |
| **_film_**       | Информация о фильмах                                                |
| **_likes_**      | Таблица связи для хранения лайков пользователей на фильм            |
| **_friends_**    | Таблица связи для хранения информации о дружбе между пользователями |
| **_film_genre_** | Таблица связи для хранения информации о жанрах фильма               |
| **_genre_**      | Таблица жанров фильма                                               |



## Примеры запросов
-------------------

### Получить список друзей

```sql
SELECT *
  FROM user AS u 
 WHERE u.user_id in (SELECT friend_id 
                       FROM friends AS f
                      WHERE f.user_id = ?
                        AND f.status = 'CONFIRMED');
```
### Получить список пользователей, отправиших запрос на добавление в друзья
```sql
SELECT *
  FROM user AS u 
 WHERE u.user_id in (SELECT friend_id 
                       FROM friends AS f
                      WHERE f.user_id = ?
                        AND f.status = 'UNCONFIRMED');
```
### Получение жанров фильмов, понравившихся пользователю
```sql
SELECT DISTINCT g.name AS genre_name
  FROM film AS f
  JOIN film_ganre AS fg ON (f.film_id = fg.film_id) 
  JOIN genre AS g ON (fg.genre_id = g.genre_id)
 WHERE f.film_id in (SELECT film_id
                       FROM likes AS l
                      WHERE l.user_id = ?);
```