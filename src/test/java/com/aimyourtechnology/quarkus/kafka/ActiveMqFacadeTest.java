package com.aimyourtechnology.quarkus.kafka;

import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import io.smallrye.reactive.messaging.kafka.MessageHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;

class ActiveMqFacadeTest implements KafkaMessage<String,String> {

    private String orderId = generateRandomString();
    private String traceyId = generateRandomString();


    private String generateRandomString() {
        return String.valueOf(new Random().nextLong());
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void incomingMqMessageIntoOutgoing() {
        KafkaMessage<String, String> result = ActiveMqFacade.invoke(this);
        assertJsonEquals(createExpectedJsonMessage(), result.getPayload());
    }

    @Override
    public String getPayload() {
        return createMqConnectorMessage();
    }

    String createExpectedJsonMessage() {
        return String.format("{\"order\":{\"orderId\":\"%s\"" +
                        "},\"traceId\":\"%s\"}",
                orderId, traceyId, "${json-unit.ignore}"
        );
    }

    private String createMqConnectorMessage() {
        return String.format("{"
                + "\"ORDER_ID\":\"%s\","
                + "\"TRACEY_ID\":\"%s\","
                + "\"XML\":\"%s\""
                + "}", orderId, traceyId, createEscapedXmlMessage());
    }

    private String createEscapedXmlMessage() {
        return String.format(
                "<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>" +
                        "<order>" +
                        "<orderId>%s</orderId>" +
                        "</order>", orderId
        );
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getTopic() {
        return null;
    }

    @Override
    public Integer getPartition() {
        return null;
    }

    @Override
    public Long getTimestamp() {
        return null;
    }

    @Override
    public MessageHeaders getHeaders() {
        return null;
    }
}