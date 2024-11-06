#usar una imagen base con Maven y OpenJDK 17
FROM maven:3.9.4-eclipse-temurin-17

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el código fuente y el pom.xml
COPY . .

# Construir el proyecto
RUN mvn clean package -DskipTests

# Exponer el puerto
EXPOSE 8080

# Ejecutar la aplicación
CMD ["java", "-jar", "target/frontend-0.0.1-SNAPSHOT.jar"]
