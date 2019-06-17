package com.aimyourtechnology.quarkus.kafka;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;

import java.util.Random;

import static net.javacrumbs.jsonunit.JsonAssert.assertJsonEquals;

public class XmlJsonConverterTest {

    private String orderId = "" + new Random().nextInt();

    @Test
    void convertsXmlToJson() {
        String jsonString = XmlJsonConverter.convertXmlToJson(xmlValue());

        assertJsonEquals(jsonValue(), jsonString);
    }

    @Test
    void convertsJsonToXml() {
        String xmlString = XmlJsonConverter.convertJsonToXml(jsonValue());

        XmlAssert.assertThat(xmlValue()).and(xmlString).ignoreWhitespace().areIdentical();
    }

    private String xmlValue() {
        return String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<order>" +
                        "<orderId>%s</orderId>\n" +
                        "</order>", orderId
        );
    }

    private String jsonValue() {
        return String.format(
                "{" +
                        "  \"order\":{" +
                        "    \"orderId\":%s" +
                        "  }" +
                        "}",
                orderId
        );
    }
}
