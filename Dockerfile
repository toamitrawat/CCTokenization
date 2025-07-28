# -------- Stage 1: Build the application --------
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Set working directory inside builder container
WORKDIR /app

# Copy source code into container
COPY . .

# Build the application and skip tests (optional)
RUN mvn clean package -DskipTests


# -------- Stage 2: Run the application --------
FROM openjdk:21-jdk-slim

# Set working directory in runtime container
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/tokenization-0.0.1-SNAPSHOT.jar app.jar

# Copy SSL keystore
COPY --from=build /app/src/main/resources/keystore.p12 keystore.p12

# Expose ports
EXPOSE 8080
EXPOSE 8443

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]