FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY ./target/pegelhub-iec-connector-1.0-SNAPSHOT-jar-with-dependencies.jar /app/connector.jar

CMD java -jar /app/connector.jar