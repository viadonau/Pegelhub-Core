FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY ./target/pegelhub-tstp-connector*.jar /app/connector.jar

CMD java -jar /app/connector.jar