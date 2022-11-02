FROM openjdk:19-alpine
COPY target/*.jar backend-service.jar
EXPOSE 28889
ENTRYPOINT ["java", "-jar", "/backend-service.jar"]