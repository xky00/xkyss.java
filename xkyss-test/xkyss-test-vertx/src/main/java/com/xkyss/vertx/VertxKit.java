package com.xkyss.vertx;

import io.vertx.core.VertxOptions;

public class VertxKit {
    public static VertxOptions debugOptions() {

        VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTimeUnit(java.util.concurrent.TimeUnit.SECONDS);
        options.setWarningExceptionTime(2000);
        options.setMaxWorkerExecuteTimeUnit(java.util.concurrent.TimeUnit.SECONDS);
        options.setMaxWorkerExecuteTime(2000);

        return options;
    }
}
