FROM openjdk:17
WORKDIR /app

COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copia el JAR con su nombre original (sin renombrar)
COPY target/ivay-backend-0.0.1-SNAPSHOT.jar .

# Usa directamente ese JAR en la ejecución
ENTRYPOINT ["/wait-for-it.sh", "mysql:3306", "--timeout=60", "--", "java", "-jar", "ivay-backend-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081