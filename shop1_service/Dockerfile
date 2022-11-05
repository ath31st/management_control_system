FROM openjdk:19-alpine
COPY target/*.jar shop1-service.jar
EXPOSE 28880
ENTRYPOINT ["java", "-jar", "/shop1-service.jar"]