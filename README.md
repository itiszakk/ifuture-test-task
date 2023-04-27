# Тестовое задание для IFuture
Создание многопоточного сервиса и оценка его производительности

## Использованные технологии:
- Java 17
- Spring Framework (Boot, Data, Web)
- Gradle
- Docker
- Flyway
- PostgreSQL
- Lombok
- Guava
- JUnit 5
- Mockito

## API сервиса:
* Получение баланса c `id`: `GET http://localhost:8080/balance/{id}`
* Обновление баланса c `id` на `amount`: `PATCH http://localhost:8080/balance`
  * Тело запроса: `{"id": id, "amount": amount}`

## Способы запуска:
### Через IDE:
- Поднять базу данных при помощи `docker-compose up postgres`
- Запустить сервер `ServerApplication` из модуля `server`
- Запустить клиента `ClientApplication` из модуля `client`
### Через Docker Compose:
- Собрать проект при помощи `gradlew clean build`
- Собрать docker-образы при помощи `docker-compose build`
- Запустить контейнеры при помощи `docker-compose up`
- Остановить контейнеры при помощи `docker-compose stop`
- Удалить контейнеры при помощи `docker-compose down`