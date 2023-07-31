ARG REGISTRY_URI
FROM ${REGISTRY_URI}/openjdk:open-11.0.14.1-jdk

ARG VERSION=1.0-SNAPSHOT
COPY build/libs/com.app.booking-${VERSION}.jar booking.jar
COPY healthcheck.sh healthcheck.sh
COPY src/main/resources/application.yml application.yml

EXPOSE 8080

ENTRYPOINT ["/bin/sh", "-c" , "echo 127.0.0.1 $HOSTNAME >> /etc/hosts && java -jar booking.jar application.yml"]