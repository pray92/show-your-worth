FROM amazoncorretto:17
MAINTAINER Choi, Jisu <redgem92@gmail.com>

ARG JAR_FILE=build/libs/app.jar

COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java","-jar", "/app.jar"]