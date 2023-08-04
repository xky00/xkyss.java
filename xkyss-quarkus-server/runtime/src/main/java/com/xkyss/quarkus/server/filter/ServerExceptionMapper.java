package com.xkyss.quarkus.server.filter;

import com.xkyss.quarkus.server.error.ServerException;
import com.xkyss.quarkus.server.service.ErrorMessageService;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * ServerException异常处理
 */
@Provider
public class ServerExceptionMapper implements ExceptionMapper<ServerException> {

    @Inject
    Logger logger;

    @Inject
    ErrorMessageService ems;

    @Override
    public Response toResponse(ServerException e) {
        logger.warn("ServerException", e);

        com.xkyss.quarkus.server.dto.Response<Object> r = com.xkyss.quarkus.server.dto.Response.exception();
        r.setMessage(ems.getMessage(r.getCode()));
        r.setData(JsonObject.of(
            "code", e.getCode(),
            "message", e.getMessage(),
            "type", e.getClass().getName()));

        return Response.ok(Response.Status.BAD_REQUEST)
            .type(MediaType.APPLICATION_JSON)
            .entity(r)
            .build();
    }
}
