# Imagen base Java
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiar el jar ya compilado
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# ejecutar la app
ENTRYPOINT ["java","-jar","app.jar"]
