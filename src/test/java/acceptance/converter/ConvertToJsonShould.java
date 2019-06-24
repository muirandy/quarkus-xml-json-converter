package acceptance.converter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class ConvertToJsonShould extends ConverterShould {

    private static final String INPUT_TOPIC = "xmlInput";
    private static final String OUTPUT_TOPIC = "jsonOutput";

    @Override
    protected Map<String, String> createCustomEnvProperties() {
        Map<String, String> envProperties = new HashMap<>();
        envProperties.put("INPUT_KAFKA_TOPIC", getInputTopic());
        envProperties.put("OUTPUT_KAFKA_TOPIC", getOutputTopic());
        envProperties.put("APP_NAME", this.getClass().getName());
        envProperties.put("MODE", "xmlToJson");
        return envProperties;
    }

    @Override
    public String getInputTopic() {
        return INPUT_TOPIC;
    }

    @Override
    protected String getOutputTopic() {
        return OUTPUT_TOPIC;
    }


    @Test
    public void convertsAnyXmlToJson() throws ExecutionException, InterruptedException {
        writeXmlToInputTopic();

        assertKafkaJsonMessage();
    }

    private void writeXmlToInputTopic() throws InterruptedException, ExecutionException {
        new KafkaProducer<String, String>(getProperties()).send(createKafkaProducerRecord(()->createXmlMessage())).get();
    }

    private void assertKafkaJsonMessage() {
        ConsumerRecords<String, String> recs = pollForResults();
        assertFalse(recs.isEmpty());

        Spliterator<ConsumerRecord<String, String>> spliterator = Spliterators.spliteratorUnknownSize(recs.iterator(), 0);
        Stream<ConsumerRecord<String, String>> consumerRecordStream = StreamSupport.stream(spliterator, false);
        Optional<ConsumerRecord<String, String>> expectedConsumerRecord = consumerRecordStream.filter(cr -> foundExpectedRecord(cr.key()))
                                                                                              .findAny();
        expectedConsumerRecord.ifPresent(cr -> assertRecordValueJson(cr));
        if (!expectedConsumerRecord.isPresent())
            fail("Did not find expected record");
    }

    private void assertRecordValueJson(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        String expectedValue = createJsonMessage();
        assertJsonEquals(expectedValue, value);
    }
}
