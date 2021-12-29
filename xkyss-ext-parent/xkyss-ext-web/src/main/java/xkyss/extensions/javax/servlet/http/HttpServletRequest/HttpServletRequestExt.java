package xkyss.extensions.javax.servlet.http.HttpServletRequest;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * HttpServletRequest 扩展
 *
 * @author xkyii
 * @createdAt 2021/08/29.
 */
@Extension
public class HttpServletRequestExt {

  /**
   * 从Request的Header取值
   * @param request 请求
   * @param key Header key
   * @return 解密后的值
   */
  public static String getDecodeHeader(@This HttpServletRequest request, String key) {

    String value = getHeader(request, key);
    if (value.isNullOrEmpty()) {
      return "";
    }

    try {
      return URLDecoder.decode(value, Charset.defaultCharset().name());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }

  }

  /**
   * 从Request的Header取值
   * @param request 请求
   * @param key Header key
   * @return 解谜后的值
   */
  public static String getHeader(@This HttpServletRequest request, String key) {
    if (request == null || key.isNullOrEmpty()) {
      return "";
    }

    return request.getHeader(key);
  }

  /**
   * 从Cookie中取值
   * @param request 请求
   * @param key 取值索引
   * @return 值
   */
  public static String getCookie(@This HttpServletRequest request, String key) {
    Cookie[] cookies = request.getCookies();

    if (cookies.isNotEmpty()) {
      for (Cookie cookie: cookies) {
        if (key.equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    return "";
  }
}
