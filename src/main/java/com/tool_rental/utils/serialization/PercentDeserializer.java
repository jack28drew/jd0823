package com.tool_rental.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.tool_rental.utils.StringUtils;

import java.io.IOException;

/**
 * This is a custom deserializer that deserializes a percent string
 * in the format "xx%" to an integer
 */
public class PercentDeserializer extends StdDeserializer<Integer> {

    public PercentDeserializer() {
        this(null);
    }

    public PercentDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return StringUtils.parsePercent(node.asText());
    }

}
