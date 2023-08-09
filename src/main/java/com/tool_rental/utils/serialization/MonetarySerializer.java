package com.tool_rental.utils.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.tool_rental.utils.StringUtils;

import java.io.IOException;

/**
 * This is a custom serializer that serializes an integer number of cents
 * into a string with the format "$xx.xx"
 */
public class MonetarySerializer extends StdSerializer<Integer> {

    public MonetarySerializer() {
        this(null);
    }

    public MonetarySerializer(Class<Integer> t) {
        super(t);
    }

    @Override
    public void serialize(Integer value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(StringUtils.formatUSD(value));
    }
}
