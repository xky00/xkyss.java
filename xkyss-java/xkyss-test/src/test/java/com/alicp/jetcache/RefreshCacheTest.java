package com.alicp.jetcache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class RefreshCacheTest {

    @Test
    public void test1() throws InterruptedException {
        Cache<Object, Object> cache = new RefreshCache<>(LinkedHashMapCacheBuilder.createLinkedHashMapCacheBuilder().buildCache());
        cache.config().setRefreshPolicy(RefreshPolicy.newPolicy(50, TimeUnit.MILLISECONDS));

        AtomicLong value = new AtomicLong();
        Function loader = k-> value.incrementAndGet();
        long t = cache.config().getRefreshPolicy().getRefreshMillis();

        // 获取数据并激活缓存
        Object v = cache.computeIfAbsent("k1", loader);
        Assertions.assertEquals(v, cache.get("k1"));
        Thread.sleep((long) (t * 1.5));
        Assertions.assertNotEquals(v, cache.get("k1"));

        v = cache.computeIfAbsent("k2", loader, false);
        Assertions.assertEquals(v, cache.get("k2"));
        Thread.sleep((long) (t * 1.5));
        Assertions.assertNotEquals(v, cache.get("k2"));

        v = cache.computeIfAbsent("k3", loader, false, 10, TimeUnit.SECONDS);
        Assertions.assertEquals(v, cache.get("k3"));
        Thread.sleep((long) (t * 1.5));
        Assertions.assertNotEquals(v, cache.get("k3"));

        // 如何停止刷新?
        getRefreshCache(cache).stopRefresh();
        Function refreshLoader = k -> {
            long _v = value.incrementAndGet();
            System.out.println("refresh load: " + _v);
            return _v;
        };
        v = cache.computeIfAbsent("k4", refreshLoader, false, 10, TimeUnit.SECONDS);
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // Thread.sleep((long) (t * 11.5));
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // System.out.println(cache.get("k4"));
        // Assertions.assertEquals(v, cache.get("k4"));
        // Thread.sleep((long) (t * 1.5));
        // Assertions.assertEquals(v, cache.get("k4"));
    }


    private static RefreshCache getRefreshCache(Cache cache) {
        Cache c = cache;
        while (!(c instanceof RefreshCache)) {
            if (c instanceof ProxyCache) {
                c = ((ProxyCache) c).getTargetCache();
            }
        }
        return (RefreshCache) c;
    }

}
