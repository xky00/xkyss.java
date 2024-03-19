package com.xkyss.jsql;

import java.util.Date;

/**
 * <b>系统时间工厂</b><br>
 * 定义这个类的目的是在不影响本机物理系统时间的条件下，保持本机时间与远程服务时间的一致。<br>
 * SystemTimeFactory内部定义了一个本机物理系统时间与远程服务系统时间之间的毫秒差值，在通过SystemTimeFactory<br>
 * 获取本机系统时间时，会利用这个毫秒差值对本机物理系统时间进行修正从而获得与远程服务系统时间一直的时间值。
 * @author rechard
 *
 */
public class SystemTimeFactory {
	/**
	 * 最后修正时间的毫秒值<br>
	 * 这个值来源于System.currentTimeMillis()
	 */
	private long lastFixTime = 0;
	/**
	 * 最后修正时间的毫秒值<br>
	 * 这个值来源于System.currentTimeMillis()
	 */
	public long getLastFixTime() {
		return lastFixTime;
	}
	/**
	 * 本机物理系统时间与远程服务系统时间的毫秒差值
	 */
	private long differentMilliseconds = 0;
	/**
	 * 修正本机系统时间使其与远程服务系统时间保持一致<br>
	 * 修正操作不影响本机的物理系统时间，而只是计算并保存两个系统时间之间的毫秒差值
	 * @param serverTime 远程服务的系统时间
	 * @return 远程服务系统时间与本机物理系统时间的毫秒差值。
	 */
	public long fix(Date serverTime) {
		Date now = new Date();
		this.differentMilliseconds = serverTime.getTime() - now.getTime();
		this.lastFixTime = System.currentTimeMillis();
		return this.differentMilliseconds;
	}
	/**
	 * 获取修正以后的本机系统时间
	 * @return 修正以后的本机系统时间
	 */
	public Date now() {
		Date now = new Date();
		long nowMilliseconds = now.getTime();
		now.setTime(nowMilliseconds + this.differentMilliseconds);
		return now;
	}
	/**
	 * 单例实例
	 */
	private static SystemTimeFactory instance = new SystemTimeFactory();
	/**
	 * 获取SystemTimeFactory的单例实例
	 * @return SystemTimeFactory的单例实例
	 */
	public static SystemTimeFactory getInstance() {
		return instance;
	}
}
