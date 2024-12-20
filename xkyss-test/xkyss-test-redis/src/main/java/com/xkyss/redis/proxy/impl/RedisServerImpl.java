package com.xkyss.redis.proxy.impl;

import com.xkyss.redis.proxy.RedisEndpoint;
import com.xkyss.redis.proxy.RedisServer;
import com.xkyss.redis.proxy.RedisServerOptions;
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
        Handler<RedisEndpoint> h1 = endpointHandler;
        Handler<Throwable> h2 = exceptionHandler;

        if (h1 == null) {
            return vertx.getOrCreateContext().failedFuture(
                new IllegalStateException("Please set endpointHandler before server is listening"));
        }

        server.connectHandler(so -> {
            NetSocketInternal soi = (NetSocketInternal) so;
            ChannelPipeline pipeline = soi.channelHandlerContext().pipeline();

            initChannel(pipeline);
            RedisConnection conn = new RedisConnection(soi, h1, h2, options);

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

    private void initChannel(ChannelPipeline pipeline) {
        pipeline.addBefore("handler", "redisDecoder", new RedisDecoder(true));
        pipeline.addBefore("handler", "redisBulkStringAggregator", new RedisBulkStringAggregator());
        pipeline.addBefore("handler", "redisArrayAggregator", new RedisArrayAggregator());
        pipeline.addBefore("handler", "redisEncoder", new RedisEncoder());

        // pipeline.addBefore("handler", "resultsHandler", new ResultsHandler());
        // pipeline.addBefore("handler", "commandHandler", new CommandHandler());
    }
}
