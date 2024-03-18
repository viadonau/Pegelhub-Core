FROM amazoncorretto:17-alpine-full
#Installing prerequisites
RUN apk --no-cache add curl

# Create user and group to downgrade privileges
RUN addgroup -S coreapp && adduser -S coreapp -G coreapp
USER coreapp:coreapp

# RabbitMQ connection
ENV RABBIT_URL=rabbit
ENV RABBIT_USER=""
ENV RABBIT_PASSWORD=""

# Registry configuration
ENV SERVICE_REGISTRY_HOST=registry
ENV SERVICE_REGISTRY=http://$SERVICE_REGISTRY_HOST:8090/eureka
ENV APP_PORT=8080

# Expose configured port
EXPOSE $APP_PORT

WORKDIR /var/lib/application

ADD ./target/app.jar .

CMD java -jar /var/lib/application/app.jar

HEALTHCHECK --interval=30s --timeout=2s --retries=5 --start-period=40s \
  CMD curl --fail --silent localhost:8080/actuator/health | grep UP || exit 1