/**
 * 
 */
package com.xkyss.jsql;

import org.hibernate.Session;

/**
 * <b>Jsql对象doWork方法的回调接口</b><br>
 * 这个接口参考了hibernate Session的Work接口，通过Jsql.doWork方法实现对Jsql内部Session的安全访问。
 * @author rechard
 *
 */
public interface JsqlWork {
	/**
	 * 应用代码实现这个方法以获取Jsql对象内部Session对象直接进行hibernate操作
	 * @param session 可用的hibernate会话对象，使用完成后不要close()。
	 * @throws Exception 实现层可能抛出的异常
	 */
	void work(Session session) throws Exception;
}
