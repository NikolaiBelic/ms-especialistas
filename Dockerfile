# Fase de construcción
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# 1. Copia los archivos necesarios para Gradle (evita copiar archivos innecesarios)
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .
COPY src/ src/

# Da permisos de ejecución a gradlew
RUN chmod +x ./gradlew

# 2. Construye el JAR (esto ejecuta './gradlew bootJar' dentro del contenedor)
RUN ./gradlew clean bootJar

# Fase de ejecución
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# 3. Copia SOLO el JAR generado desde la fase 'builder'
COPY --from=builder /app/build/libs/ms-especialistas-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]