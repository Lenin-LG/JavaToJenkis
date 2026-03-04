# Usa una imagen base de Java
FROM eclipse-temurin:21-jdk-slim

# Directorio de la app en el contenedor
WORKDIR /app

# Copia los archivos de tu proyecto
COPY . .

# Construye tu proyecto con Maven
RUN ./mvnw clean package || mvn clean package

# Define el comando para ejecutar tu app
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
