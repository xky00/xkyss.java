package xkyss.extensions.org.springframework.http.server.ServerHttpRequest;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.springframework.http.server.ServerHttpRequest;

@Extension
public class ServerHttpRequestExt {

  public static void helloWorld(@This ServerHttpRequest self) {
    System.out.println("hello world!");
  }

}