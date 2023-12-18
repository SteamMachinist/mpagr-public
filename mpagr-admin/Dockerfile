FROM gradle:jdk20 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM docker:latest

RUN apk add curl
RUN set -ex && apk --no-cache add sudo
RUN sudo apk update
RUN sudo apk upgrade

RUN apk add --no-cache tzdata
ENV TZ=Europe/Moscow

RUN DOCKER_CONFIG=${DOCKER_CONFIG:-$HOME/.docker}
RUN mkdir -p $DOCKER_CONFIG/cli-plugins
RUN curl -SL https://github.com/docker/compose/releases/download/v2.20.3/docker-compose-linux-x86_64 -o $DOCKER_CONFIG/cli-plugins/docker-compose
RUN chmod +x $DOCKER_CONFIG/cli-plugins/docker-compose

RUN apk add -X http://dl-cdn.alpinelinux.org/alpine/edge/testing openjdk20

EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/mpagr-admin.jar
WORKDIR /app
COPY src/main/resources /app/resources
ENTRYPOINT ["java","-jar","/app/mpagr-admin.jar"]