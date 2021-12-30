package xkyss.extensions.org.springframework.http.server.reactive.ServerHttpRequest;

import manifold.ext.rt.api.Extension;
//import manifold.ext.rt.api.This;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.util.MultiValueMap;
import xkyss.extensions.org.springframework.http.HttpRequest.HttpRequestExt;

//import java.io.UnsupportedEncodingException;
//import java.net.URLDecoder;
//import java.nio.charset.Charset;


@Extension
public class ServerHttpRequestExt extends HttpRequestExt {
//
//  /**
//   * 从Request的Query取值,只取第一条
//   * @param request 请求
//   * @param key Query key
//   * @return 解密后的值,如果没有找到,就返回空字符串
//   */
//  public static String getDecodedQuery(@This ServerHttpRequest request, String key) {
//    try {
//      return getDecodedQuery(request, key, Charset.defaultCharset().name());
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//      return "";
//    }
//  }
//
//  /**
//   * 从Request的Query取值,只取第一条
//   * @param request 请求
//   * @param key Query key
//   * @return 解密后的值
//   */
//  public static String getDecodedQuery(@This ServerHttpRequest request, String key, String charset) throws UnsupportedEncodingException {
//    String value = getQuery(request, key);
//    if (value.isNullOrEmpty()) {
//      return "";
//    }
//
//    return URLDecoder.decode(value, charset);
//  }
//
//  /**
//   * 从Request的Query取值,只取第一条
//   * @param request 请求
//   * @param key Query key
//   * @return 解密后的值
//   */
//  public static String getQuery(@This ServerHttpRequest request, String key) {
//    if (request == null || key.isNullOrEmpty()) {
//      return "";
//    }
//
//    MultiValueMap<String, String> queryParams = request.getQueryParams();
//    if (queryParams.isEmpty()) {
//      return "";
//    }
//
//    String r = queryParams.getFirst(key);
//    return (r == null) ? "" : r;
//  }
}