# ==============================================================================
# SE API - Optimized Multi-Stage Dockerfile
# ==============================================================================
# Production-hardened build with security best practices
# - Minimal attack surface with distroless final stage
# - Non-root user execution
# - Build cache optimization
# - Security scanning friendly
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Stage
# ------------------------------------------------------------------------------
FROM gradle:9.0.0-jdk21-alpine AS build

WORKDIR /app

# Copy Gradle configuration files first (better caching)
COPY gradle ./gradle
COPY gradlew gradle.properties settings.gradle.kts build.gradle.kts ./
COPY buildSrc ./buildSrc

# Copy module build files
COPY core/build.gradle.kts ./core/
COPY rest/build.gradle.kts ./rest/
COPY database/build.gradle.kts ./database/
COPY assembly/build.gradle.kts ./assembly/

# Download dependencies (cached layer)
RUN ./gradlew dependencies --no-daemon --no-configuration-cache

# Copy source code
COPY core/src ./core/src
COPY rest/src ./rest/src
COPY database/src ./database/src
COPY assembly/src ./assembly/src

# Build fat JAR (skip tests and detekt for faster Docker builds)
# Tests and quality gates run in CI/CD pipeline
RUN ./gradlew assembly:buildFatJar --no-daemon --no-configuration-cache -x test -x detekt

# ------------------------------------------------------------------------------
# Stage 2: Runtime Stage (Distroless)
# ------------------------------------------------------------------------------
# Using distroless java21-debian12 for minimal attack surface
FROM gcr.io/distroless/java21-debian12:nonroot AS runtime

# Metadata labels
ARG APP_VERSION="unknown"
ARG BUILD_DATE
ARG VCS_REF

LABEL org.opencontainers.image.title="SE API" \
      org.opencontainers.image.description="Suite Epargne API - Connaissance Client" \
      org.opencontainers.image.version="${APP_VERSION}" \
      org.opencontainers.image.created="${BUILD_DATE}" \
      org.opencontainers.image.revision="${VCS_REF}" \
      org.opencontainers.image.vendor="PACK Solutions" \
      maintainer="PACK Solutions"

# Set working directory
WORKDIR /app

# Copy the fat JAR from build stage
COPY --from=build --chown=nonroot:nonroot /app/assembly/build/libs/app.jar /app/app.jar

# Expose application port
EXPOSE 8080

# Set JVM options for containerized environment
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=100 \
    -XX:+UseStringDeduplication \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom"

# Health check (distroless doesn't have curl/wget, using Java)
# Kubernetes will use HTTP probes instead

# Run as non-root user (distroless nonroot user: 65532)
USER nonroot:nonroot

# Entrypoint
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
