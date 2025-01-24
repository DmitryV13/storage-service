FROM amazoncorretto:17
LABEL authors="dmitry voicu 2025"
WORKDIR /app
COPY target/storageservice-0.0.1-SNAPSHOT.jar /app/storage-service.jar
ENTRYPOINT ["java", "-jar", "storage-service.jar"]