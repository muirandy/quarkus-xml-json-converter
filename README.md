# Purpose
An app to read XML from a kafka topic, convert to Json, produce to another kafka topic.
Uses [Quarkus](https://quarkus.io/).

## Environment
You need:
 * a Kafka broker running in a place that this application can access it
 * something to write some XML onto the "input" topic.
 
You can find a suitable environment [here](https://github.com/muirandy/sns2-system-tests), although its far more that needed. 

This is a different attempt at implementing [this project](https://github.com/muirandy/graal-kafka-xml-json-converter), but without Kafka Streams.

## Building

### Build and run a fat Jar
```
mvn install
```

The fat Jar is:
```
target/xmlJsonConverter-1.0-SNAPSHOT-runner.jar
```

Run the fat jar:
```
java -Xmx48M -jar target/xmlJsonConverter-1.0-SNAPSHOT-runner.jar
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
docker run -i --rm --network=sns2-system-tests_default -e APP_NAME=linuxXmlToJsonConverter -e MODE=xmlToJson -e KAFKA_BROKER_SERVER=sns2-system-tests_kafka01.internal-service_1 -e KAFKA_BROKER_PORT=9092 -e INPUT_KAFKA_TOPIC=incoming.op.msgs -e OUTPUT_KAFKA_TOPIC=modify.op.msgs sns/quarkus-xml-json-converter-tiny
```
 