package com.aimyourtechnology.quarkus.kafka;

import io.smallrye.reactive.messaging.kafka.KafkaMessage;
import org.json.JSONObject;

class ActiveMqFacade {
    static KafkaMessage<String, String> invoke(KafkaMessage<String, String> inputMessage) {
        return KafkaMessage.of(inputMessage.getKey(), buildResultPayload(inputMessage));
    }

    private static String buildResultPayload(KafkaMessage<String, String> inputMessage) {
        String payload = inputMessage.getPayload();
        String xml = XmlJsonConverter.readXmlFieldFromJson("XML", payload);
        String traceyId = XmlJsonConverter.readXmlFieldFromJson("TRACEY_ID", payload);
        String json = XmlJsonConverter.convertXmlToJson(xml);
        return JsonAppender.append(json, traceyId);
    }

    private static class JsonAppender {
        private static final int PRETTY_PRINT_INDENT_FACTOR = 4;

        static String append(String jsonString, String traceyId) {
            JSONObject json = new JSONObject(jsonString);
            json.put("traceId", traceyId);
            return json.toString(PRETTY_PRINT_INDENT_FACTOR);
        }
    }
}
