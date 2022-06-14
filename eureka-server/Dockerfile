FROM amazoncorretto:17
USER root
WORKDIR /app
COPY ./build/libs/eureka-server.jar ./
RUN echo "📡 Eureka server 📡"
CMD ["java", "-jar", "-Duser.timezone=Europe/Berlin", "-Dspring.profiles.active=production", "eureka-server.jar"]
