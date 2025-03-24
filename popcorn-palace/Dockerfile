# Use an official maven image
FROM maven:3.9.9-eclipse-temurin-21-alpine

# Set the working directory inside the container
WORKDIR /project

COPY . .

RUN mvn clean package

# Copy the application JAR file to the container
RUN mkdir /app && cp target/*.jar /app/app.jar

WORKDIR /app
# Expose the application port (change it if needed)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]