## Building

### Build and run a fat Jar
mvn compile quarkus:dev

### Build a native executable (Mac)
_Ensure that you have Graal VM RC16 (CE is fine!) on the path._
```
mvn package -Pnative
```
The native image is:
```
target/xmlJsonConverter-1.0-SNAPSHOT-runner
```

### Build a docker image with a native executable

Build the linux native executable:
```
./mvnw package -Pnative -Dnative-image.docker-build=true
```

Build an image with the native executable:
```
docker build -f src/main/docker/Dockerfile.native -t sns/quarkus-xml-json-converter .
```

Run the image:
```
docker run -i --rm sns/quarkus-xml-json-converter
```

### Build a tiny docker image
```
docker build -f src/main/docker/Dockerfile.tiny -t sns/quarkus-xml-json-converter-tiny .
```

Run the tiny image:
```
docker run -i --rm sns/quarkus-xml-json-converter-tiny
```
 