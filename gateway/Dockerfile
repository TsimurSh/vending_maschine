FROM amazoncorretto:17
USER root
WORKDIR /app
COPY ./build/libs/gateway.jar ./
EXPOSE 8443
CMD ["java", "-jar", "-Duser.timezone=Europe/Berlin", "-Dspring.profiles.active=production", "gateway.jar"]
