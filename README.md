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
- Spring Boot 3 (Data JPA/Validation/Web/Cloud)
- Spring Retry
- Postgres
- Liquibase

`Features`
- Using _pessimistic_ locking to maintain data consistency with
  a large number of parallel updates to the same resource
- Retry to increase the fault tolerance of the application

`Testing`
- the level of controllers, services, converters are covered by tests
- performed load testing of 1000 RPS at the endpoint of the wallet balance change using `Gatling`

![](http://postimg.su/image/UiNJCF19/galting_pessimistic_lock_test.png)

`Quick start`
1. Clone a repository:
```bash
git clone https://github.com/timofeev-vadim-96/wallet-api
cd wallet-api
```
2. Run the command in the terminal:
```bash
docker-compose up --build -d
```