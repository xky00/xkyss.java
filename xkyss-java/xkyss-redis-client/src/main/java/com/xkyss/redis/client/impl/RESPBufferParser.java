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
package com.xkyss.redis.client.impl;

import com.xkyss.redis.client.Response;
import com.xkyss.redis.client.ResponseType;
import com.xkyss.redis.client.impl.types.*;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public final class RESPBufferParser implements Handler<Buffer> {

  public static final String VERSION = "3";

  // 512Mb
  private static final long MAX_STRING_LENGTH = 536870912;

  // the callback when a full response message has been decoded
  private final BufferParserHandler handler;
  // a composite buffer to allow buffer concatenation as if it was
  // a long stream
  private final ReadableBuffer buffer = new ReadableBuffer();
  // arrays can have nested objects so we need to keep track of the
  // nesting while parsing
  private final ArrayStack stack;

  RESPBufferParser(BufferParserHandler handler, int maxStack) {
    this.handler = handler;
    this.stack = new ArrayStack(maxStack);
  }

  // parser state machine state
  private boolean eol = true;
  private int bytesNeeded = 0;
  private int lengthNeeded = 0;
  private boolean verbatim = false;

  @Override
  public void handle(Buffer chunk) {
    // add the chunk to the buffer
    buffer.append(chunk);

    while (buffer.readableBytes() >= (eol ? 3 : bytesNeeded != -1 ? bytesNeeded + 2 : 0)) {
      // setup a rollback point
      buffer.mark();

      // we need to locate the eol
      if (eol) {
        // this is the begin of a message
        final byte type = buffer.readByte();

        // locate the eol and handle as a C string
        final int start = buffer.getOffset();
        final int eol = buffer.findLineEnd();

        // not found at all
        if (eol == -1) {
          buffer.reset();
          break;
        }

        // special case for sync messages or messages that report the wrong length
        if (start == eol) {
          buffer.reset();
          break;
        }

        switch (type) {
          case '+': // Simple Strings
            handleSimpleString(start, eol);
            break;
          case '-': // Simple Errors
            handleSimpleError(eol);
            break;
          case '!':
            handleBulkError(eol);
            break;
          case ':': // Integers
          case ',': // Doubles
          case '(': // Big numbers
            handleNumber(type, eol);
            break;
          case '=': // Verbatim strings
            handleBulk(eol, true);
            break;
          case '$': // Bulk strings
            handleBulk(eol, false);
            break;
          case '*': // Arrays
          case '%': // Maps
          case '~': // Sets
            handleMulti(type, eol);
            break;
          case '_': // Nulls
            handleNull(eol);
            break;
          case '#': // Booleans
            handleBoolean(eol);
            break;
          case '|': // Attributes
            handleAttribute(eol);
            break;
          case '>': // Pushes
            handlePush(eol);
            break;
          default:
            // notify
            handler.fail(ErrorType.create("ILLEGAL_STATE Unknown RESP type " + (char) type));
            return;
        }
      } else {
        // empty string
        if (bytesNeeded == 0) {
          // special case as we don't need to allocate objects for this
          // handleResponse(BulkType.EMPTY, false);
        } else {
          // fixed length parsing && read the required bytes
          //handleResponse(BulkType.create(buffer.readBytes(bytesNeeded), verbatim), false);
          buffer.readBytes(bytesNeeded);
          buffer.skip(2); // \r\n
          handleResponse(null, false);
          buffer.skip(-2); // \r\n
          // clear the verbatim
          verbatim = false;
        }
        // clean up the buffer, skip to the last \r\n
        if (buffer.skipEOL()) {
          // switch back to eol parsing
          eol = true;
        } else {
          // operation failed
          buffer.reset();
        }
      }
    }
  }

  private void handleSimpleError(int eol) {
    buffer.readLine(eol);
    handleResponse(null, false);
  }

  private void handleNumber(byte type, int eol) {
    switch (type) {
      case ':':
        buffer.readNumber(eol, ReadableBuffer.NumericType.INTEGER);
        handleResponse(null, false);
        break;
      case ',':
        buffer.readNumber(eol, ReadableBuffer.NumericType.DECIMAL);
        handleResponse(null, false);
        break;
      case '(':
        buffer.readNumber(eol, ReadableBuffer.NumericType.BIGINTEGER);
        handleResponse(null, false);
        break;
      default:
        handler.fail(new NumberFormatException("Invalid REDIS format: [" + (char) type + "]"));
        break;
    }
  }

  private long handleLength(int eol) {
    final int offset = buffer.getOffset();
    final long integer = buffer.readLong(eol);
    lengthNeeded = buffer.getOffset() - offset + 1;

    // special cases
    // redis multi cannot have more than 2GB elements
    if (integer > Integer.MAX_VALUE) {
      handler.fail(ErrorType.create("ILLEGAL_STATE Redis Multi cannot be larger 2GB elements"));
      return -1;
    }

    if (integer < 0) {
      if (integer == -1L) {
        // this is a NULL array
        handleResponse(null, false);
        return -1;
      }
      // other negative values are not valid
      handler.fail(ErrorType.create("ILLEGAL_STATE Redis Multi cannot have negative length"));
      return -1;
    }

    return integer;
  }

  private void handlePush(int eol) {
    long len = handleLength(eol);
    if (len >= 0) {
      if (len == 0L) {
        // push always have 1 entry
        handler.fail(ErrorType.create("ILLEGAL_STATE Redis Push must have at least 1 element"));
      } else {
        handleResponse(new Counter((int) len), true);
      }
    }
  }

  private void handleAttribute(int eol) {
    long len = handleLength(eol);
    if (len >= 0L) {
      if (len == 0L) {
        // push always have 1 entry
        handler.fail(ErrorType.create("ILLEGAL_STATE Redis Push must have at least 1 element"));
      } else {
        handleResponse(new Counter((int) len * 2), true);
        // handleResponse(AttributeType.create(len), true);
      }
    }
  }

  private void handleBoolean(int eol) {
    byte value = buffer.readByte();
    switch (value) {
      case 't':
      case 'f':
        buffer.skipEOL();
        handleResponse(null, false);
        break;
      default:
        handler.fail(ErrorType.create("Invalid boolean value: " + ((char) value)));
    }
  }

  private void handleSimpleString(int start, int eol) {
    buffer.readLine(eol);
    handleResponse(null, false);
  }

  private void handleBulkError(int eol) {
    handleBulk(eol, false);
  }

  private void handleBulk(int eol, boolean verbatim) {
    long len = handleLength(eol);
    if (len >= 0L) {
      // redis strings cannot be longer than 512Mb
      if (len > MAX_STRING_LENGTH) {
        handler.fail(ErrorType.create("ILLEGAL_STATE Redis Bulk cannot be larger than 512MB"));
        return;
      }
      // safe cast
      bytesNeeded = (int) len;
      // in this case we switch from eol parsing to fixed len parsing
      this.eol = false;
      this.verbatim = verbatim;
    }
  }

  private void handleMulti(byte type, int eol) {

    long len = handleLength(eol);

    if (len >= 0L) {
      // empty arrays can be cached and require no further processing
      if (len == 0L) {
        // handleResponse(type == '%' ? MultiType.EMPTY_MAP : MultiType.EMPTY_MULTI, false);
      } else {
        boolean asMap = type == '%';
        Counter counter = new Counter(asMap ? (int) (len * 2) : (int) len);
        handleResponse(counter, true);
      }
    }
  }

  private void handleNull(int eol) {
    buffer.skipEOL();
    handleResponse(null, false);
  }

  private void handleResponse(Counter counter, boolean push) {
    final Counter multi = stack.peek();
    // verify if there are multi's on the stack
    if (multi != null) {
      // add the parsed response to the multi
      multi.add(counter);
      // push the given response to the stack
      if (push) {
        stack.push(counter);
      } else {
        // break the chain and verify end condition
        Counter m = multi;
        // clean up complete messages
        while (m.complete()) {
          stack.pop();

          // in case of chaining we need to take into account
          // if the stack is empty or not
          if (stack.empty()) {
            // if (m.type() != ResponseType.ATTRIBUTE) {
            //   // handle the multi to the listener
            handleCallback();
            // }
            return;
          }
          // peek into the next entry
          m = stack.peek();

          if (m == null) {
            handler.fail(ErrorType.create("ILLEGAL_STATE Multi can't be null"));
            return;
          }
        }
      }
    } else {
      if (push) {
        stack.push(counter);
      } else {
        // there's nothing on the stack
        // so we can handle the response directly
        // to the listener
        handleCallback();
      }
    }
  }

  private void handleCallback() {
    int offset = buffer.getOffset();
    int start = buffer.getStart();
    int mark = buffer.getMark();
    buffer.reset(start);
    handler.handle(buffer.readBytes(offset-start));
    buffer.setStart(offset);
    buffer.setMark(mark);
  }

  static class Counter {
    // 记录总大小
    private final int size;
    // 记录当前大小
    int count = 0;

    Counter(int size) {
      this.size = size;
    }

    int size() {
      return this.size;
    }

    int count() {
      return this.count;
    }

    boolean complete() {
      return count == size;
    }

    void add(Counter c) {
      this.count++;
    }
  }
}
