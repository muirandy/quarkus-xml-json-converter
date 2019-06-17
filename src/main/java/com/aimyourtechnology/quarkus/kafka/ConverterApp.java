package com.aimyourtechnology.quarkus.kafka;

import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConverterApp {

    private static final String XML_TOPIC = "incoming-op-msgs";
    private static final String JSON_TOPIC = "modify-op-msgs";

    @Incoming(XML_TOPIC)
    @Outgoing(JSON_TOPIC)
    @Broadcast
    public String process(String xml) {
        return XmlJsonConverter.convertXmlToJson(xml);
    }
}