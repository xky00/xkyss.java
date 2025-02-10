package com.xkyss.redis.client.impl;

import com.xkyss.redis.client.impl.keys.BeginSearch;
import com.xkyss.redis.client.impl.keys.FindKeys;

public final class KeyLocator {

  public final Boolean ro;
  public final BeginSearch begin;
  public final FindKeys find;

  public KeyLocator(Boolean ro, BeginSearch begin, FindKeys find) {
    this.ro = ro;
    this.begin = begin;
    this.find = find;
  }
}
