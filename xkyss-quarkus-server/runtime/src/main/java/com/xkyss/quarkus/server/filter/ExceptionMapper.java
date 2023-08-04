package com.xkyss.quarkus.server.filter;

import com.xkyss.quarkus.server.service.ErrorMessageService;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 * 异常处理
 */
public class ExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Exception> {
    @Inject
    Logger logger;

    @Inject
    ErrorMessageService ems;

    @Override
    public Response toResponse(Exception e) {
        logger.warn("Exception", e);

        com.xkyss.quarkus.server.dto.Response<Object> r = com.xkyss.quarkus.server.dto.Response.exception();
        r.setMessage(ems.getMessage(r.getCode()));
        r.setData(JsonObject.of("message", e.getMessage()));

        return Response.ok(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(r)
            .build();
    }
}
