# Use an official OpenJDK image
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/popcorn-palace-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port (change it if needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]