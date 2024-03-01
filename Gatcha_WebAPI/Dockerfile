FROM openjdk:21
MAINTAINER amflinois
COPY target/Gatcha_WebAPI-0.0.1-SNAPSHOT.jar authAPI.jar
ENTRYPOINT ["java", "-jar", "/authAPI.jar"]
EXPOSE 8080/tcp
