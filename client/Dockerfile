FROM amazoncorretto:17
VOLUME /client
ADD build/libs/client-1.0-SNAPSHOT.jar client.jar
ENTRYPOINT ["java", "-jar", "/client.jar"]