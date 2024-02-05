FROM eclipse-temurin:21.0.2_13-jre-alpine
VOLUME /tmp
COPY target/intervenant-service-1.0.jar intervenant-service.jar
ENTRYPOINT ["java", "-jar", "/intervenant-service.jar"]