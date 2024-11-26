/**
 *
 */
package com.xkyss.jsql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * <b>hibernate会话简单管理封装接口</b><br>
 * 这个接口主要用来优化Jsql内部对hibernate会话的使用。<br>
 * HibernateSessionBox有以下几个推荐性的实现原则：<br>
 * 1. 每个HibernateSessionBox仅用来封装一个hibernate会话对象，多次连续对getSession方法调用获得的同一个对象；<br>
 * 2. 如果freeSession方法被调用时，getSession方法从未以forUpdate=true被调用过，则freeSession方法将关闭会话对象<br>
 * @author rechard
 *
 */
public interface HibernateSessionBox {
    /**
     * 关闭当前SessionBox对象<br>
     * 如果指定了事务托管标记并且事务已经开启，hasError=true会回滚事务；hasError=false则会提交事务。
     * @param hasError 关闭原因，=true表示因为错误而关闭；=false表示正常关闭
     * @return 当前SessionBox对象
     */
    HibernateSessionBox close(boolean hasError);
    /**
     * 直接关闭当前SessionBox对象<br>
     * 不管是否开启了事务，都直接关闭内部会话对象。<br>
     * 主要用于取消操作。
     * @return 当前SessionBox对象
     */
    HibernateSessionBox close();
    /**
     * 获取一个可用hibernate会话对象
     * @param forUpdate 指定所获取的会话对象是否将用于更新类操作
     * @return 如果成功则返回一个可用的hibernate会话对象
     */
    Session getSession(boolean forUpdate);
    /**
     * 释放一个由getSession方法获得的hibernate会话对象<br>
     * 如果调用getSession方法时forUpdate指定的是false，则当前方法有可能关闭指定的session对象。
     * @param session 由getSession方法获得的hibernate会话对象
     * @return 当前封装接口对象
     */
    HibernateSessionBox freeSession(Session session);
    /**
     * 获取当前会话对象的事务编号<br>
     * 同事务同编号，没有开启事务也视为一个独立事务，也具有一个全局唯一的事务编号
     * @return 全局唯一的会话编号。
     */
    String getTransactionSn();
    /**
     * 获取当前会话对象在当前事务下的一个递增序列值
     * @return 当前会话对象在当前事务下的一个递增序列值
     */
    int nextTransactionSequence();
    /**
     * 获取可用hibernate会话的会话工厂
     * @return 可用hibernate会话的会话工厂
     */
    SessionFactory getSessionFactory();
    /**
     * 当前HibernateSessionBox所封装的session所属的数据源名称
     * @return 数据源名称
     */
    String getDataSourceName();
    /**
     * 当前HibernateSessionBox所封装的session所属的数据源名称
     * @param dataSourceName 数据源
     * @return Session
     */
    HibernateSessionBox setDataSourceName(String dataSourceName);
}
