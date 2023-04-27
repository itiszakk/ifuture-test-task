FROM amazoncorretto:17
EXPOSE 8080
VOLUME /server
ADD build/libs/server-1.0-SNAPSHOT.jar server.jar
ENTRYPOINT ["java", "-jar", "/server.jar"]