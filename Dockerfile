# ---------- Stage 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Resolve dependencies first so this layer is cached until pom.xml changes
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:17-jre-jammy AS runtime

LABEL org.opencontainers.image.title="calculator-api" \
      org.opencontainers.image.source="https://github.com/vaishak-iiitb/calc_spe"

RUN useradd -r -u 1001 -d /app app
WORKDIR /app
COPY --from=build --chown=app:app /workspace/target/calculator-api.jar app.jar

USER app
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -fsS http://127.0.0.1:8080/actuator/health | grep -q UP || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
