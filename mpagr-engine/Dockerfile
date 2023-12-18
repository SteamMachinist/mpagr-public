FROM gradle:jdk20 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:20-jdk
ENV TZ=Europe/Moscow

RUN apt -y update
RUN apt install -y build-essential

EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/mpagr-engine.jar
WORKDIR /app
COPY src/main/resources /app/resources
ENTRYPOINT ["java","-jar","/app/mpagr-engine.jar"]