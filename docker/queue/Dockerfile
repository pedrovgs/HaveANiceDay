FROM       openjdk:8
MAINTAINER Karumi


ADD  https://s3-eu-west-1.amazonaws.com/softwaremill-public/elasticmq-server-0.13.8.jar /elasticmq/elasticmq-server-0.13.8.jar
RUN  chmod +x /elasticmq/elasticmq-server-0.13.8.jar

EXPOSE 9324

ENTRYPOINT ["java", "-jar", "/elasticmq/elasticmq-server-0.13.8.jar"]
