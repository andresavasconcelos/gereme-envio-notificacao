package com.br.project.gereme.enums;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class ChannelEnumDeserializer extends JsonDeserializer<ChannelEnum> {
    @Override
    public ChannelEnum deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText().toUpperCase(); // Converte para uppercase
        return ChannelEnum.valueOf(value);
    }
}