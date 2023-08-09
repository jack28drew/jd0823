package com.tool_rental.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.tool_rental.utils.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializers(new LocalDateSerializer(StringUtils.DATE_FORMAT))
                    .deserializers(new LocalDateDeserializer(StringUtils.DATE_FORMAT))
                    .modules(
                            new JavaTimeModule(),
                            new ParameterNamesModule(JsonCreator.Mode.PROPERTIES))
                    .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                    .visibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                    .visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    .build();
        };
    }
}
