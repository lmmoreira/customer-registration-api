FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd -ms /bin/bash appuser

COPY target/customer-0.0.1-SNAPSHOT /app/app.jar

RUN chown -R appuser:appuser /app

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]