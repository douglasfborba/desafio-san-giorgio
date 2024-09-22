FROM openjdk:17-jdk-alpine
EXPOSE 8080
EXPOSE 4566
COPY san_giorgio-api/build/libs/san_giorgio-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]