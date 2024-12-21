package com.xkyss.redis.proxy.impl;

import com.xkyss.redis.proxy.RedisContext;
import com.xkyss.redis.proxy.RedisEndpoint;
import com.xkyss.redis.proxy.RedisServerOptions;
import com.xkyss.redis.proxy.middleware.FallbackMiddleware;
import com.xkyss.redis.proxy.middleware.MiddlewareBuilder;
import com.xkyss.redis.proxy.middleware.MiddlewareDelegate;
import com.xkyss.redis.proxy.middleware.PluginMiddleware;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.FullBulkStringRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.impl.NetSocketInternal;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;

/**
 * Redis 连接
 */
public class RedisConnection {
    private static final Logger logger = LoggerFactory.getLogger(RedisConnection.class);

    private final NetSocketInternal so;
    private final ChannelHandlerContext ctx;
    private final RedisServerOptions options;

    private RedisEndpointImpl endpoint;
    private final Handler<RedisEndpoint> endpointHandler;

    private final Handler<Throwable> exceptionHandler;
    private final Supplier<MiddlewareDelegate<RedisContext>> middlewareBuilder;

    public RedisConnection(
        NetSocketInternal so,
        Handler<RedisEndpoint> endpointHandler,
        Handler<Throwable> exceptionHandler,
        RedisServerOptions options,
        Supplier<MiddlewareDelegate<RedisContext>> middlewareBuilder) {

        this.so = so;
        this.endpointHandler = endpointHandler;
        this.exceptionHandler = exceptionHandler;
        this.options = options;
        this.ctx = so.channelHandlerContext();
        this.middlewareBuilder = middlewareBuilder;
    }

    /**
     * 处理客户端消息
     *
     * @param m 消息
     */
    public void handleMessage(Object m) {
        if (!(m instanceof RedisMessage)) {
            this.ctx.fireExceptionCaught(new Exception("Wrong m type " + m.getClass().getName()));
        }

        logger.info("messageHandler. m.getClass: {}", m.getClass());

        if (m instanceof ArrayRedisMessage) {
            parseCommand((ArrayRedisMessage) m);
        }
    }

    /**
     * 解析Redis命令
     * @param m 消息
     */
    private void parseCommand(ArrayRedisMessage m) {
        List<RedisMessage> commands = m.children();
        if (commands.isEmpty()) {
            return;
        }

        String cmdName = msgToString(commands.get(0));
        Command cmd = Command.create(cmdName);
        logger.info("Command: {}", cmd.toString());

        Request request = Request.cmd(cmd);
        for (int i = 1; i < commands.size(); i++) {
            RedisMessage redisMessage = commands.get(i);
            Buffer buffer = msgToBuffer(redisMessage);
            if (buffer != null) {
                request.arg(buffer);
            }
        }

        logger.info("Request: \n{}", request);

        RedisContext rc = new RedisContext().request(request);
        if (request.command().toString().equals("ping")) {
            handlePing();
        }

        if (checkConnected()) {
            this.endpoint.handle(rc);
        }
    }

    private void handlePing() {
        // 如果endpoint不为空，说明已经建立连接，直接返回
        if (endpoint != null) {
            return;
        }

        endpoint = new RedisEndpointImpl(this.so, this.middlewareBuilder.get());

        this.so.exceptionHandler(e -> this.endpoint.handleException(e));
        this.so.closeHandler(v -> this.endpoint.handleClosed());
        this.endpointHandler.handle(this.endpoint);
    }

    /**
     * RedisMessage 转 String
     * @param msg 消息
     * @return String
     */
    public String msgToString(RedisMessage msg) {
        Buffer buffer = msgToBuffer(msg);
        return buffer.toString();
    }

    /**
     * RedisMessage 转 Buffer
     * @param msg 消息
     * @return Buffer
     */
    public Buffer msgToBuffer(RedisMessage msg) {
        if (msg instanceof FullBulkStringRedisMessage) {
            ByteBuf content = ((FullBulkStringRedisMessage) msg).content();
            return Buffer.buffer(content);
        }
        return Buffer.buffer();
    }
    /**
     * Check if the endpoint was created and is connected
     *
     * @return  status of the endpoint (connected or not)
     */
    private boolean checkConnected() {
        synchronized (this.so) {
            if ((this.endpoint != null) && (this.endpoint.isConnected())) {
                return true;
            } else {
                so.close();
                throw new IllegalArgumentException("Received an Redis packet from a not connected client (CONNECT not sent yet)");
            }
        }
    }
}
