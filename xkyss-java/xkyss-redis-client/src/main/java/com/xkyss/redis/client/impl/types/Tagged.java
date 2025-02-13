package com.xkyss.redis.client.impl.types;

import com.xkyss.redis.client.Response;
import com.xkyss.redis.client.ResponseType;

public class Tagged implements Response {

    public static Response NULL = new Tagged(ResponseType.NULL);
    public static Response SIMPLE = new Tagged(ResponseType.SIMPLE);
    public static Response ERROR = new Tagged(ResponseType.ERROR);
    public static Response BOOLEAN = new Tagged(ResponseType.BOOLEAN);
    public static Response NUMBER = new Tagged(ResponseType.NUMBER);
    public static Response BULK = new Tagged(ResponseType.BULK);
    public static Response PUSH = new Tagged(ResponseType.PUSH);
    public static Response ATTRIBUTE = new Tagged(ResponseType.ATTRIBUTE);
    public static Response MULTI = new Tagged(ResponseType.MULTI);

    private final ResponseType type;

    public Tagged(ResponseType type) {
        this.type = type;
    }

    @Override
    public ResponseType type() {
        return null;
    }
}
