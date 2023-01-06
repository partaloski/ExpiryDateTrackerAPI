FROM openjdk:11
MAINTAINER ppartaloski@yahoo.com
EXPOSE 9091
COPY target/ExpiryDateTrackerAPI-0.0.1-SNAPSHOT.jar edt-api-1.0.0.jar
ENTRYPOINT ["java","-jar","/edt-api-1.0.0.jar"]