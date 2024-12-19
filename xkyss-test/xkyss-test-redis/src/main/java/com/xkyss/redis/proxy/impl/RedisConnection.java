package com.xkyss.redis.proxy.impl;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.FullBulkStringRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.util.CharsetUtil;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.impl.NetSocketInternal;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Redis 连接
 */
public class RedisConnection {
    private static final Logger log = LoggerFactory.getLogger(RedisConnection.class);

    private final NetSocketInternal so;
    private final ChannelHandlerContext ctx;

    public RedisConnection(NetSocketInternal so) {
        this.so = so;
        this.ctx = so.channelHandlerContext();
    }

    /**
     * 处理客户端消息
     * @param m 消息
     */
    public void handleMessage(Object m) {
        if (!(m instanceof RedisMessage)) {
            this.ctx.fireExceptionCaught(new Exception("Wrong m type " + m.getClass().getName()));
        }

        log.info("messageHandler. m.getClass: {}", m.getClass());

        if (m instanceof ArrayRedisMessage) {
            parseCommand((ArrayRedisMessage) m);
        }
    }

    private void parseCommand(ArrayRedisMessage m) {
        List<RedisMessage> commands = m.children();
        if (commands.isEmpty()) {
            return;
        }

        String cmdName = msgToString(commands.get(0));
        Command cmd = Command.create(cmdName);
        log.info("Command: {}", cmd.toString());

        Request request = Request.cmd(cmd);
        for (int i = 1; i < commands.size(); i++) {
            RedisMessage redisMessage = commands.get(i);
            Buffer buffer = msgToBuffer(redisMessage);
            if (buffer != null) {
                request.arg(buffer);
            }
        }

        log.info("Request: \n{}", request);
    }

    public String msgToString(RedisMessage msg) {
        Buffer buffer = msgToBuffer(msg);
        return buffer.toString();
    }

    public Buffer msgToBuffer(RedisMessage msg) {
        if(msg instanceof FullBulkStringRedisMessage) {
            ByteBuf content = ((FullBulkStringRedisMessage) msg).content();
            return Buffer.buffer(content);
        }
        return Buffer.buffer();
    }
}
