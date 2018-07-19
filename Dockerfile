FROM  java:openjdk-8-jre-alpine

MAINTAINER  Pedro Vicente Gómez Sánchez  <pedrovicente.gomez@gmail.com>

ADD target/scala-2.12/HaveANiceDay-assembly-1.6.0.jar /app/

WORKDIR /app

CMD java -jar /app/HaveANiceDay-assembly-1.6.0.jar