### Wallet-API

---

`Documentation` (available only when the application is running)
* [OpenAPI description](http://localhost:8080/api/v1/api-docs)
* [Swagger UI](http://localhost:8080/api/v1/swagger-ui)


`Endpoints`
- POST "/api/v1/wallet/{id}" Making a transaction with the wallet balance
- GET "/api/v1/wallet/{id}" Getting the wallet balance

`Stack`
- Java 17
- Docker-compose
- Spring boot 3 (Data JPA/Validation/Web/Cloud)
- Spring Retry
- Postgres
- Liquibase

`Особенности`
- Использование оптимистичной блокировки для сохранения консистентности данных при 
большом количестве параллельных обновлений на один и тот же ресурс
- Retry для повышения отказоустойчивости приложения

`Testing`
- уровень контроллеров, сервисов, конвертеров покрыты тестами
- произведено нагрузочное тестирование 1000 RPS с помощью 

`Локальный запуск / Quick start`
1. Склонировать репозиторий:
```bash
git clone 
cd wallet-api 
```
2. Выполнить команду в терминале:
```bash
docker-compose up --build -d
```