package acceptance.converter;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;


public class ConvertToXmlShould extends ConverterShould {

    private static final String INPUT_TOPIC = "jsonInput";
    private static final String OUTPUT_TOPIC = "xmlOutput";

    @Override
    protected Map<String, String> createCustomEnvProperties() {
        Map<String, String> envProperties = new HashMap<>();
        envProperties.put("INPUT_KAFKA_TOPIC", getInputTopic());
        envProperties.put("OUTPUT_KAFKA_TOPIC", getOutputTopic());
        envProperties.put("APP_NAME", this.getClass().getName());
        envProperties.put("MODE", "jsonToXml");
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
    public void convertsAnyJsonToXml() throws ExecutionException, InterruptedException {
        writeJsonToInputTopic();

        assertKafkaXmlMessage();
    }

    private void writeJsonToInputTopic() throws ExecutionException, InterruptedException {
        new KafkaProducer<String, String>(getProperties()).send(createKafkaProducerRecord(()-> createJsonMessage())).get();
    }

    private void assertKafkaXmlMessage() {
        ConsumerRecords<String, String> recs = pollForResults();
        assertFalse(recs.isEmpty());

        Spliterator<ConsumerRecord<String, String>> spliterator = Spliterators.spliteratorUnknownSize(recs.iterator(), 0);
        Stream<ConsumerRecord<String, String>> consumerRecordStream = StreamSupport.stream(spliterator, false);
        Optional<ConsumerRecord<String, String>> expectedConsumerRecord = consumerRecordStream.filter(cr -> foundExpectedRecord(cr.key()))
                                                                                              .findAny();
        expectedConsumerRecord.ifPresent(cr -> assertRecordValueXml(cr));
        if (!expectedConsumerRecord.isPresent())
            fail("Did not find expected record");
    }

    private void assertRecordValueXml(ConsumerRecord<String, String> consumerRecord) {
        String value = consumerRecord.value();
        String expectedValue = createXmlMessage();
        XmlAssert.assertThat(expectedValue).and(value).ignoreWhitespace().areIdentical();
    }
}
