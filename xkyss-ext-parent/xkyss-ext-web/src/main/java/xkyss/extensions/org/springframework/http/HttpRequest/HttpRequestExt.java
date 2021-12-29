package xkyss.extensions.org.springframework.http.HttpRequest;

import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.springframework.http.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * HttpRequest 扩展
 *
 * @author xkyii
 * @createdAt 2021/08/29.
 */
@Extension
public class HttpRequestExt {

  /**
   * 从Request的Header取值,只取第一条
   * @param request 请求
   * @param key Header key
   * @return 解密后的值,如果没有找到,就返回空字符串
   */
  public static String getDecodeHeader(@This HttpRequest request, String key) {
    try {
      return getDecodeHeader(request, key, Charset.defaultCharset().name());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 从Request的Header取值,只取第一条
   * @param request 请求
   * @param key Header key
   * @return 解密后的值
   */
  public static String getDecodeHeader(@This HttpRequest request, String key, String charset) throws UnsupportedEncodingException {
    String value = getHeader(request, key);
    if (value.isNullOrEmpty()) {
      return "";
    }

    return URLDecoder.decode(value, charset);
  }

  /**
   * 从Request的Header取值,只取第一条
   * @param request 请求
   * @param key Header key
   * @return 解谜后的值
   */
  public static String getHeader(@This HttpRequest request, String key) {
    if (request == null || key.isNullOrEmpty()) {
      return "";
    }

    List<String> processorPids = request.getHeaders().get(key);
    if (processorPids.isEmpty()) {
      return "";
    }

    return processorPids.get(0);
  }
}
