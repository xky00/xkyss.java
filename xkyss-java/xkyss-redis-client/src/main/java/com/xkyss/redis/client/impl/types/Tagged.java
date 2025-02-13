package com.xkyss.redis.client.impl.types;

import com.xkyss.redis.client.Response;
import com.xkyss.redis.client.ResponseType;

public class Tagged {

    public static Response NULL = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.NULL;
        }
    };
    public static Response SIMPLE = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.SIMPLE;
        }
    };
    public static Response ERROR = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.ERROR;
        }
    };
    public static Response BOOLEAN = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.BOOLEAN;
        }
    };
    public static Response NUMBER = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.NUMBER;
        }
    };
    public static Response BULK = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.BULK;
        }
    };
    public static Response PUSH = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.PUSH;
        }
    };
    public static Response ATTRIBUTE = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.ATTRIBUTE;
        }
    };
    public static Response MULTI = new Response() {
        @Override
        public ResponseType type() {
            return ResponseType.MULTI;
        }
    };
}
