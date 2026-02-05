# Use Java 17
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy all project files
COPY . .

# Give execute permission to mvnw (THIS FIXES ERROR 126)
RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/financeapp-0.0.1-SNAPSHOT.jar"]