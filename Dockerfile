FROM eclipse-temurin:21-jre-alpine
RUN mkdir /auth_service
WORKDIR /auth_service
COPY target/AuthenticationService-0.0.1-SNAPSHOT.jar /auth_service
ENTRYPOINT java -jar /auth_service/AuthenticationService-0.0.1-SNAPSHOT.jar