FROM openjdk:17

WORKDIR /usr/app

COPY ./ ./

RUN ./mvnw -f pom.xml clean compile
CMD ./mvnw -f pom.xml liberty:run