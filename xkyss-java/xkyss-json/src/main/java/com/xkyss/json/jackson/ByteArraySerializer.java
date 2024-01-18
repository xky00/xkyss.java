package com.xkyss.json.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

import static com.xkyss.json.impl.JsonUtil.BASE64_ENCODER;

class ByteArraySerializer extends JsonSerializer<byte[]> {

  @Override
  public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
    jgen.writeString(BASE64_ENCODER.encodeToString(value));
  }
}
