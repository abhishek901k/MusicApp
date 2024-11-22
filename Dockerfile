# Stage 1: Build the application using Gradle
FROM gradle:8.10-jdk17 AS build
WORKDIR /home/app
COPY build.gradle settings.gradle ./
RUN gradle --no-daemon dependencies
# First 6 steps will run only when we will change dependency, otherwise will be used from cache

# Next 2 steps will run only when some changes are made in src
COPY src src
RUN gradle --no-daemon build -x test

# Stage 2: Create the final image
FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /home/app/build/libs/music-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]