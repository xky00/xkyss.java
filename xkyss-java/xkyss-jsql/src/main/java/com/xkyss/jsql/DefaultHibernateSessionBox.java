/**
 * 
 */
package com.xkyss.jsql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.LocalStatus;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.Statistics;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author rechard
 *
 */
public class DefaultHibernateSessionBox implements HibernateSessionBox {
	/**
	 * hibernate会话对象
	 */
	private Session session;
	/**
	 * session对象通过getSession方法被引用的计数器
	 */
	private long refCount;
	/**
	 * 当前HibernateSessionBox是否已经被关闭
	 */
	private boolean isClosed;
	/**
	 * 有效的hibernate会话工厂
	 */
	private SessionFactory sessionFactory;
	/**
	 * session或者sessionFactory所述的数据源名称
	 */
	private String dataSourceName;
	/**
	 * 事务托管标记
	 */
	private boolean transactionTrusteeship;
	/**
	 * 标记当前会话对象是否由当前Box创建
	 */
	private boolean createFromThis = false;
	
	private String statisticsItemName;

	private SystemTimeFactory systemTimeFactory;
	/**
	 * @param session 有效的hibernate会话对象
	 */
	public DefaultHibernateSessionBox(Session session) {
		this.session = session;
		this.transactionTrusteeship = false;
		this.isClosed = false;
		this.refCount = 1;
	}
	/**
	 * @param sessionFactory 有效的hibernate工厂
	 * @param transactionTrusteeship 事务托管标记，为true表示由HibernateSessionBox自动控制和管理事务
	 */
	public DefaultHibernateSessionBox(SessionFactory sessionFactory, boolean transactionTrusteeship) {
		this.sessionFactory = sessionFactory;
		this.transactionTrusteeship = transactionTrusteeship;
		this.isClosed = false;
		this.refCount = 0;
	}
	// private void updateStatistics() {
	// 	if (this.statisticsItemName != null) {
	// 		Statistics stat = null;
	// 		if (this.sessionFactory != null) {
	// 			stat = this.sessionFactory.getStatistics();
	// 		} else if (this.session != null) {
	// 			stat = this.session.getSessionFactory().getStatistics();
	// 		}
	// 		if (stat != null) {
	// 			long count = stat.getSessionOpenCount() - stat.getSessionCloseCount();
	// 			if (count != F.runtimeStatisticsTable().getCount(this.statisticsItemName)) {
	// 				F.runtimeStatisticsTable().setCount(
	// 						this.statisticsItemName,
	// 						count);
	//
	// 				String maxItemName = this.statisticsItemName + "峰值";
	// 				long maxCount = F.runtimeStatisticsTable().getCount(maxItemName);
	// 				if (maxCount < count) {
	// 					F.runtimeStatisticsTable().setCount(maxItemName, count);
	// 				}
	// 			}
	// 		}
	// 	}
	// }
	private static Map<SessionFactory, String> statisticsItemNames = new HashMap<SessionFactory, String>();
	private String buildStatisticsItemName(Session session) {
		final String[] itemName = new String[1];
		synchronized (statisticsItemNames) {
			itemName[0] = statisticsItemNames.get(session.getSessionFactory());
			if (itemName[0] == null) {
				session.doWork(new Work(){
					@Override
					public void execute(Connection conn) throws SQLException {
						try {
							DatabaseMetaData dmd = conn.getMetaData();
							String url = dmd.getURL();
							String dbinfo = null;
							if (url.indexOf("@") > 0) {
								dbinfo = url.split("@")[1];
							} else if (url.indexOf("//") > 0) {
								dbinfo = url.split("//")[1];
								int index = dbinfo.indexOf("?");
								if (index > 0) {
									dbinfo = dbinfo.substring(0, index);
								}
							}
							itemName[0] = String.format("运行时统计.%s活跃会话数", dbinfo);
						} catch (Exception e) {
						}
					}});
				if (itemName[0] != null) {
					statisticsItemNames.put(session.getSessionFactory(), itemName[0]);
				}
			}
		}
		return itemName[0];
	}
	@Override
	public HibernateSessionBox freeSession(
			Session session) {
		synchronized (this) {
			if (!this.isClosed) {
				if (session == this.session) {
					this.refCount--;
					if (this.refCount == 0 && 
						(session.getTransaction() == null || 
						LocalStatus.NOT_ACTIVE == session.getTransaction().getLocalStatus())) {
						// 会话引用计数为0并且没有开启事务则关闭之
						try {
							this.session.close();
							// this.updateStatistics();
						} catch (Exception e) {
						} finally {
							this.session = null;
						}
					}
				}
			}
		}
		return this;
	}
	@Override
	public Session getSession(boolean forUpdate) {
		synchronized (this) {
			if (!this.isClosed) {
				if (this.session == null) {
					this.session = this.createSession();
					if (this.session != null) {
						this.refCount = 1;
					}
				} else {
					this.refCount++;
				}
				if (this.session != null) {
					if (this.statisticsItemName == null) {
						this.statisticsItemName = this.buildStatisticsItemName(this.session);
						// this.updateStatistics();
					}
				}
				if (this.session != null && forUpdate && this.transactionTrusteeship && 
					(this.session.getTransaction() == null || LocalStatus.NOT_ACTIVE == this.session.getTransaction().getLocalStatus())) {
					// 是用于更新的会话对象而且当前HibernateSessionBox被指定要进行事务托管则自动开启事务
					this.session.beginTransaction();
				}
			}
		}
		return this.session;
	}
	/**
	 * 标记当前会话对象是否由当前Box创建
	 */
	private boolean isCreateFromThis() {
		return createFromThis;
	}
	/**
	 * 创建一个hibernate会话对象
	 * @return
	 */
	private Session createSession() {
		Session session = null;
		session = this.sessionFactory.openSession();
		if (session != null) {
			boolean error = true;
			try {
				session.doWork(new Work(){
					@Override
					public void execute(Connection arg0) throws SQLException {
					}});
				this.createFromThis = true;
				error = false;
			} finally {
				if (error) {
					try {
						session.close();
					} catch (Exception e) {}
				}
			}
		}
//		F.log().d(String.format("'%s'创建了会话(%d),%s", HibernateProcessorAbstract.this.getClass().getSimpleName(), session.hashCode(), Thread.currentThread().getName()));
		return session;
	}
	/* (non-Javadoc)
	 * @see com.aj.frame.db.hibernate.jsql.HibernateSessionBox#close(boolean)
	 */
	@Override
	public HibernateSessionBox close(boolean hasError) {
		synchronized (this) {
			if (!this.isClosed) {
				this.isClosed = true;
				try {
					if (this.session != null && this.transactionTrusteeship && 
						this.session.getTransaction() != null &&
						LocalStatus.ACTIVE == this.session.getTransaction().getLocalStatus()) {
						if (hasError) {
							this.session.getTransaction().rollback();
//							F.log().d(String.format("'%s'回滚了会话(%d)的事务(%d),%s", HibernateProcessorAbstract.this.getClass().getSimpleName(), this.session.hashCode(), this.transaction.hashCode(), Thread.currentThread().getName()));
						} else {
							this.session.getTransaction().commit();
//							F.log().d(String.format("'%s'提交了会话(%d)的事务(%d),%s", HibernateProcessorAbstract.this.getClass().getSimpleName(), this.session.hashCode(), this.transaction.hashCode(), Thread.currentThread().getName()));
						}
					}
				} finally {
					if (this.session != null && this.isCreateFromThis()) {
						try {
//							F.log().d(String.format("'%s'关闭了会话(%d),hasError=%s,%s", HibernateProcessorAbstract.this.getClass().getSimpleName(), this.session.hashCode(), hasError, Thread.currentThread().getName()));
							this.session.close();
							// this.updateStatistics();
						} finally {
							this.session = null;
						}
					}
				}
			}
		}
		return this;
	}
	private Transaction currentTransaction;
	private String currentTransactionSn;
	@Override
	public String getTransactionSn() {
		Session currentSession = this.session;
		if (currentSession != null) {
			Transaction transaction = currentSession.getTransaction();
			if (transaction != null) {
				boolean resetSequence = false;
				synchronized (this) {
					if (this.currentTransaction == null || transaction != this.currentTransaction) {
						this.currentTransactionSn = 
								String.format("%016d_%s", this.systemTimeFactory.now().getTime(), UUID.randomUUID().toString().toLowerCase());
						this.currentTransaction = transaction;
						resetSequence = true;
					}
				}
				if (resetSequence) {
					synchronized (this.sequence) {
						this.sequence[0] = 0;
					}
				}
				return this.currentTransactionSn;
			}
		}
		synchronized (this.sequence) {
			this.sequence[0] = 0;
		}
		return String.format("%016d_%s", this.systemTimeFactory.now().getTime(), UUID.randomUUID().toString().toLowerCase());

	}
	@Override
	public SessionFactory getSessionFactory() {
		SessionFactory factory = this.sessionFactory;
		if (factory == null && this.session != null) {
			factory = this.session.getSessionFactory();
		}
		return factory;
	}
	private final int[] sequence = new int[1];
	@Override
	public int nextTransactionSequence() {
		synchronized (this.sequence) {
			return this.sequence[0]++;
		}
	}
	@Override
	public String getDataSourceName() {
		return this.dataSourceName;
	}
	@Override
	public HibernateSessionBox setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
		return this;
	}

	public DefaultHibernateSessionBox setSystemTimeFactory(SystemTimeFactory systemTimeFactory) {
		this.systemTimeFactory = systemTimeFactory;
		return this;
	}

	@Override
	public HibernateSessionBox close() {
		synchronized (this) {
			if (!this.isClosed) {
				this.isClosed = true;
				if (this.session != null && this.isCreateFromThis()) {
					try {
//						F.log().d(String.format("'%s'关闭了会话(%d),hasError=%s,%s", HibernateProcessorAbstract.this.getClass().getSimpleName(), this.session.hashCode(), hasError, Thread.currentThread().getName()));
						this.session.close();
						// this.updateStatistics();
					} finally {
						this.session = null;
					}
				}
			}
		}
		return this;
	}
}
