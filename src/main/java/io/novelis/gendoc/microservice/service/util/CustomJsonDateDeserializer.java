package io.novelis.gendoc.microservice.service.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.ZonedDateTime;

public class CustomJsonDateDeserializer extends JsonDeserializer {
    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return ZonedDateTime.parse(parser.getValueAsString());
    }
}
