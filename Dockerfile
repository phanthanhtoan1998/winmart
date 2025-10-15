# Stage 1: Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy pom files
COPY pom.xml .
COPY winmart-common/pom.xml winmart-common/
COPY winmart-service/pom.xml winmart-service/

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY winmart-common/src winmart-common/src
COPY winmart-service/src winmart-service/src

# Build the application
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the jar file from build stage
COPY --from=build /app/winmart-service/target/*.jar app.jar

# Expose port
EXPOSE 3333

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:3333/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

