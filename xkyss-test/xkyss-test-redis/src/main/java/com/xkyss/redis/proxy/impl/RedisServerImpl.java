package com.xkyss.redis.proxy.impl;

import com.xkyss.redis.proxy.RedisContext;
import com.xkyss.redis.proxy.RedisEndpoint;
import com.xkyss.redis.proxy.RedisServer;
import com.xkyss.redis.proxy.RedisServerOptions;
import com.xkyss.redis.proxy.middleware.MiddlewareHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.util.ReferenceCountUtil;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.impl.NetSocketInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Redis Proxy Server 实现
 */
public class RedisServerImpl implements RedisServer {
    private static final Logger log = LoggerFactory.getLogger(RedisServerImpl.class);

    private final VertxInternal vertx;
    private final NetServer server;
    private RedisServerOptions options;

    private Handler<RedisEndpoint> endpointHandler;
    private Handler<Throwable> exceptionHandler;
    private Supplier<MiddlewareHandler<RedisContext>> middlewareBuilder;


    private Map<String, RedisEndpoint> endpoints = new ConcurrentHashMap<>();

    public RedisServerImpl(Vertx vertx, RedisServerOptions options) {
        this.vertx = (VertxInternal) vertx;
        this.server = vertx.createNetServer(options);
        this.options = options;
    }

    @Override
    public Future<RedisServer> listen() {
        return listen(this.options.getPort());
    }

    @Override
    public Future<RedisServer> listen(int port, String host) {
        Handler<RedisEndpoint> h1 = ep -> {
            endpoints.put(UUID.randomUUID().toString(), ep);
            if (this.endpointHandler != null) {
                this.endpointHandler.handle(ep);
            }
        };

        server.connectHandler(so -> {
            NetSocketInternal soi = (NetSocketInternal) so;
            ChannelPipeline pipeline = soi.channelHandlerContext().pipeline();

            initChannel(pipeline);
            RedisConnection conn = new RedisConnection(soi, h1, exceptionHandler, options, middlewareBuilder);

            soi.eventHandler(e -> {
                log.info("eventHandler.");
                ReferenceCountUtil.release(e);
            });

            soi.messageHandler(m -> {
                synchronized (conn) {
                    conn.handleMessage(m);
                }
            });
        });

        return server.listen(port, host).map(this);
    }

    @Override
    public Future<RedisServer> listen(int port) {
        return listen(port, this.options.getHost());
    }

    @Override
    public RedisServerImpl endpointHandler(Handler<RedisEndpoint> handler) {
        this.endpointHandler = handler;
        return this;
    }

    @Override
    public RedisServerImpl exceptionHandler(Handler<Throwable> handler) {
        this.exceptionHandler = handler;
        return this;
    }

    @Override
    public RedisServer middlewareBuilder(Supplier<MiddlewareHandler<RedisContext>> supplier) {
        this.middlewareBuilder = supplier;
        return this;
    }

    private void initChannel(ChannelPipeline pipeline) {
        pipeline.addBefore("handler", "redisDecoder", new RedisDecoder(true));
        pipeline.addBefore("handler", "redisBulkStringAggregator", new RedisBulkStringAggregator());
        pipeline.addBefore("handler", "redisArrayAggregator", new RedisArrayAggregator());
        pipeline.addBefore("handler", "redisEncoder", new RedisEncoder());
    }
}
