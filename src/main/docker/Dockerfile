FROM openjdk:8-jdk-alpine
MAINTAINER Aleksander Kolata <olek.tbg.konta@gmail.com>

# This Dockerfile is used with gradle task

VOLUME /tmp
ADD target/meetingorganizer-0.1.0.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar