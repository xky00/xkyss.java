package com.xkyss.quarkus.server.filter;


import com.xkyss.quarkus.server.config.BuildConfig;
import com.xkyss.quarkus.server.dto.Response;
import com.xkyss.quarkus.server.service.ErrorMessageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;

import java.util.Set;

import static com.xkyss.quarkus.server.constant.Constants.KS_SERVER_RESPONSE_FILTER_PRIORITY;

/**
 * 自动给http调用包装为Response
 */
@ApplicationScoped
public class ResponseFilter {

    @Inject
    ErrorMessageService ems;

    @Inject
    BuildConfig buildConfig;

    private static final Set<MediaType> mediaTypes = Set.of(MediaType.APPLICATION_JSON_TYPE, MediaType.TEXT_PLAIN_TYPE);

    @ServerResponseFilter(priority = KS_SERVER_RESPONSE_FILTER_PRIORITY)
    public void mapResponse(ContainerResponseContext response) {

        // 没有返回体
        if (!response.hasEntity()) {
            return;
        }

        // 不处理无媒体类型
        if (response.getMediaType() == null) {
            return;
        }

        // 过滤指定MediaType
        if (mediaTypes.stream().noneMatch(t -> response.getMediaType().isCompatible(t))) {
            return;
        }

        // 忽略指定的类型
        if (buildConfig.ignoreResponseFilterTypes().contains(response.getEntityClass().getName())) {
            return;
        }

        // 已经是Response,看下是否需要填充message
        if (response.getEntity() instanceof Response) {
            @SuppressWarnings("rawtypes") Response entity = (Response) response.getEntity();
            if (entity.getCode() != null && entity.getMessage() == null) {
                entity.setMessage(ems.getMessage(entity.getCode()));
            }
            return;
        }

        // 包装一下
        Response<Object> r = Response.success();
        r.setMessage(ems.getMessage(r.getCode()));
        r.setData(response.getEntity());
        response.setEntity(r);
    }
}