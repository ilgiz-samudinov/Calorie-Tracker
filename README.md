# Calorie Tracker API

Простой **REST API**, который помогает отслеживать потребление калорий, управлять приемами пищи и ставить цели (например, потеря веса или набор массы).

## Что это?

Этот проект позволяет пользователям:
- Создавать профили с информацией (возраст, вес, рост и цель).
- Рассчитывать норму калорий на день в зависимости от цели.
- Добавлять приемы пищи и отслеживать, сколько калорий было съедено.
- Генерировать отчеты по ежедневному потреблению калорий.

## Технологии

- **Java** (Spring Boot)
- **PostgreSQL** (для хранения данных)
- **Maven** (для сборки)
- **Postman** (для тестирования)

## Как запустить?

1. **Клонируйте репозиторий:**
   ```bash
   git clone https://github.com/ilgiz-samudinov/Calorie-Tracker.git
   ```

2. **Настройте PostgreSQL:**
   - Создайте базу данных `calorie_tracker`.
   - Обновите настройки подключения в `application.properties`.

3. **Соберите и запустите приложение:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   Приложение будет доступно по адресу: `http://localhost:8080`

## Основные эндпоинты

- **POST** `/api/users` — создать пользователя.
- **GET** `/api/users/{id}` — получить данные пользователя.
- **PUT** `/api/users/{id}` — обновить информацию о пользователе.
- **POST** `/api/meals` — добавить прием пищи.
- **GET** `/api/users/report/daily/{id}` — получить отчет о калориях за день.
- **GET** `/api/users/report/calories-check/{id}` — проверить калории для пользователя на определенную дату.
- **GET** `/api/users/report/history/{id}` — получить отчет о калориях за период (от startDate до endDate).

## Примеры запросов:

- **Создать пользователя:**
  ```json
  {
    "name": "Ilgiz Samudinov",
    "email": "ilgiz.samudinov@example.com",
    "age": 21,
    "weight": 55.5,
    "height": 170.0,
    "goal": "WEIGHT_LOSS"
  }
  ```

- **Получить пользователя по ID:**
  GET запрос на `/api/users/1`

- **Обновить данные пользователя:**
  PUT запрос на `/api/users/1` с новым телом:
  ```json
  {
    "name": "Ilgiz Samudinov",
    "email": "ilgiz.samudinov@example.com",
    "age": 22,
    "weight": 57.0,
    "height": 170.0,
    "goal": "MAINTENANCE"
  }
  ```

- **Получить отчет по калориям за день:**
  GET запрос на:
  ```http
  http://localhost:8080/api/users/report/daily/1?date=2025-03-28
  ```

- **Проверить калории для пользователя:**
  GET запрос на:
  ```http
  http://localhost:8080/api/users/report/calories-check/1?date=2025-03-28
  ```

- **Получить отчет о калориях за период:**
  GET запрос на:
  ```http
  http://localhost:8080/api/users/report/history/1?startDate=2025-03-28&endDate=2025-03-29
  ```
