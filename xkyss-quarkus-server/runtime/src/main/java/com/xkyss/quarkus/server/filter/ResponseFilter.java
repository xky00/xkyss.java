package com.xkyss.quarkus.server.filter;


import com.xkyss.quarkus.server.service.ErrorMessageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

/**
 * 自动给http调用包装为Response
 */
@ApplicationScoped
public class ResponseFilter {

    @Inject
    ErrorMessageService ems;

    @ServerResponseFilter
    public void mapResponse(ContainerResponseContext response) {
        // 没有返回体
        if (!response.hasEntity()) {
            return;
        }

        // 不处理无媒体类型
        if (response.getMediaType() == null) {
            return;
        }

        // 只对json返回类型进行处理
        if (!response.getMediaType().isCompatible(MediaType.APPLICATION_JSON_TYPE)) {
            return;
        }

        // 已经是Response了
        if (response.getEntity() instanceof Response) {
            return;
        }
        if (response.getEntity() instanceof com.xkyss.quarkus.server.dto.Response) {
            return;
        }

        // 包装一下
        com.xkyss.quarkus.server.dto.Response<Object> r = com.xkyss.quarkus.server.dto.Response.success();
        r.setMessage(ems.getMessage(r.getCode()));
        r.setData(response.getEntity());
        response.setEntity(r);
    }
}