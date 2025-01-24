FROM maven:3.8.5-openjdk-17 AS build

LABEL maintainer = "Vadim Timofeev"
LABEL telegram = "@w0nder_waffle"
LABEL email = "timofeev.vadim.96@mail.ru"

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build target/wallet-api-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]