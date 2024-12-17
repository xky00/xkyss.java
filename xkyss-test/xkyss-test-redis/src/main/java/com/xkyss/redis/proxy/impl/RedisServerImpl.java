package com.xkyss.redis.proxy.impl;

import com.xkyss.redis.proxy.RedisServer;
import com.xkyss.redis.proxy.RedisServerOption;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.redis.RedisArrayAggregator;
import io.netty.handler.codec.redis.RedisBulkStringAggregator;
import io.netty.handler.codec.redis.RedisDecoder;
import io.netty.handler.codec.redis.RedisEncoder;
import io.netty.util.ReferenceCountUtil;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.impl.VertxInternal;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.impl.NetSocketInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisServerImpl implements RedisServer {
    private static final Logger log = LoggerFactory.getLogger(RedisServerImpl.class);

    private final VertxInternal vertx;
    private final NetServer server;
    private RedisServerOption options;

    public RedisServerImpl(Vertx vertx, RedisServerOption options) {
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
        server.connectHandler(so -> {
            NetSocketInternal soi = (NetSocketInternal) so;
            ChannelPipeline pipeline = soi.channelHandlerContext().pipeline();
            initChannel(pipeline);

            soi.eventHandler(e -> {
                log.info("eventHandler.");
                ReferenceCountUtil.release(e);
            });

            soi.messageHandler(m -> {
                log.info("messageHandler.");
            });
        });
        return server.listen(port, host).map(this);
    }

    @Override
    public Future<RedisServer> listen(int port) {
        return listen(port, this.options.getHost());
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
