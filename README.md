# Ingress Uptime
This is a spring-boot application that does a simple request/response check for Ingresses in a kubernetes cluster.
The application works in tandom with [controller.uptime](https://github.com/dolittle-entropy/controller.uptime)

The app has to run in a Kubernetes cluster or minikube.

It uses the Kubernetes API to compile a list of ingresses with a preconfigured label.
The preconfigured label is set in application.properties
```
uptime.monitor.ingress-selector=myLabel=PingMe
```


### Usage:
In [controller.uptime](https://github.com/dolittle-entropy/controller.uptime) the url path is set in the ingress to which this application 
responds to.

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