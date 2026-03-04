FROM openjdk:17-jdk-slim
LABEL maintainer="EvgenyEVS <evis10@yandex.ru>"

WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]