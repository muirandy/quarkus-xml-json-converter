package com.aimyourtechnology.quarkus.kafka;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConverterApp {

    private static final String XML_TOPIC = "incoming-op-msgs";
    private static final String JSON_TOPIC = "modify-op-msgs";

    @ConfigProperty(name = "mode")
    String mode;

    @Incoming(XML_TOPIC)
    @Outgoing(JSON_TOPIC)
    @Broadcast
    public KafkaMessage<String, String> process(KafkaMessage<String, String> message) {
        if (mode.equalsIgnoreCase("xmlToJson"))
            return KafkaMessage.of(message.getKey(), XmlJsonConverter.convertXmlToJson(message.getPayload()));
        if (mode.equalsIgnoreCase("jsonToXml"))
            return KafkaMessage.of(message.getKey(), XmlJsonConverter.convertJsonToXml(message.getPayload()));
        if (mode.equalsIgnoreCase("mqConnector"))
            return performActiveMqConnectorConversion(message);
        throw new InvalidModeException(mode);
    }

    private KafkaMessage<String, String> performActiveMqConnectorConversion(KafkaMessage<String, String> message) {
        return ActiveMqFacade.invoke(message);
    }

    private class InvalidModeException extends RuntimeException {
        public InvalidModeException(String mode) {
            super(mode);
        }
    }
}