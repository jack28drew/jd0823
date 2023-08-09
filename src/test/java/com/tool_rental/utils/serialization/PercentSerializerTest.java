package com.tool_rental.utils.serialization;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.assertj.core.api.Assertions.assertThat;

class PercentSerializerTest {

    private Writer jsonWriter;
    private JsonGenerator gen;
    private SerializerProvider serializerProvider;

    private PercentSerializer percentSerializer;

    @BeforeEach
    void setup() throws IOException {
        jsonWriter = new StringWriter();
        gen = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();

        percentSerializer = new PercentSerializer();
    }

    @Test
    void verifyPercentIntegerSerializedToPercentString() throws IOException {
        var percent = 7;

        percentSerializer.serialize(percent, gen, serializerProvider);
        gen.flush();

        assertThat(jsonWriter.toString())
                .isEqualTo("\"7%\"");
    }
}