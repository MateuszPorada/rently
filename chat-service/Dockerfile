FROM maven:3.8.6-openjdk-18
WORKDIR /test
USER root
COPY . .
CMD chmod 777 pom.xml
CMD ls
RUN mvn clean install -DskipTests
CMD mvn spring-boot:run