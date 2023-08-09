package com.tool_rental.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tool_rental.utils.StringUtils;

import java.io.IOException;

/**
 * This is a custom deserializer that deserializes a USD string in
 * the format "$xx.xx" to an integer number of cents
 */
public class MonetaryDeserializer extends StdDeserializer<Integer> {

    public MonetaryDeserializer() {
        this(null);
    }

    public MonetaryDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return StringUtils.parseUsdToCents(node.asText());
    }
}
