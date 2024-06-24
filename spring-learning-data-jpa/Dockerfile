FROM sapmachine:21-jdk-ubuntu
VOLUME /tmp

# name of the fat springboot jar/war to run (no path allowed!). Developer build will override this value
# (see dockerfile-maven-plugin configuration). CI build will not define it so it will default to app.jar.
ARG JAR_FILE=app.jar

ADD target/${JAR_FILE} app.jar

ENV JAVA_OPTS=""
EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -Xmx128m -Xss256k -Djava.security.egd=file:/dev/./urandom -jar app.jar