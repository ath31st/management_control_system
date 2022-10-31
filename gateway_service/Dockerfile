FROM openjdk:19-alpine
COPY target/*.jar gateway-service.jar
EXPOSE 28888
ENTRYPOINT ["java", "-jar", "/gateway-service.jar"]