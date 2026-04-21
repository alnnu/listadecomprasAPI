FROM maven
WORKDIR /usr/src/mymaven
COPY ./ .
ENTRYPOINT ["mvn", "spring-boot:run"]
