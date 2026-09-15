FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY . .
RUN chmod +x mvn
RUN ./mvn clean package -DskipTests -B
ENTRYPOINT ["java", "-jar", "/app/target/helpdesk-api-0.0.1-SNAPSHOT.jar"]

