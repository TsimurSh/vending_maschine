FROM amazoncorretto:17
WORKDIR /app
COPY ./build/libs/machine.jar ./
CMD ["java", "-jar", "-Duser.timezone=Europe/Berlin", "-Dspring.profiles.active=production", "machine.jar"]
