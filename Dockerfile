FROM frolvlad/alpine-oraclejdk8:slim
ENV SERVICE_NAME=zuul
ENV CONSUL_SERVER=192.168.178.19
ENV CONFIG_SERVER=192.168.178.19

EXPOSE 8003
ADD build/libs/zuul-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=production","/app.jar"]
