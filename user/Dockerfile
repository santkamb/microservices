FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/user-v1-api.jar app.jar

EXPOSE 8289

ENTRYPOINT ["java","-jar","app.jar"]