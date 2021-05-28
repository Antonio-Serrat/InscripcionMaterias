FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY target/I.M-0.0.1-SNAPSHOT.jar inscipcionmaterias.jar
EXPOSE 8088
ENTRYPOINT exec java $JAVA_OPTS -jar inscipcionmaterias.jar
# For Spring-Boot project, use the entrypoint below to reduce Tomcat startup time.
#ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar inscipcionmaterias.jar
