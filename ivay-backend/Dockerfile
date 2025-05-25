# -------- STAGE 1: Build the JAR using Maven --------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /build

# Copy pom.xml and download dependencies (cache optimization)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the full source code
COPY src ./src

# Build the JAR (skipping tests)
RUN mvn clean package -DskipTests

# -------- STAGE 2: Run the built JAR --------
FROM openjdk:17

# Set working directory
WORKDIR /app

# Copy wait-for-it script and make it executable
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copy the JAR built in the previous stage
COPY --from=builder /build/target/ivay-backend-0.0.1-SNAPSHOT.jar .

# Wait for MySQL to be ready, then start the Spring Boot app
ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--timeout=60", "--", "java", "-jar", "ivay-backend-0.0.1-SNAPSHOT.jar"]

# Expose application port
EXPOSE 8081
