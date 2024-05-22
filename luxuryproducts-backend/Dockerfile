# Gebruik een basis image met Java 21
FROM openjdk:21-jdk-slim

# Stel een werkdirectory in
WORKDIR /app

# Kopieer de jar file naar de container
COPY ./target/GameWebshop-0.0.1-SNAPSHOT.jar /app/app.jar

# Exposeer de poort waarop de applicatie draait
EXPOSE 8080

# Start de applicatie wanneer de container start
CMD ["java", "-jar", "app.jar"]