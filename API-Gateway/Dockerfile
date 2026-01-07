FROM eclipse-temurin:17-jre

WORKDIR /api-gateway

COPY target/api-gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]