# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /workspace

# Cache dependencies first
COPY pom.xml ./pom.xml
RUN mvn -B -ntp -DskipTests dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn -B -ntp -DskipTests clean package && \
    JAR_FILE=$(ls target/*.jar | grep -v "original" | head -n 1) && \
    cp "$JAR_FILE" target/app.jar

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /workspace/target/app.jar ./app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
