# Use a lightweight OpenJDK image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file
COPY target/tokenization-0.0.1-SNAPSHOT.jar app.jar


# Expose HTTP and HTTPS ports
EXPOSE 8080
EXPOSE 8443

# Copy SSL keystore from resources
COPY src/main/resources/keystore.p12 keystore.p12

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
