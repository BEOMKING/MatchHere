FROM openjdk:11
MAINTAINER ian.ilminmoon@gmail.com
#ARG JAR_FILE=build/libs/*.jar
ARG JAR_FILE=*.jar

COPY ${JAR_FILE} app.jar

#ENTRYPOINT ["run.sh"]
ENTRYPOINT ["java", "-jar", "/app.jar"]
