package com.xkyss.redis.proxy.impl;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.redis.ArrayRedisMessage;
import io.netty.handler.codec.redis.FullBulkStringRedisMessage;
import io.netty.handler.codec.redis.RedisMessage;
import io.netty.util.CharsetUtil;
import io.vertx.core.net.impl.NetSocketInternal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        log.info("\t {}", m.toString());

        if (m instanceof ArrayRedisMessage) {
            ArrayRedisMessage arr = (ArrayRedisMessage) m;
            if (!arr.isNull()) {
                RedisMessage arr0 = arr.children().get(0);
                if (arr0 instanceof FullBulkStringRedisMessage) {
                    FullBulkStringRedisMessage fbsm = (FullBulkStringRedisMessage) arr0;
                    String cmd = fbsm.content().toString(CharsetUtil.UTF_8);
                    log.info("\t\t {}", cmd);
                }
            }
        }
    }
}
