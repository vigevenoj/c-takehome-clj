FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/ctakehome.jar /ctakehome/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/ctakehome/app.jar"]
