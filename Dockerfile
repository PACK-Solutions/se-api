# Build stage
FROM gradle:9.0.0-jdk21 AS build

WORKDIR /app

# Copy gradle wrapper and properties first for better caching
COPY gradle ./gradle
COPY gradlew gradle.properties ./
COPY settings.gradle.kts build.gradle.kts ./
COPY core/build.gradle.kts ./core/
COPY rest/build.gradle.kts ./rest/
COPY database/build.gradle.kts ./database/
COPY assembly/build.gradle.kts ./assembly/

# Download dependencies (this layer will be cached)
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY core/src ./core/src
COPY rest/src ./rest/src
COPY database/src ./database/src
#COPY assembly/src ./assembly/src

# Build the fat JAR without detekt and tests for faster Docker builds
RUN ./gradlew rest:buildFatJar --no-daemon -x test -x detekt

# Runtime stage
FROM eclipse-temurin:21-jre-alpine AS runtime

# Install wget for healthcheck and create non-root user
RUN apk add --no-cache wget && \
    addgroup -g 1001 appgroup && \
    adduser -u 1001 -G appgroup -s /bin/sh -D appuser

# Set working directory
WORKDIR /app

RUN wget -O dd-java-agent.jar 'https://dtdg.co/latest-java-tracer'

# Add metadata labels
LABEL maintainer="PACK Solutions" \
    description="Suite Epargne API" \
    version="1.0.0"
# TODO get version ?

# Copy the fat JAR from the build stage
COPY --from=build /app/rest/build/libs/app.jar /app/app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose the port the app runs on
EXPOSE 8080

# Add healthcheck
# HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=5 \
#  CMD wget -q --spider http://localhost:8080/api/persons || exit 1

# Set JVM options for containerized environment
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"

# Set the entrypoint
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -javaagent:dd-java-agent.jar -Ddd.logs.injection=true -Ddd.trace.sample.rate=1 -jar /app/app.jar"]
