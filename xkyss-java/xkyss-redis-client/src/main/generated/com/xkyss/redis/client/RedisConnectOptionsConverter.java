package com.xkyss.redis.client;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.impl.JsonUtil;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Converter and mapper for {@link com.xkyss.redis.client.RedisConnectOptions}.
 * NOTE: This class has been automatically generated from the {@link com.xkyss.redis.client.RedisConnectOptions} original class using Vert.x codegen.
 */
public class RedisConnectOptionsConverter {


  private static final Base64.Decoder BASE64_DECODER = JsonUtil.BASE64_DECODER;
  private static final Base64.Encoder BASE64_ENCODER = JsonUtil.BASE64_ENCODER;

   static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, RedisConnectOptions obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "connectionString":
          if (member.getValue() instanceof String) {
            obj.setConnectionString((String)member.getValue());
          }
          break;
        case "connectionStrings":
          if (member.getValue() instanceof JsonArray) {
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                obj.addConnectionString((String)item);
            });
          }
          break;
        case "decodeWithBuffer":
          if (member.getValue() instanceof Boolean) {
            obj.setDecodeWithBuffer((Boolean)member.getValue());
          }
          break;
        case "endpoint":
          break;
        case "endpoints":
          if (member.getValue() instanceof JsonArray) {
            java.util.ArrayList<java.lang.String> list =  new java.util.ArrayList<>();
            ((Iterable<Object>)member.getValue()).forEach( item -> {
              if (item instanceof String)
                list.add((String)item);
            });
            obj.setEndpoints(list);
          }
          break;
        case "maxNestedArrays":
          if (member.getValue() instanceof Number) {
            obj.setMaxNestedArrays(((Number)member.getValue()).intValue());
          }
          break;
        case "maxWaitingHandlers":
          if (member.getValue() instanceof Number) {
            obj.setMaxWaitingHandlers(((Number)member.getValue()).intValue());
          }
          break;
        case "password":
          if (member.getValue() instanceof String) {
            obj.setPassword((String)member.getValue());
          }
          break;
        case "preferredProtocolVersion":
          if (member.getValue() instanceof String) {
            obj.setPreferredProtocolVersion(com.xkyss.redis.client.ProtocolVersion.valueOf((String)member.getValue()));
          }
          break;
        case "protocolNegotiation":
          if (member.getValue() instanceof Boolean) {
            obj.setProtocolNegotiation((Boolean)member.getValue());
          }
          break;
      }
    }
  }

   static void toJson(RedisConnectOptions obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

   static void toJson(RedisConnectOptions obj, java.util.Map<String, Object> json) {
    json.put("decodeWithBuffer", obj.getDecodeWithBuffer());
    if (obj.getEndpoint() != null) {
      json.put("endpoint", obj.getEndpoint());
    }
    if (obj.getEndpoints() != null) {
      JsonArray array = new JsonArray();
      obj.getEndpoints().forEach(item -> array.add(item));
      json.put("endpoints", array);
    }
    json.put("maxNestedArrays", obj.getMaxNestedArrays());
    json.put("maxWaitingHandlers", obj.getMaxWaitingHandlers());
    if (obj.getPassword() != null) {
      json.put("password", obj.getPassword());
    }
    if (obj.getPreferredProtocolVersion() != null) {
      json.put("preferredProtocolVersion", obj.getPreferredProtocolVersion().name());
    }
    json.put("protocolNegotiation", obj.isProtocolNegotiation());
  }
}
