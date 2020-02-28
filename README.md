# Ingress Uptime
This project provides a solution for a simple health/ping response check. 
This is a spring-boot application and uses its actuator dependency.

### Usage:


### Build:
```
mvn clean package
```

### Run:
```
java -jar target/<artifact>.jar
```

### Running as a docker container:
```
docker build -t <image_tag> . && docker run -p 8080:8080 <image_tag>
```