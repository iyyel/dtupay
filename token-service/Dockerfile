FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/token-service-1.0.0-runner.jar /usr/src
EXPOSE 8080/tcp
CMD java -Xmx64m \
-jar token-service-1.0.0-runner.jar