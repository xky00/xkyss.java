/**
 * 
 */
package com.xkyss.jsql;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import java.io.File;
import java.util.*;

/**
 * <b>hibernate配置工具类</b><br>
 * 由于hibernate的配置信息的获取，在不同应用场合有细微差别，特定义一个工具类进行规范以便于进行扩展应用。<br>
 * HibernateConfiguration内部定义了一个静态的Configuration对象，实现一次构造，多次引用。<br>
 * @author rechard
 *
 */
public class HibernateConfiguration {

	/**
	 * 常规构造<br>
	 * 在需要访问hibernate配置的地方简单的new HibernateConfiguration()即可
	 */
	public HibernateConfiguration() {
	}
	/**
	 * 配置文件名构造<br>
	 * 进程中构造一次即可<br>
	 * 适用于hibernate配置文件放置在classpath范围内的场合
	 * @param cfgFileName hibernate配置文件名
	 */
	public HibernateConfiguration(String cfgFileName) {
		synchronized (HibernateConfiguration.syncObject) {
			HibernateConfiguration.hibernateConfigurations.add(new Configuration().configure(cfgFileName));
		}
	}
	/**
	 * 配置文件构造<br>
	 * 进程中构造一次即可<br>
	 * 适用于hibernate配置文件放置在任意物理路径的场合
	 * @param cfgFile hibernate配置文件对象
	 */
	public HibernateConfiguration(File cfgFile) {
		synchronized (HibernateConfiguration.syncObject) {
			HibernateConfiguration.hibernateConfigurations.add(new Configuration().configure(cfgFile));
		}
	}
	/**
	 * 配置对象构造<br>
	 * 进程中构造一次即可<br>
	 * 适用于已经获得正确hibernate配置对象的场合
	 * @param configuration hibernate配置对象
	 */
	public HibernateConfiguration(Configuration configuration) {
		synchronized (HibernateConfiguration.syncObject) {
			HibernateConfiguration.hibernateConfigurations.add(configuration);
		}
	}
	/**
	 * 配置对象构造<br>
	 * 进程中构造一次即可<br>
	 * 适用于已经获得正确hibernate配置对象的场合
	 * @param configuration hibernate配置对象
	 */
	public HibernateConfiguration(Configuration[] configurations) {
		synchronized (HibernateConfiguration.syncObject) {
			for (Configuration conf : configurations) {
				HibernateConfiguration.hibernateConfigurations.add(conf);
			}
		}
	}
	
	/**
	 * 所有HibernateConfiguration实例对象共享访问的hibernate配置对象
	 */
	private static Set<Configuration> hibernateConfigurations = new HashSet<Configuration>();

	/**
	 * hibernateConfiguration的同步对象
	 */
	private static Object syncObject = new Object();
	
	/**
	 * 获取hibernate配置对象
	 * @return hibernate配置对象
	 */
	public Set<Configuration> getHibernateConfigurations() {
		return HibernateConfiguration.hibernateConfigurations;
	}
	
	private static Map<Class<?>, PersistentClass> persistentClassCache = new HashMap<Class<?>, PersistentClass>();
	/**
	 * 获取指定类的PersistentClass对象
	 * @param clazz 类对象
	 * @return 如果clazz是一个可识别的hibernate映射类则返回对应的PersistentClass对象，否则返回null。
	 */
	public PersistentClass getPersistentClass(Class<?> clazz) {
		PersistentClass pc = null;
		synchronized(HibernateConfiguration.persistentClassCache) {
			pc = HibernateConfiguration.persistentClassCache.get(clazz);
		}
		if (pc != null) {
			return pc;
		}
		synchronized (HibernateConfiguration.syncObject) {
			if (HibernateConfiguration.hibernateConfigurations.size() < 1) {
				return null;
			}
			Iterator<Configuration> it = HibernateConfiguration.hibernateConfigurations.iterator();
			while (it.hasNext() && pc == null) {
				Configuration hconf = it.next();
				pc = hconf.getClassMapping(clazz.getName());
				if (pc == null) {
					try {
						hconf.addClass(clazz);
						hconf.buildMappings();
					} catch (Exception e) {
					}
					pc = hconf.getClassMapping(clazz.getName());
				}
			}
		}
		if (pc != null) {
			synchronized(HibernateConfiguration.persistentClassCache) {
				HibernateConfiguration.persistentClassCache.put(clazz, pc);
			}
		}
		return pc;
	}

	/**
	 * 去映射类对应实体表的字段
	 * @param clazz 映射类
	 * @param propertyName 属性名
	 * @return 指定映射类属性的字段
	 */
	public Column getColumn(Class<?> clazz, String propertyName) {
		PersistentClass pc = this.getPersistentClass(clazz);
		if (pc == null){
			return null;
		}
		Property property = null;
		try {
			property = pc.getProperty(propertyName);
		} catch (Exception e) {}
		if (property != null) {
			Iterator<?> it = property.getColumnIterator();
			if (it != null && it.hasNext()) {
				return (Column) it.next();
			}
		}
		return null;
	}
	
	/**
	 * 取映射类的实体表名
	 * @param clazz 映射类
	 * @return 实体表名
	 */
	public String getTableName(Class<?> clazz) {
		PersistentClass pc = this.getPersistentClass(clazz);
		if (pc != null){
			return pc.getTable().getName();
		}
		return null;
	}
	/**
	 * 缺省的主数据源会话工厂
	 */
	private static SessionFactory defaultMasterSessionFactory;
	/**
	 * 缺省的从数据源会话工厂
	 */
	private static SessionFactory defaultSlaveSessionFactory;
	/**
	 * 缺省的扩展外部数据源会话工厂
	 */
	private static List<SessionFactory> defaultExtSessionFactorys;

	public SessionFactory getDefaultMasterSessionFactory() {
		return HibernateConfiguration.defaultMasterSessionFactory;
	}
	public void setDefaultMasterSessionFactory(SessionFactory masterSessionFactory) {
		HibernateConfiguration.defaultMasterSessionFactory = masterSessionFactory;
	}
	public SessionFactory getDefaultSlaveSessionFactory() {
		return HibernateConfiguration.defaultSlaveSessionFactory;
	}
	public void setDefaultSlaveSessionFactory(SessionFactory slaveSessionFactory) {
		HibernateConfiguration.defaultSlaveSessionFactory = slaveSessionFactory;
	}
	public List<SessionFactory> getDefaultExtSessionFactorys() {
		return HibernateConfiguration.defaultExtSessionFactorys;
	}
	public void setDefaultExtSessionFactorys(List<SessionFactory> extSessionFactorys) {
		HibernateConfiguration.defaultExtSessionFactorys = extSessionFactorys;
	}
}
