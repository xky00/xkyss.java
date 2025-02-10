package com.xkyss.redis.client.impl.types;

import com.xkyss.redis.client.Response;

public interface Multi extends Response {

  /**
   * Adds a reply to the current response as it gets parsed from the wire.
   * @param reply the reply to add
   */
  void add(Response reply);


  /**
   * Signals that we received all required replies.
   */
  boolean complete();
}
