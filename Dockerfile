FROM maven:3.9.6-amazoncorretto-21 as build
RUN mkdir -p /auth-api
WORKDIR /auth-api
COPY Gatcha_WebAPI/pom.xml /auth-api
COPY Gatcha_WebAPI/src /auth-api/src
RUN mvn -f pom.xml clean package

FROM openjdk:21
MAINTAINER amflinois
COPY --from=build /auth-api/target/*.jar authAPI.jar
ENTRYPOINT ["java", "-jar", "/authAPI.jar"]
EXPOSE 8080/tcp
