FROM openjdk:17

WORKDIR /usr/app

COPY ./ ./

RUN ./mvnw -f pom.xml compile
CMD ./mvnw -f pom.xml liberty:run