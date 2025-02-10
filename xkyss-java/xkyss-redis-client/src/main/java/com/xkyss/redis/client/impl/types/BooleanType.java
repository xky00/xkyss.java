/*
 * Copyright 2019 Red Hat, Inc.
 * <p>
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * <p>
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p>
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 * <p>
 * You may elect to redistribute this code under either of these licenses.
 */
package com.xkyss.redis.client.impl.types;

import com.xkyss.redis.client.Response;
import com.xkyss.redis.client.ResponseType;

public final class BooleanType implements Response {

  public static final BooleanType TRUE = new BooleanType(true);
  public static final BooleanType FALSE = new BooleanType(false);

  public static BooleanType create(Boolean value) {
    return new BooleanType(value);
  }

  private final Boolean value;

  private BooleanType(Boolean value) {
    this.value = value;
  }

  @Override
  public ResponseType type() {
    return ResponseType.BOOLEAN;
  }

  @Override
  public Boolean toBoolean() {
    return value;
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
