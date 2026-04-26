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

## Скриншоты

|                     Loading                     |                     Error                     |                     List                     |                     Details                     |                     Favourites                     |                     Empty                     |
|:-----------------------------------------------:|:---------------------------------------------:|:--------------------------------------------:|:-----------------------------------------------:|:--------------------------------------------------:|:---------------------------------------------:|
| <img src="screenshots/loading.png" width="200"> | <img src="screenshots/error.png" width="200"> | <img src="screenshots/list.png" width="200"> | <img src="screenshots/details.png" width="200"> | <img src="screenshots/favourites.png" width="200"> | <img src="screenshots/empty.png" width="200"> |