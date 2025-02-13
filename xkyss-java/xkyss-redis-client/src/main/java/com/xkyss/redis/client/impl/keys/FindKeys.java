package com.xkyss.redis.client.impl.keys;

import java.util.List;

@FunctionalInterface
public interface FindKeys {

  int forEach(List<byte[]> args, int arity, int offset, KeyConsumer collector);
}
