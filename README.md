## Building

### Build and run a fat Jar
```
mvn install
```

The fat Jar is:
```
target/xmlJsonConverter-1.0-SNAPSHOT-runner.jar
```

### Build and run in Quarkus development mode:
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

### Build a linux native executable

```
./mvnw package -Pnative -Dnative-image.docker-build=true
```

### Build a tiny docker image
```
docker build -f src/main/docker/Dockerfile.tiny -t sns/quarkus-xml-json-converter-tiny .
```

### Run the tiny image:
```
docker run -i --rm sns/quarkus-xml-json-converter-tiny
```
 