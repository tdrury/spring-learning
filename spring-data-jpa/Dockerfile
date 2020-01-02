FROM openjdk:8-jre
VOLUME /tmp

# name of the fat springboot jar/war to run (no path allowed!). Developer build will override this value
# (see dockerfile-maven-plugin configuration). CI build will not define it so it will default to app.jar.
ARG JAR_FILE=app.jar

ADD target/${JAR_FILE} app.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -Xmx128m -Xss256k -Djava.security.egd=file:/dev/./urandom -jar app.jar