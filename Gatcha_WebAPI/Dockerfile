FROM maven:3.9.6-amazoncorretto-21 as build
RUN mkdir -p /authentication-api
WORKDIR /authentication-api
COPY pom.xml /authentication-api
COPY src /authentication-api/src
RUN mvn -f pom.xml clean package

FROM openjdk:21
MAINTAINER amflinois
COPY target/Gatcha_WebAPI-0.0.1-SNAPSHOT.jar authAPI.jar
ENTRYPOINT ["java", "-jar", "/authAPI.jar"]
EXPOSE 8080/tcp
