# Фильмы Ghibli Studio

Музыченко Алевтина Игоревна  
Группа: Б9124-09.03.03пикд(4)

## Выбранный API
GHIBLI - Studio Ghibli API  
Base URL: https://ghibli-api.vercel.app/api/  

API предоставляет данные о фильмах студии Ghibli:
- "id": string
- "title": string
- "image": string
- "original_title_romanised": string
- "movie_banner": string
- "description": string
- "director": string
- "producer": string
- "release_date": string
- "running_time": string
- "rt_score": string
- "people": []
- "species": []
- "locations": []
- "vehicles": []
- "original_title": string
- "url": string

## Room
Таблица *favourite_film*
Поля:
- id
- title
- originalTitle
- description
- director
- producer
- releaseDate
- runningTime
- rtScore
Пользователь может добавлять и удалять фильмы в избранном.

## Тестирование

### Юнит-тесты

#### FilmViewModelTest – 7 тестов
- `initial state` – начальное состояние UI
- `loadAllFilms success updates films` – успешная загрузка списка фильмов
- `loadAllFilms network error message` – ошибка сети и отображение сообщения
- `retry after error` – повторная загрузка после ошибки
- `search with non-matching query` – поиск без результата
- `toggleFavourite adding same film twice` – добавление того же самого фильма дважды
- `refreshFavourites reloads favourite ids` – обновление статуса избранного

#### FilmDetailsViewModelTest – 3 теста
- `savedStateHandle correct filmId` – корректное чтение ID фильма
- `loadFilm network error then loads from favourites` – при ошибке сети данные подгружаются из избранного
- `loadFilm shows error` – ошибка сети + нет в избранном, показ ошибки

---

### Интеграционные тесты

#### FavouriteRepositoryTest (Repository + Room) – 3 теста
- `addToFavourites_returns_saved_film` – сохранение фильма в избранное
- `removeFromFavourites_removes_film` – удаление из избранного
- `addToFavourites_twice` – повторное добавление в избранное

#### UiTest - 1 тест
- `error_thenRetry_showsData` – ошибка -> нажатие Retry -> успешное состояние


## Скриншоты

|                     Loading                     |                     Error                     |                     List                     |                     Details                     |                     Favourites                     |                     Empty                     |
|:-----------------------------------------------:|:---------------------------------------------:|:--------------------------------------------:|:-----------------------------------------------:|:--------------------------------------------------:|:---------------------------------------------:|
| <img src="screenshots/loading.png" width="200"> | <img src="screenshots/error.png" width="200"> | <img src="screenshots/list.png" width="200"> | <img src="screenshots/details.png" width="200"> | <img src="screenshots/favourites.png" width="200"> | <img src="screenshots/empty.png" width="200"> |