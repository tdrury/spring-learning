FROM openjdk:8-jdk
VOLUME /tmp

ARG docker_artifact_file=app.jar

RUN groupadd -r learners && useradd -r -g learners learn
RUN cat /etc/passwd
RUN cat /etc/group

WORKDIR /home/learn

ADD ${docker_artifact_file} app.jar
RUN chown -R learn:learners /home/learn
RUN ls -al

USER learn:learners
RUN pwd
RUN ls -al

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Xmx128m -Xss256k -Djava.security.egd=file:/dev/./urandom -jar app.jar