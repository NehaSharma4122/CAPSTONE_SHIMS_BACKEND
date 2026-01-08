FROM eclipse-temurin:17-jre

WORKDIR /hospital-management-service

COPY target/HospitalManagementService-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]