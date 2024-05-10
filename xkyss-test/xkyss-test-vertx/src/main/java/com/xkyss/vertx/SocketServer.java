package com.xkyss.vertx;

import io.netty.buffer.ByteBuf;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.parsetools.JsonEventType;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.core.parsetools.RecordParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.vertx.core.logging.LoggerFactory.LOGGER_DELEGATE_FACTORY_CLASS_NAME;
import static java.lang.System.setProperty;

public class SocketServer extends AbstractVerticle {

    public static void main(String[] args) {
        setProperty (LOGGER_DELEGATE_FACTORY_CLASS_NAME, SLF4JLogDelegateFactory.class.getName());

        Vertx vertx = Vertx.vertx(VertxKit.debugOptions());
        vertx.deployVerticle(new SocketServer());
    }

    private final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    @Override
    public void start() {
        NetServerOptions options = new NetServerOptions()
            .setLogActivity(true)
            .setHost("localhost")
            .setPort(4321);
        NetServer server = vertx.createNetServer(options);

        server
            .connectHandler(socket -> {
                int headerLength = 0x10;
                RecordParser parser = RecordParser.newFixed(headerLength, socket);
                // 根据长度拆包
                parser
                    .endHandler(v -> socket.close())
                    .exceptionHandler(e -> {
                        logger.error("Error", e);
                        socket.close();
                    })
                    .handler(new Handler<Buffer>() {
                        private boolean parseHeader = true;
                        private long rid;
                        private int bodyLen;
                        private int type;

                        @Override
                        public void handle(Buffer buffer) {
                            if (parseHeader) {
                                rid = buffer.getLongLE(0x00);
                                bodyLen = buffer.getIntLE(0x08);
                                type = buffer.getIntLE(0x0C);
                                logger.info("rid: {}, bodyLen: {}, type: {}", rid, bodyLen, type);
                                parseHeader = false;
                                parser.fixedSizeMode(bodyLen);
                            }
                            else {
                                String body = buffer.toString("UTF-8");
                                logger.info("body: {}", body);

                                CacheModel cm = Json.decodeValue(body, CacheModel.class);

                                // GET
                                if (type == 1) {
                                    Buffer buf = Buffer.buffer();

                                    User u = new User();
                                    u.setName("name-" + cm.getKey());
                                    u.setCode("code-" + cm.getKey());
                                    u.setAge((int)rid);
                                    String r = Json.encode(u);

                                    // body_offset
                                    buf.appendIntLE(0x10 + r.length());
                                    // header
                                    buf.appendLongLE(rid);
                                    buf.appendIntLE(r.length());
                                    buf.appendIntLE(type);
                                    // body
                                    buf.appendString(r);
                                    socket.write(buf);
                                }
                                // PUT
                                else if (type == 2) {

                                }
                                // REMOVE
                                else if (type == 3) {

                                }
                                else {
                                    logger.error("Unknown type: {}", type);
                                }

                                parseHeader = true;
                                parser.fixedSizeMode(headerLength);
                            }
                        }
                    });

                // // 粘包处理
                // RecordParser.newDelimited("\0", socket)
                //     .endHandler(v -> socket.close())
                //     .exceptionHandler(e -> {
                //         logger.error("Error", e);
                //         socket.close();
                //     })
                //     .handler(buffer -> {
                //         String data = buffer.toString("UTF-8");
                //         logger.info("Received data: {}", data);
                //
                //         CacheModel cache = Json.decodeValue(buffer, CacheModel.class);
                //
                //         User user = new User();
                //         user.setName("name-" + cache.getKey());
                //         user.setCode("code-" + cache.getKey());
                //         user.setAge(cache.getRid().intValue());
                //         cache.setValue(Json.encode(user));
                //
                //         // 返回
                //         Buffer respBuffer = Json.encodeToBuffer(cache);
                //         respBuffer.appendByte((byte) 0);
                //         socket.write(respBuffer);
                //     });

                // // Json粘包处理
                // JsonParser parser = JsonParser.newParser(socket);
                // parser.objectValueMode()
                //     .endHandler(v -> socket.close())
                //     .exceptionHandler(e -> {
                //         logger.error("Error", e);
                //         socket.close();
                //     })
                //     .handler(event -> {
                //         Message message = event.mapTo(Message.class);
                //         logger.info("Received message: {}", message.toString());
                //
                //     });
            })
            .listen();
    }

    static class CacheModel {
        private String area;
        private String cacheName;
        private String key;
        private String value;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCacheName() {
            return cacheName;
        }

        public void setCacheName(String cacheName) {
            this.cacheName = cacheName;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    static class User {
        String name;
        String code;
        Integer age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", age=" + age +
                '}';
            }
    }
}
