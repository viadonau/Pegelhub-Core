FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY target/app.jar /app/

COPY src/main/java/org/example/libRevPiReader.so /usr/lib/

RUN touch /usr/lib/libRevPiReader.so

ENV LD_LIBRARY_PATH=/usr/lib

CMD ["java", "-jar", "app.jar"]