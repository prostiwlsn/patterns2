FROM maven:3.8.7-openjdk-17 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=builder /app/target/*.jar gateway.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "gateway.jar"]