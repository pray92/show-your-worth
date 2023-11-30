FROM amazoncorretto:17
MAINTAINER Choi, Jisu <redgem92@gmail.com>

ARG JAR_FILE=build/libs/app.jar

COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "-Dspring.datasource.url=jdbc:mysql://${DB_ADDRESS}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8", "-Dspring.datasource.username=${DB_USERNAME}", "-Dspring.datasource.password=${DB_PASSWORD}", "-Dspring.session.timeout=${SESSION_TIMEOUT}", "-Dredis.session.master.node=${REDIS_SESSION_MASTER_NODE}", "-Dredis.session.password=${REDIS_NODE_PASSWORD}", "-Dcloud.credentials.access-key=${CLOUD_ACCESS_KEY}", "-Dcloud.credentials.secret-key=${CLOUD_SECRET_KEY}", "-Dcloud.storage.endpoint=${STORAGE_END_POINT}", "-Dcloud.storage.region.static=${STORAGE_REGION}", "-Dcloud.storage.bucket-name=${STORAGE_BUCKET_NAME}", "/app.jar"]
