///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS io.vertx:vertx-core:4.5.1


import io.vertx.core.Vertx;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class Standalone {

    public static void main(String... args) throws IOException {
        AtomicInteger count = new AtomicInteger();
        Vertx vertx = Vertx.vertx();
        vertx.setPeriodic(1000, id -> {
            count.getAndIncrement();
            // This handler will get called every second
            System.out.println("timer fired!  " + count);
        });
    }
}
