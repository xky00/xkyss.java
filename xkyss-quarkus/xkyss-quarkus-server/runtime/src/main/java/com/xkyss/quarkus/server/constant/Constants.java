package com.xkyss.quarkus.server.constant;

/**
 * 常量
 */
public class Constants {
    public static final String CONFIG_PREFIX = "xkyss";
    public static final String CONFIG_SERVER_PREFIX = CONFIG_PREFIX + ".server";
    public static final String CONFIG_SERVER_BUILD_PREFIX = CONFIG_SERVER_PREFIX + ".build";


    public static final String ROUTE_PREFIX = CONFIG_PREFIX;


    public static final String I18N_ERROR = "i18n/error";
    public static final String I18N_SERVER_ERROR = "i18n/_server_error";
    public static final String I18N_VALIDATION = "i18n/validation";


    public static final String REQUEST_TIME_KEY = "request-time";
    public static final String REQUEST_BODY_KEY = "request-body";
    public static final String REQUEST_BODY_HASHCODE_KEY = "request-body-hashcode-key";
    public static final String RESPONSE_BODY_KEY = "response-body";


    /// 优先级 (值越小,优先级越高)
    public static final int KS_SERVER_PRIORITY = 500;

    public static final int KS_SERVER_PRIORITY_HTTP_LOG_FILTER = KS_SERVER_PRIORITY + 1;
    public static final int KS_SERVER_PRIORITY_RESPONSE_FILTER = KS_SERVER_PRIORITY + 2;
}
