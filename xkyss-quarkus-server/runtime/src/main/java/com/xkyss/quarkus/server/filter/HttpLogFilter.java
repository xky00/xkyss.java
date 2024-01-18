package com.xkyss.quarkus.server.filter;

import com.xkyss.quarkus.server.config.RuntimeConfig;
import io.quarkus.runtime.util.StringUtil;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mutiny.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MediaType;
import org.apache.commons.io.IOUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.StringJoiner;

import static com.xkyss.quarkus.server.constant.Constants.*;

@SuppressWarnings("unused")
@ApplicationScoped
public class HttpLogFilter {

    @Inject
    Logger logger;

    @Inject
    RuntimeConfig config;

    @Inject
    Vertx vertx;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    @ServerRequestFilter
    public Uni<Void> mapRequest(ContainerRequestContext request, RoutingContext rc) {
        if (!config.httpLogEnable()) {
            return Uni.createFrom().voidItem();
        }

        return vertx.executeBlocking(Uni.createFrom().item(() -> {
            try {
                boolean isLogBody = request.getMediaType() != null
                    && (request.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE) || request.getMediaType().isCompatible(MediaType.TEXT_PLAIN_TYPE))
                    && request.hasEntity() && request.getEntityStream().available() > 0;
                if (isLogBody) {
                    String text = IOUtils.toString(request.getEntityStream(), StandardCharsets.UTF_8);
                    request.setEntityStream(IOUtils.toInputStream(text, StandardCharsets.UTF_8));
                    rc.put(REQUEST_BODY_HASHCODE_KEY, text.hashCode());

                    if ((request.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE))) {
                        rc.put(REQUEST_BODY_KEY, Json.encodePrettily(Json.decodeValue(text)));
                    }
                    else {
                        rc.put(REQUEST_BODY_KEY, text);
                    }
                } else {
                    rc.put(REQUEST_BODY_KEY, "<(EMPTY) OR NOT json or text>");
                }
            } catch (IOException e) {
                logger.warn("获取Request Body失败", e);
                rc.put(REQUEST_BODY_KEY, "<EXCEPTION>");
            }
            return null;
        })).replaceWithVoid();
    }


    @ServerResponseFilter(priority = KS_SERVER_PRIORITY_HTTP_LOG_FILTER)
    public Uni<Void> mapResponse(ContainerResponseContext response, RoutingContext rc) {
        if (!config.httpLogEnable()) {
            return Uni.createFrom().voidItem();
        }

        return vertx.executeBlocking(Uni.createFrom().item(() -> {
            boolean isLogBody = response.getMediaType() != null
                && (response.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE) || response.getMediaType().isCompatible(MediaType.TEXT_PLAIN_TYPE))
                && response.hasEntity();
            if (isLogBody) {
                Object entity = response.getEntity();
                if (entity instanceof String) {
                    rc.put(RESPONSE_BODY_KEY, entity);
                }
                else if (entity instanceof byte[]) {
                    rc.put(RESPONSE_BODY_KEY, Json.encodePrettily(Json.decodeValue(Buffer.buffer((byte[]) entity))));
                }
                else {
                    rc.put(RESPONSE_BODY_KEY, Json.encodePrettily(entity));
                }
            } else {
                rc.put(RESPONSE_BODY_KEY, "<(EMPTY) OR NOT (json or text)>");
            }
            return null;
        })).replaceWithVoid();
    }

    void register(@Observes Router router) {
        if (!config.httpLogEnable()) {
            logger.info("未注册Http日志过滤.");
            return;
        }
        LoggerHandler loggerHandler = new LoggerHandler(logger);

        router.route().order(Integer.MIN_VALUE).handler(loggerHandler);

        if (config.httpLogRoutes().isPresent()) {
            for (String routePath: config.httpLogRoutes().get()) {
                router.route(routePath).order(-1).handler(loggerHandler);
            }
        }

        logger.info("已注册Http日志过滤.");
    }

    public static class LoggerHandler implements Handler<RoutingContext> {
        private final Logger logger;

        public LoggerHandler(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void handle(RoutingContext rc) {

            rc.put(REQUEST_TIME_KEY, new Date());
            rc.put(REQUEST_BODY_KEY, "<EMPTY>");
            rc.put(RESPONSE_BODY_KEY, "<EMPTY>");

            rc.addEndHandler().onComplete(v -> {
                StringBuilder sb = new StringBuilder();
                Date requestAt = rc.get(REQUEST_TIME_KEY);

                // request
                {
                    HttpServerRequest request = rc.request();
                    sb.append("\n\n[Request]:\n");
                    sb.append("[Request At]: ").append(dateFormat.format(requestAt)).append("\n\n");
                    sb.append(request.method()).append(" ").append(request.uri()).append(" ").append(request.version().alpnName()).append("\n");
                    String text = readAttribute(request.headers());
                    sb.append(StringUtil.isNullOrEmpty(text) ? "<Request head is empty>" : text).append("\n\n");
                    sb.append((String) rc.get(REQUEST_BODY_KEY));
                }

                // response
                {
                    HttpServerResponse response = rc.response();
                    Date responseAt = new Date();
                    sb.append("\n\n\n[Response]:\n");
                    sb.append("[ Request At]: ").append(dateFormat.format(requestAt)).append("\n");
                    sb.append("[Response At]: ").append(dateFormat.format(responseAt)).append("\n");
                    sb.append("[ Total Cast]: ").append(responseAt.getTime() - requestAt.getTime()).append(" ms\n\n");
                    sb.append("Status: ").append(response.getStatusCode()).append(", ").append(response.getStatusMessage()).append("\n");
                    String text = readAttribute(response.headers());
                    sb.append(StringUtil.isNullOrEmpty(text) ? "<Response head is empty>" : text).append("\n\n");
                    sb.append((String) rc.get(RESPONSE_BODY_KEY));
                }

                logger.info(sb);
            });
            rc.next();
        }

        private static String readAttribute(MultiMap headers) {
            if (headers.isEmpty()) {
                return null;
            }
            else {
                final StringJoiner joiner = new StringJoiner(System.lineSeparator());

                for (Map.Entry<String, String> header : headers) {
                    joiner.add(header.getKey() + ": " + header.getValue());
                }

                return joiner.toString();
            }
        }
    }
}
