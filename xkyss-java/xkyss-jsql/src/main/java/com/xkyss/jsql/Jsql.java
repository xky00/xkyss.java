package com.xkyss.jsql;


import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.hibernate.jdbc.Work;
import org.hibernate.mapping.Column;
import org.hibernate.metadata.ClassMetadata;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <b>用Java对象描述SQL语法的DB工具类</b><br>
 * Jsql存在的意义在于实现更彻底的对象化DB应用，因此仅支持基于hibernate的对象持久化技术。<br>
 * Jsql的实现原理是通过Java对象操作指定参数，然后根据参数合成标准的HQL，最后由hibernate执行HQL完成实际的对象持久化。
 * @author rechard
 *
 */
public class Jsql {
    private static String callMeAt() {
        // if (F.log().getLevel() > AJLogger.DEBUG) {
        //     return "";
        // }
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        StackTraceElement lastDidme = null;
        for (int i = sts.length - 1; i >= 0; i--) {
            if (sts[i].getClassName().indexOf(Jsql.class.getName()) == 0) {
                break;
            }
            lastDidme = sts[i];
        }
        return String.format("[错误源码位置在 %s.%s(%s:%d)]", lastDidme.getClassName(), lastDidme.getMethodName(), lastDidme.getFileName(), lastDidme.getLineNumber());
    }
    /**
     * <b>参数命名ID</b>
     */
    private class ParamNameId {
        /**
         * ID计数
         */
        private int paramNameId = 0;
        /**
         * 复位计数
         * @return 当前参数命名ID对象
         */
        public ParamNameId reset() {
            this.paramNameId = 0;
            return this;
        }
        /**
         * 获取新的ID值
         * @return 新的ID值
         */
        public int nextParamNameId() {
            return this.paramNameId++;
        }
    }
    /**
     * <b>逻辑语法的类型定义</b>
     */
    private enum LogicType {
        Start, // 一个逻辑语法集合的起始逻辑
        AndLogic,  // 与逻辑
        OrLogic // 或逻辑
    }
    /**
     * <b>语法抽象接口</b><br>
     * Grammar是对组成一个完整SQL语句的各种语法的抽象定义接口，这里没有使用interface而使用abstract，是考虑对应用层隐藏一部分接口方法。
     */
    public abstract class Grammar {
        /**
         * 克隆当前语法对象
         * @return 克隆对象。
         */
        protected abstract Grammar clone();
        /**
         * 生成HQL
         * @param building 用来进行HQL字符串合并的StringBuilder对象
         * @param params 生成过程中构造的HQL参量，其中key=参量名；value=参量值
         * @return 如果没有严重逻辑错误则当前生成对象，否则返回null，错误或警告信息直接输出调试信息。
         */
        public abstract Grammar build(StringBuilder building, Map<String, Object> params);
        /**
         * 返回与当前语法对象绑定的有效hibernate会话对象
         * @return 有效的hibernate会话对象
         */
        public abstract Session currentSession();
        /**
         * 获取hibernate会话对象的管理封装对象
         * @return 获取有效hibernate会话对象的管理封装对象
         */
        protected abstract HibernateSessionBox getSessionBox();
        /**
         * 设置当前语法对象的父对象
         * @param parent 父语法对象
         * @return 当前语法对象
         */
        protected abstract Grammar setParent(Grammar parent);
        /**
         * 获取当前语法对象的父对象
         * @return 当前语法对象的父对象
         */
        protected abstract Grammar getParent();
        /**
         * 当前语法对象的子语法对象
         * @return 当前语法对象的子语法对象
         */
        protected abstract List<Grammar> subs();
        /**
         * 获取一个用来保障HQL参量名称语法唯一性的ID值<br>
         * 生成原则是：总是返回根语法对象的该属性值。
         * @return 用来保障HQL参量名称语法唯一性的ID值
         */
        protected abstract int paramNameId();
        /**
         * 获得语法对象的参数命名ID对象
         * @return 参数命名ID对象
         */
        protected abstract ParamNameId getParamNameId();
        /**
         * 设置语法对象的参数命名ID对象
         * @param paramNameId 参数命名ID对象
         */
        protected abstract void setParamNameId(ParamNameId paramNameId);
        /**
         * 目标别名<br>
         * 除非当前语法对象是Select、Update、Delete语法对象之一，否则返回父对象的该属性值。
         * @return 目标别名
         */
        protected abstract String alias();
        /**
         * 清理当前语法对象<br>
         * 清理过程使当前语法对象与下级语法对象脱离父子关系
         * @return 当前语法对象
         */
        protected abstract Grammar clear();
        /**
         * 获得当前语法对象树的根对象
         * @return 当前语法对象树的根对象
         */
        protected abstract Grammar root();
        /**
         * 获取结果类型
         * @return 结果类型
         */
        protected abstract Class<?> resultClass();
        /**
         * 获取操作目标
         * @return 操作目标
         */
        protected abstract Object target();
        /**
         * 获得当前语法对象树中的根逻辑语法对象
         * @return 当前语法对象树中的根逻辑语法对象
         */
        protected abstract Grammar rootLogic();
        /**
         * Jsql执行回调机制开关
         */
        protected abstract boolean isEnableExecuteCallback();
        /**
         * Jsql执行回调机制开关
         */
        protected abstract void setEnableExecuteCallback(boolean enableExecuteCallback);
    }
    /**
     * <b>语法对象实现类的抽象基类</b>
     */
    private abstract class GrammarAbstract extends Grammar {
        @Override
        protected boolean isEnableExecuteCallback() {
            Grammar parent = this.getParent();
            if (parent != null) {
                return parent.isEnableExecuteCallback();
            }
            return false;
        }
        @Override
        protected void setEnableExecuteCallback(boolean enableExecuteCallback) {
            Grammar parent = this.getParent();
            if (parent != null) {
                parent.setEnableExecuteCallback(enableExecuteCallback);
            }
        }
        @Override
        protected Grammar clone() {
            // 暂时不实现克隆
            return this;
        }
        protected void cloneSubs(Grammar cloneObject) {
            if (this.subs().size() > 0) {
                Iterator<Grammar> it = this.subs().iterator();
                while (it.hasNext()) {
                    Grammar sub = it.next();
                    Grammar clsub = sub.clone();
                    clsub.setParent(cloneObject);
                    cloneObject.subs().add(clsub);
                }
            }
        }
        private GrammarAbstract() {
        }
        @Override
        public Session currentSession() {
            HibernateSessionBox sessionBox = this.getSessionBox();
            return sessionBox == null ? null : sessionBox.getSession(true);
        }
        @Override
        protected HibernateSessionBox getSessionBox() {
            if (this.getParent() == null) {
                return null;
            }
            return this.root().getSessionBox();
        }
        private Grammar parent;
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#setParent(com.aj.frame.db.jsql.Jsql.Grammar)
         */
        @Override
        protected Grammar setParent(Grammar parent) {
            this.parent = parent;
            return this;
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#getParent()
         */
        @Override
        protected Grammar getParent() {
            return this.parent;
        }
        private List<Grammar> subs = new ArrayList<Grammar>();
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#subs()
         */
        @Override
        protected List<Grammar> subs() {
            return this.subs;
        }
        private ParamNameId paramNameId = new ParamNameId();
        @Override
        protected ParamNameId getParamNameId() {
            if (this.getParent() != null) {
                return this.root().getParamNameId();
            }
            return this.paramNameId;
        }
        @Override
        protected void setParamNameId(ParamNameId paramNameId) {
            if (this.getParent() != null) {
                this.root().setParamNameId(paramNameId);
            }
            this.paramNameId = paramNameId;
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#paramNameId()
         */
        @Override
        protected int paramNameId() {
            if (this.getParent() != null) {
                return this.root().paramNameId();
            }
            return this.paramNameId.nextParamNameId();
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#alias()
         */
        @Override
        protected String alias() {
            if (this.getParent() != null) {
                return this.getParent().alias();
            }
            return null;
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#clear()
         */
        @Override
        protected Grammar clear() {
            Iterator<Grammar> it = this.subs().iterator();
            while (it.hasNext()) {
                Grammar sub = it.next();
                sub.clear();
                sub.setParent(null);
            }
            this.subs().clear();
            this.paramNameId.reset();
            return this;
        }
        @Override
        protected Grammar rootLogic() {
            Logic<?, ?> logic = (this instanceof Logic) ? (Logic<?, ?>)this : null;
            while (logic != null) {
                Grammar parent = logic.getParent();
                if (parent != null && parent instanceof Logic) {
                    logic = (Logic<?, ?>)parent;
                    continue;
                }
                break;
            }
            return logic;
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#root()
         */
        @Override
        protected Grammar root() {
            Grammar root = this;
            while (root.getParent() != null) {
                root = root.getParent();
            }
            return root;
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#resultClass()
         */
        @Override
        protected Class<?> resultClass() {
            return this.getParent() == null ? null : this.getParent().resultClass();
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#target()
         */
        @Override
        protected Object target() {
            return this.getParent() == null ? null : this.getParent().target();
        }
        /**
         * 根据当前语法对象所在的语法树，生成可以直接执行的HQL字符串<br>
         * 生成HQL字符串的目的在于调试，不建议直接用于对象持久化操作。
         * @return 可以直接执行的HQL字符串
         */
        @Override
        public String toString() {
            return this.toString(false);
        }
        /**
         * 根据当前语法对象所在的语法树，生成可以直接执行的SQL字符串<br>
         * 生成SQL字符串的目的在于调试，不建议直接用于对象持久化操作。
         * @return 可以直接执行的SQL字符串
         */
        public String toSql() {
            return "\r\n" + this.toString(true) + "\r\n";
        }
        /**
         * 根据当前语法对象所在的语法树，生成可以直接执行的SQL或者HQL字符串<br>
         * @param buildAsSql 指定true则生成SQL，否则生成HQL
         * @return 可以直接执行的SQL或者HQL字符串
         */
        private String toString(boolean buildAsSql) {
            StringBuilder building = new StringBuilder();
            Map<String, Object> params = new HashMap<String, Object>();
            GrammarAbstract root = (GrammarAbstract) this.root();
            root.setBuildAsSql(buildAsSql);
            root.build(building, params);
            root.setBuildAsSql(false);
            java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String hql = building.toString();
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> en = it.next();
                Object val = en.getValue();
                String valString = (val instanceof String || val instanceof Date) ? "'" : "";
                valString += val instanceof Date ? format.format((Date)val) : val;
                valString += (val instanceof String || val instanceof Date) ? "'" : "";
                hql = hql.replaceAll(":" + en.getKey(), valString);
            }
            return hql;
        }
        private boolean buildAsSql;
        protected boolean isBuildAsSql() {
            if (this.getParent() == null) {
                return this.buildAsSql;
            }
            return ((GrammarAbstract)this.root()).isBuildAsSql();
        }
        protected GrammarAbstract setBuildAsSql(boolean buildAsSql) {
            this.buildAsSql = buildAsSql;
            return this;
        }
        protected Class<?> getTargetClass() {
            Class<?> cls = null;
            Object target = this.target();
            while (target != null && target instanceof Grammar) {
                target = ((Grammar)target).target();
            }
            if (target != null && target instanceof Class) {
                cls = (Class<?>) target;
            }
            return cls;
        }
        /**
         * 根据buildAsSql标记返回属性名或者字段名
         * @param propName 属性名
         * @return 属性名或者字段名
         */
        protected String getPropertyName(String propName) {
            if (this.isBuildAsSql() && this.target() != null) {
                Class<?> targetClass = this.getTargetClass();
                if (targetClass != null) {
                    HibernateConfiguration hc = new HibernateConfiguration();
                    Column col = hc.getColumn((Class<?>)targetClass, propName);
                    if (col != null) {
                        return col.getName();
                    }
                }
            }
            return propName;
        }
        /**
         * 根据buildAsSql标记返回类名或者表名
         * @param cls 目标类
         * @return 类名或者表名
         */
        protected String getClassName(Class<?> cls) {
            if (this.isBuildAsSql()) {
                HibernateConfiguration hc = new HibernateConfiguration();
                return hc.getTableName(cls);
            }
            return cls.getName();
        }
        /**
         * 生成指定样本对象的WHERE用途字符串
         * @param example 样本对象
         * @param operator 操作符（"="、">="、"<="等）
         * @param separate 多个属性之间的分隔符，生成where条件时指定" AND "，生成更新赋值时指定","
         * @param forWhere 指定是否用于Where条件
         * @param forAdditional 追加模式标记，仅在forWhere=false时有意义
         * @param insertOr 是否需要在最前面插入" or "前缀
         * @param building 用来进行HQL字符串合并的StringBuilder对象
         * @param params 生成过程中构造的HQL参量，其中key=参量名；value=参量值
         * @return 用来生成条件字符串的有效属性数量。
         */
        protected int buildConditions(Object example, String operator, String separate, boolean forWhere, boolean forAdditional, boolean insertOr, StringBuilder building, Map<String, Object> params) {
            boolean insertedOr = false;
            int count = 0;
            // 利用json编解码将样本对象转换成Map对象
            Map<String, Object> map;
            try {
                map = this.getNotNulls(example);
                HibernateSessionBox sessionBox = this.getSessionBox();
                Session session = sessionBox == null ? null : sessionBox.getSession(false);
                ClassMetadata cm = session == null ? null : session.getSessionFactory().getClassMetadata(example.getClass());
                if (cm != null) {
                    // 排除映射字段之外的属性参与HQL字符串的生成
                    this.checkProperties(session, cm, map);
                }
                if (forWhere && cm != null) {
                    try {
                        String pkName = cm.getIdentifierPropertyName();
                        if (pkName != null && pkName.length() > 0) {
                            Object pkValue = map.get(pkName);
                            if (pkValue != null) {
                                map.clear();
                                map.put(pkName, pkValue);
                            }
                        }
                    } finally {
                        sessionBox.freeSession(session);
                    }
                }
                boolean forLike = operator.trim().equals("LIKE");
                boolean forEquals = operator.trim().equals("=") || operator.trim().equals("<>") || operator.trim().equals("!=");
                String alias = this.alias();
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> e = it.next();
                    if (forLike && !(e.getValue() instanceof String)) {
                        // LIKE模式下忽略非字符串属性
                        continue;
                    }
                    if (forWhere && forEquals && e.getValue() instanceof Date) {
                        // 日期时间属性不作为where的等于或不等于条件
                        continue;
                    }

                    if (!insertedOr && insertOr) {
                        building.append(" OR ");
                        insertedOr = true;
                    }
                    if (count > 0) {
                        building.append(separate);
                    }
                    if (alias != null && alias.length() > 0) {
                        building.append(alias);
                        building.append('.');
                    }
                    building.append(this.getPropertyName(e.getKey()));
                    building.append(operator);
                    if (!forWhere && operator.trim().equals("=") && forAdditional) {
                        // 在编译set语法并且操作符指定的是赋值'='号，并且指定了追加模式标记...
                        building.append(this.getPropertyName(e.getKey()));
                        building.append('+');
                    }
                    String paramName = String.format("p%d_", this.paramNameId());
                    building.append(':');
                    building.append(paramName);
                    if (forLike) {
                        params.put(paramName, "%" + e.getValue().toString() + "%");
                    } else {
                        params.put(paramName, e.getValue());
                    }
                    count++;
                }
            } catch (Exception e) {
                F.log().dbe(String.format("Jsql运行时异常：%s - %s.", e.getMessage(), e.getClass().getSimpleName()), e);
            }
            return count;
        }
        /**
         * 检查并清理不能作为查询条件的属性
         * @param session
         * @param cm
         * @param map
         */
        private void checkProperties(Session session, ClassMetadata cm, Map<String, Object> map) {
            String[] names = cm.getPropertyNames();
            Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> en = it.next();
                boolean doRemove = true;
                if (en.getKey().equals(cm.getIdentifierPropertyName())) {
                    doRemove = false;  // en描述的属性是主键字段映射属性
                } else {
                    for (String name : names) {
                        if (name.equals(en.getKey())) {
                            doRemove = false; // en描述的属性是字段映射属性
                            break;
                        }
                    }
                }
                Class<? extends Object> cls = en.getValue().getClass();
                if (!doRemove) {
                    if (cls.isArray() || en.getValue() instanceof Collection || en.getValue() instanceof Map) {
                        doRemove = true; // 属性值的类型是数组、Collection或Map则忽略之
                    }
                }
                if (!doRemove) {
                    if (session.getSessionFactory().getClassMetadata(cls) != null) {
                        doRemove = true; // 属性值的类型是另外一个映射对象则忽略之
                    }
                }

                if (doRemove) {
                    it.remove();
                }
            }
        }
        protected <TARGET_CLASS> Class<?> getPropertyClass(Class<TARGET_CLASS> targetClass, String propertyName) {
            Class<?> propCls = null;
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(targetClass);
                PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
                for (int i = 0; i< propertyDescriptors.length; i++) {
                    PropertyDescriptor descriptor = propertyDescriptors[i];
                    String propName = descriptor.getName();
                    if (propName.equals(propertyName)) {
                        Method readMethod = descriptor.getReadMethod();
                        Method writeMethod = descriptor.getWriteMethod();
                        if (readMethod != null && writeMethod != null) { // 可读可写的属性才有意义
                            propCls = descriptor.getPropertyType();
                            break;
                        }
                    }
                }
            } catch (IntrospectionException e) {
                F.log().dbe(String.format("Jsql运行时异常：%s - %s.", e.getMessage(), e.getClass().getSimpleName()), e);
            }
            return propCls;
        }
        /**
         * 获取指定目标类型的指定属性的类型
         * @param targetClass 目标类型
         * @param propertiesName 指定属性的名称
         * @return 指定属性的类型，如果指定属性不属于指定的目标类型则返回null。
         */
        protected <TARGET_CLASS> Class<?> getPropertiesClass(Class<TARGET_CLASS> targetClass, String propertiesName) {
            if (propertiesName.indexOf(",") > 0 ||
                this.resultClass().equals(targetClass) ||
                new Object[0].getClass().equals(this.resultClass())) {
                String[] propNames = propertiesName.split(",");
                for (String propName : propNames) {
                    if (this.getPropertyClass(targetClass, propName.trim()) == null) {
                        return null;
                    }
                }
                return this.resultClass();
            } else {
                return this.getPropertyClass(targetClass, propertiesName);
            }
        }
        /**
         * @param object
         * @return
         * @throws IntrospectionException
         * @throws IllegalArgumentException
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         */
        private Map<String, Object> getNotNulls(Object object)
            throws IntrospectionException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException {
            // 获取对象的属性信息
            Class<? extends Object> type = object.getClass();
            Map<String, Object> res = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
            for (int i = 0; i< propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Method writeMethod = descriptor.getWriteMethod();
                    if (readMethod != null && writeMethod != null) { // 可读可写的属性才有意义
                        Object result = readMethod.invoke(object, new Object[0]);
                        if (result != null) {
                            res.put(propertyName, result);
                        }
                    }
                }
            }
            return res;
        }
    }
    /**
     * <b>结果描述类</b><br>
     * 结果对象用于执行整个语法树所构成的完整SQL语法并获得结果。
     */
    public class Result<RESULT_CLASS> extends GrammarAbstract {
        //		public String getLogicsInfo() {
//			Grammar logic = this.rootLogic();
//			if (logic == null) {
//				return "\r\n";
//			}
//			StringBuilder logicsInfo = new StringBuilder("\r\n");
//			this.getLogicsInfo(logic, logicsInfo, 0);
//			return logicsInfo.toString();
//		}
//		private void getLogicsInfo(Grammar logic, StringBuilder logicsInfo, int deep) {
//			for (int i = 0; i < deep; i++) {
//				logicsInfo.append("  ");
//			}
//			Logic<?, ?> lg = (Logic<?, ?>)logic;
//			logicsInfo.append(String.format("type:%s(condition:%s, subs:%d)\r\n", lg.logicType.name(), lg.condition == null ? "null" : lg.condition.getClass().getSimpleName(), lg.subs().size()));
//			if (lg.subs().size() > 0) {
//				for (Grammar sub : lg.subs()) {
//					if (sub instanceof Logic) {
//						this.getLogicsInfo(sub, logicsInfo, deep + 1);
//					} else {
//						for (int i = 0; i < deep; i++) {
//							logicsInfo.append("  ");
//						}
//						logicsInfo.append(sub.getClass().getSimpleName() + "\r\n");
//					}
//				}
//			}
//		}
        protected Grammar getWhereGrammar() {
            Grammar where = null;
            if (this instanceof Where) {
                where = this;
            } else {
                where = this.getParent();
                while (where != null && !(where instanceof Where)) {
                    where = where.getParent();
                }
            }
            return where;
        }
        private Result() {
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.jsql.Jsql.Grammar#build(java.lang.StringBuilder, java.util.Map)
         */
        @Override
        public Grammar build(StringBuilder building,
                             Map<String, Object> params) {
            if (Jsql.this.isEnableDebug()) {
                Grammar root = this.root();
                if (!(root instanceof Select)) {
                    F.log().dbd(String.format("Jsql逻辑错误：找不到Select语法对象%s.", Jsql.callMeAt()));
                }
            }
            return this;
        }
        /**
         * 执行select count（1）操作<br>
         * 仅适用于select。
         * @return select count（1）操作的结果值，如果失败则返回-1，详细错误原因在日志中输出。
         */
        public int count() {
            StringBuilder building = new StringBuilder();
            Map<String, Object> params = new HashMap<String, Object>();
            Grammar root = this.root();
            if (root instanceof Select) {
                Select select = (Select)root;
                boolean isForCount = select.isForCount();
                select.setForCount(true);
                select.build(building, params);
                select.setForCount(isForCount);
                HibernateSessionBox sessionBox = this.getSessionBox();
                Session session = sessionBox == null ? null : sessionBox.getSession(false);
                if (session == null) {
                    F.log().dbd(String.format("Jsql逻辑错误：没有指定有效的hibernate会话对象%s.", Jsql.callMeAt()));
                    return -1;
                }
                try {
                    @SuppressWarnings("unchecked")
                    List<Long> res = this.exec(session, building, params, null, false, null);
                    return (int)res.get(0).longValue();
                } finally {
                    sessionBox.freeSession(session);
                }
            } else {
                F.log().dbd(String.format("Jsql逻辑错误：语法不完整%s.", Jsql.callMeAt()));
            }
            return -1;
        }
        /**
         * 执行delete或update操作
         * @return 收执行影响的数量，如果失败则返回-1，详细错误原因在日志中输出。
         */
        public int executeUpdate() {
            return this.executeUpdate(null);
        }
        /**
         * 执行delete或update操作
         * @param pagingInfo 指定delete或update操作的分页和排序要求
         * @return 收执行影响的数量，如果失败则返回-1，详细错误原因在日志中输出。
         */
        @SuppressWarnings("rawtypes")
        public int executeUpdate(PagingInfo pagingInfo) {
            StringBuilder building = new StringBuilder();
            Map<String, Object> params = new HashMap<String, Object>();
            Grammar root = this.root();
            if (root instanceof UpdateAbstract) {
                if (Jsql.this.getExecuteBefore() != null && root.isEnableExecuteCallback()) {
                    Object setArgs = null;
                    if (root instanceof Update) {
                        Grammar set = root.subs() != null && root.subs().size() > 0 ? root.subs().get(0) : null;
                        if (set != null && set instanceof Set) {
                            setArgs = ((Set)set).args;
                        }
                    }
                    Jsql.this.getExecuteBefore().executeUpdateBefore(Jsql.this, this, this.getTargetClass(), setArgs);
                }
                UpdateAbstract update = (UpdateAbstract)root;
                if (!update.isUpdateAll()) {
                    Grammar logic = update.rootLogic();
                    if (logic == null) {
                        F.log().dbd(String.format("Jsql逻辑错误：意外的无条件'%s'语法被阻止执行%s.", root.getClass().getSimpleName(), Jsql.callMeAt()));
                        return -1;
                    }
                }
                update.build(building, params);
                if (pagingInfo != null) {
                    if (pagingInfo.getTotalSize() == null || pagingInfo.getTotalSize().intValue() <= 0 ||
                        pagingInfo.getTotalPageNumber() == null || pagingInfo.getTotalPageNumber().intValue() <= 0 ||
                        pagingInfo.getPageSize() == null || pagingInfo.getPageSize().intValue() <= 0 ||
                        pagingInfo.getPageNo() == null || pagingInfo.getPageNo().intValue() <= 1) {
                        pagingInfo = null;
                        F.log().dbw(String.format("Jsql逻辑错误：'%s'语法指定了无效的分页参数%s.", root.getClass().getSimpleName(), Jsql.callMeAt()));
                    } else {
                        int firstResult = (pagingInfo.getPageNo() - 1) * pagingInfo.getPageSize();
                        if (firstResult < 0 || firstResult >= pagingInfo.getTotalPageNumber().intValue()) {
                            pagingInfo = null;
                            F.log().dbw(String.format("Jsql逻辑错误：'%s'语法指定了无效的分页参数%s.", root.getClass().getSimpleName(), Jsql.callMeAt()));
                        }
                    }
                }
                HibernateSessionBox sessionBox = this.getSessionBox();
                Session session = null;
                UUID batchId = null;
                try {
                    session = sessionBox == null ? null : sessionBox.getSession(true);
                    if (session == null) {
                        F.log().dbd(String.format("Jsql逻辑错误：没有指定有效的hibernate会话对象%s.", Jsql.callMeAt()));
                        return -1;
                    }
                    if (Jsql.this.getExecuteBeanChanged() != null) {
                        batchId = UUID.randomUUID();
                        if (root instanceof Delete) {
                            Jsql.this.getExecuteBeanChanged().batchDeleting(Jsql.this, (Delete)root, (Class<?>)root.target(), batchId);
                        } else if (root instanceof Update) {
                            Jsql.this.getExecuteBeanChanged().batchUpdating(Jsql.this, (Update)root, (Class<?>)root.target(), batchId);
                        }
                    }
                    @SuppressWarnings("unchecked")
                    List<Integer> res = this.exec(session, building, params, pagingInfo, true, null);
                    int retVal = res.get(0);
                    if (retVal > 0 && pagingInfo != null) {
                        // 根据执行结果修正分页信息对象的相关数值
                        int newTotalSize = pagingInfo.getTotalSize() - retVal;
                        newTotalSize = newTotalSize < 0 ? 0 : newTotalSize;
                        pagingInfo.setTotalSize(newTotalSize);
                        pagingInfo.setTotalPageNumber((newTotalSize + pagingInfo.getPageSize() - 1) / pagingInfo.getPageSize());
                        if (pagingInfo.getPageNo() > pagingInfo.getTotalPageNumber()) {
                            pagingInfo.setPageNo(pagingInfo.getTotalPageNumber());
                        }
                    }
                    if (Jsql.this.getExecuteBeanChanged() != null) {
                        try {
                            if (root instanceof Delete) {
                                if (retVal > 0) {
                                    Jsql.this.getExecuteBeanChanged().batchDeleted(Jsql.this, (Delete)root, (Class<?>)root.target(), batchId, retVal);
                                } else {
                                    Jsql.this.getExecuteBeanChanged().batchDeleteCanceled(Jsql.this, (Delete)root, (Class<?>)root.target(), batchId);
                                }
                            } else if (root instanceof Update) {
                                if (retVal > 0) {
                                    Jsql.this.getExecuteBeanChanged().batchUpdated(Jsql.this, (Update)root, (Class<?>)root.target(), batchId, retVal);
                                } else {
                                    Jsql.this.getExecuteBeanChanged().batchUpdateCanceled(Jsql.this, (Update)root, (Class<?>)root.target(), batchId);
                                }
                            }
                        } finally {
                            // 批量更新或删除已经执行过回调了
                            batchId = null;
                        }
                    }
                    return retVal;
                } finally {
                    try {
                        if (batchId != null) {
                            // 批量更新或删除尚未执行过结论性回调，表示操作过程出异常了，这里要回调通知batchId指定的操作已经取消了
                            if (root instanceof Delete) {
                                Jsql.this.getExecuteBeanChanged().batchDeleteCanceled(Jsql.this, (Delete)root, (Class<?>)root.target(), batchId);
                            } else if (root instanceof Update) {
                                Jsql.this.getExecuteBeanChanged().batchUpdateCanceled(Jsql.this, (Update)root, (Class<?>)root.target(), batchId);
                            }
                        }
                    } finally {
                        if (sessionBox != null && session != null) {
                            sessionBox.freeSession(session);
                        }
                    }
                }
            } else {
                F.log().dbd(String.format("Jsql逻辑错误：'%s'语法不支持executeUpdate()方法%s.", root.getClass().getSimpleName(), Jsql.callMeAt()));
            }
            return -1;
        }
        /**
         * 执行select操作<br>
         * 等同于list(null)
         * @return select操作结果，如果失败则返回-1，详细错误原因在日志中输出。
         */
        public List<RESULT_CLASS> list() {
            return this.list(null);
        }

        /**
         * 执行select操作
         * @return select操作结果,获取第一个结果,如果没有,则返回null
         */
        public RESULT_CLASS single() {
            return singleOrDefault(null);
        }

        /**
         * 执行select操作
         * @param defaultValue 默认值
         * @return select操作结果,获取第一个结果,如果没有,则返回{defaultValue}
         */
        public RESULT_CLASS singleOrDefault(RESULT_CLASS defaultValue) {
            List<RESULT_CLASS> list = this.list(new PagingInfo(1));
            if (list == null || list.size() == 0) {
                return defaultValue;
            }

            return list.get(0);
        }

        /**
         * 执行select操作<br>
         * 等同于list(new PagingInfo(Integer.MAX_VALUE))
         * @return select操作结果，如果失败则返回-1，详细错误原因在日志中输出。
         */
        public List<RESULT_CLASS> listAll() {
            return list(new PagingInfo(Integer.MAX_VALUE));
        }
        /**
         * 合并指定语法树的Where条件到当前语法树<br>
         * 合并以后，当前语法树的Where条件会整体‘and’指定语法树的整体Where条件；<br>
         * 支持不同类型的语法树之间进行合并，例如：一个Delete语法可以合并一个Select或者Update语法树的Where条件；
         * @param grammar 目标语法树的任意语法节点对象
         * @return 当前语法对象
         */
        @SuppressWarnings("unchecked")
        public Result<RESULT_CLASS> mergeConditions(Grammar grammar) {
            if (grammar != null) {
                Grammar logic = grammar.rootLogic();
                if (logic != null) {
                    Grammar where = this.getWhereGrammar();
                    if (where != null) {
                        logic = logic.clone();
                        if (where.subs().size() > 0) {
                            Grammar thisRootLogic = where.subs().remove(0);
                            Logic<RESULT_CLASS, RESULT_CLASS> newLogic = new Logic<RESULT_CLASS, RESULT_CLASS>(LogicType.Start);
                            newLogic.setParent(where);
                            where.subs().add(newLogic);

                            thisRootLogic.setParent(newLogic);
                            newLogic.subs().add(thisRootLogic);

                            newLogic.and((Logic<RESULT_CLASS, RESULT_CLASS>)logic);
                        } else {
                            logic.setParent(where);
                            where.subs().add(logic);
                        }
                    }
                }
            }
            return this;
        }
        /**
         * 执行select操作<br>
         * 如果pagingInfo参数指定为null，则会按照缺省的分页大小执行，并返回第一页结果。<br>
         * 缺省分页大小由Jsq对象的defaultPageSize属性决定，原始值是256。
         * @param pagingInfo 指定select操作的分页和排序要求，并返回结果总量
         * @return select操作结果，如果失败则返回-1，详细错误原因在日志中输出。
         */
        public List<RESULT_CLASS> list(PagingInfo pagingInfo) {
            return this.list(false, pagingInfo);
        }
        /**
         * 执行select操作<br>
         * 如果pagingInfo参数指定为null，则会按照缺省的分页大小执行，并返回第一页结果。<br>
         * 缺省分页大小由Jsq对象的defaultPageSize属性决定，原始值是256。
         * @param forUpdate 指定是否在select时开启行锁
         * @return select操作结果，如果失败则返回-1，详细错误原因在日志中输出。
         */
        public List<RESULT_CLASS> list(boolean forUpdate) {
            return this.list(forUpdate, null);
        }
        /**
         * 执行select操作<br>
         * 如果pagingInfo参数指定为null，则会按照缺省的分页大小执行，并返回第一页结果。<br>
         * 缺省分页大小由Jsq对象的defaultPageSize属性决定，原始值是256。
         * @param forUpdate 指定是否在select时开启行锁
         * @param pagingInfo 指定select操作的分页和排序要求，并返回结果总量
         * @return select操作结果，如果失败则返回-1，详细错误原因在日志中输出。
         */
        @SuppressWarnings("unchecked")
        public List<RESULT_CLASS> list(boolean forUpdate, PagingInfo pagingInfo) {
            StringBuilder building = new StringBuilder();
            Map<String, Object> params = new HashMap<String, Object>();
            Grammar root = this.root();
            if (root instanceof Select) {
                if (Jsql.this.getExecuteBefore() != null && root.isEnableExecuteCallback()) {
                    Jsql.this.getExecuteBefore().listBefore(Jsql.this, this, this.getTargetClass());
                }
                HibernateSessionBox sessionBox = this.getSessionBox();
                Session session = null;
                try {
                    session = sessionBox == null ? null : sessionBox.getSession(false);
                    if (session == null) {
                        F.log().dbd(String.format("Jsql逻辑错误：没有指定有效的hibernate会话对象%s.", Jsql.callMeAt()));
                        return null;
                    }
                    // 当前语法树描述的是一段select语句
                    Select select = (Select)root;
                    if (pagingInfo == null) {
                        pagingInfo = new PagingInfo();
                        pagingInfo.setTotalPageNumber(1);
                        pagingInfo.setTotalSize(pagingInfo.getPageSize());
                    } else if (!select.isForCount() && pagingInfo.getTotalSize() == null) {
                        // 调用参数指定了pagingInfo参数但总结果数量值尚未填充，则在执行select之前先获得一次count
                        select.setForCount(true); // 使当前的select语法树在生成HQL时生成select count（1）语法
                        select.build(building, params);
                        List<Long> res = this.exec(session, building, params, null, false, null);
                        select.setForCount(false);
                        pagingInfo.setTotalSize((int)res.get(0).longValue());

                        // 重置HQL生成参数
                        building = new StringBuilder();
                        params.clear();
                    }
                    if (pagingInfo.getPageSize() == null || pagingInfo.getPageSize().intValue() < 1) {
                        // 未指定分页大小时采用缺省的分页大小
                        pagingInfo.setPageSize(select.getDefaultPageSize());
                    }
                    if (pagingInfo.getTotalSize() != null) {
                        // 根据结果总数量计算页数
                        pagingInfo.setTotalPageNumber((pagingInfo.getTotalSize() + pagingInfo.getPageSize() - 1) / pagingInfo.getPageSize());
                        if (pagingInfo.getPageNo() == null || pagingInfo.getPageNo().intValue() < 1) {
                            if (pagingInfo.getTotalPageNumber().intValue() > 0) {
                                pagingInfo.setPageNo(1);
                            } else {
                                pagingInfo.setPageNo(0);
                            }
                        }
                    }

                    select.build(building, params);
                    String aliasForUpdate = null;
                    if (forUpdate && (
                        pagingInfo.getOrderType() == null ||
                            pagingInfo.getOrderType().equals(OrderType.NOORDER) ||
                            pagingInfo.getOrderPropertyNames() == null ||
                            pagingInfo.getOrderPropertyNames().length < 1)) {
                        aliasForUpdate = select.alias();
                    }
                    @SuppressWarnings("rawtypes")
                    List results = this.exec(session, building, params, pagingInfo, false, aliasForUpdate);
                    if (results != null && results.size() > 0 && !new Object[0].getClass().equals(this.resultClass())) {
                        boolean hasNull = false;
                        Object example = null;
                        Iterator<?> it = results.iterator();
                        while (it.hasNext() && example == null) {
                            Object res = it.next();
                            if (res == null) {
                                hasNull = true;
                            } else {
                                example = res;
                            }
                        }
                        if (hasNull ||
                            !example.getClass().equals(this.getTargetClass()) &&
                                this.resultClass().equals(this.getTargetClass())) {
                            String[] propNames = select.propName.split(",");
                            Class<?> cls = this.getTargetClass();
                            for (int i = 0; i < results.size(); i++) {
                                Object res = results.get(i);
                                try {
                                    Object bean = cls.newInstance();
                                    if (res != null) {
                                        Object[] vals = res.getClass().isArray() ? (Object[])res : new Object[]{res};
                                        for (int j = 0; j < propNames.length; j++) {
                                            BeanUtil.setBeanProperticeValue(bean, propNames[j].trim(), vals[j]);
                                        }
                                    }
                                    results.set(i, bean);
                                } catch (Exception e) {
                                    F.log().dbe(String.format("Jsql运行时异常：%s - %s.", e.getMessage(), e.getClass().getSimpleName()), e);
                                }
                            }
                        }
                    }
//					if (results != null && results.size() > 0 && select.propName != null && select.propName.indexOf(",") > 0) {
//						String[] propNames = select.propName.split(",");
//						Class<?> cls = this.getTargetClass();
//						for (int i = 0; i < results.size(); i++) {
//							Object[] vals = (Object[]) results.get(i);
//							try {
//								Object bean = cls.newInstance();
//								for (int j = 0; j < propNames.length; j++) {
//									BeanUtil.setBeanProperticeValue(bean, propNames[j].trim(), vals[j]);
//								}
//								results.set(i, bean);
//							} catch (Exception e) {
//								F.log().dbe(String.format("Jsql运行时异常：%s - %s.", e.getMessage(), e.getClass().getSimpleName()), e);
//							}
//						}
//					}
                    if (Jsql.this.getExecuteAfter() != null && root.isEnableExecuteCallback()) {
                        Jsql.this.getExecuteAfter().listAfter(Jsql.this, results);
                    }
                    return results;
                } finally {
                    if (sessionBox != null && session != null) {
                        sessionBox.freeSession(session);
                    }
                }
            } else {
                F.log().dbd(String.format("Jsql逻辑错误：语法不完整%s.", Jsql.callMeAt()));
            }
            return null;
        }
        /**
         * 执行select类的HQL
         * @param session 可用的hibernate会话对象
         * @param building HQL字符串
         * @param params 执行参数
         * @param pagingInfo 分页参数
         * @param executeUpdate 指定是查询还是更新或删除
         * @param aliasForUpdate 指定在执行select操作时是否开启行锁，如果指定了有效值则用该值开启行锁
         * @return select结果
         */
        @SuppressWarnings("rawtypes")
        private List exec(Session session, StringBuilder building,
                          Map<String, Object> params, PagingInfo pagingInfo, boolean executeUpdate, String aliasForUpdate) {
            if (pagingInfo != null && (
                pagingInfo.getOrderType() != null && !pagingInfo.getOrderType().equals(PagingInfo.OrderType.NOORDER) &&
                    pagingInfo.getOrderPropertyNames() != null && pagingInfo.getOrderPropertyNames().length > 0 ||
                    pagingInfo.getOrders() != null && pagingInfo.getOrders().size() > 0)) {
                // 如果指定了排序参数则在building中追加排序语法
                building.append(" ORDER BY ");
                String alias = this.alias();
                boolean ordered = false;
                if (pagingInfo.getOrders() != null && pagingInfo.getOrders().size() > 0) {
                    Iterator<Order> it = pagingInfo.getOrders().iterator();
                    while (it.hasNext()) {
                        Order order = it.next();
                        if (order.getOrderType() != null && !order.getOrderType().equals(PagingInfo.OrderType.NOORDER) &&
                            order.getOrderPropertyNames() != null && order.getOrderPropertyNames().length > 0) {
                            String[] opns = order.getOrderPropertyNames();
                            for (int i = 0; i < opns.length; i++) {
                                if (ordered) {
                                    building.append(',');
                                }
                                if (alias != null && alias.length() > 0) {
                                    building.append(alias);
                                    building.append('.');
                                }
                                building.append(opns[i]);
                                building.append(' ');
                                building.append(order.getOrderType().name());
                                ordered = true;
                            }
                        }
                    }
                }
                if (!ordered && pagingInfo.getOrderType() != null && !pagingInfo.getOrderType().equals(PagingInfo.OrderType.NOORDER) &&
                    pagingInfo.getOrderPropertyNames() != null && pagingInfo.getOrderPropertyNames().length > 0) {
                    // 与老的排序参数兼容的代码
                    String[] opns = pagingInfo.getOrderPropertyNames();
                    for (int i = 0; i < opns.length; i++) {
                        if (i > 0) {
                            building.append(',');
                        }
                        if (alias != null && alias.length() > 0) {
                            building.append(alias);
                            building.append('.');
                        }
                        building.append(opns[i]);
                        building.append(' ');
                        building.append(pagingInfo.getOrderType().name());
                    }
                }
            }
            String hql = building.toString();
            if (!executeUpdate && aliasForUpdate != null && aliasForUpdate.length() > 0) {
                hql.replace(aliasForUpdate, "as " + aliasForUpdate);
            }
            Query query = session.createQuery(hql);
            if (params.size() > 0) {
                Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> en = it.next();
                    query.setParameter(en.getKey(), en.getValue());
                }
            }
            if (pagingInfo != null) {
                // 设置分页参数
                query.setMaxResults(pagingInfo.getPageSize());
                if (pagingInfo.getPageNo() != null && pagingInfo.getPageNo().intValue() >= 1) {
                    query.setFirstResult(pagingInfo.getPageSize().intValue() * (pagingInfo.getPageNo().intValue() - 1));
                }
            }
            if (executeUpdate) {
                List<Integer> res = new ArrayList<Integer>();
                long time = System.currentTimeMillis();
                res.add(query.executeUpdate());
                if (Jsql.this.getExecuteCompleted() != null) {
                    Jsql.this.getExecuteCompleted().executeUpdateCompleted(Jsql.this, this, hql, System.currentTimeMillis() - time);
                }
                return res;
            }
            long time = System.currentTimeMillis();
            if (aliasForUpdate != null && aliasForUpdate.length() > 0) {
                query.setLockMode(aliasForUpdate, LockMode.PESSIMISTIC_WRITE);
            }
            List results = query.list();
            if (Jsql.this.getExecuteCompleted() != null) {
                Jsql.this.getExecuteCompleted().listCompleted(Jsql.this, this, hql, System.currentTimeMillis() - time);
            }
            return results;
        }
    }
    /**
     * <b>逻辑语法定义类</b><br>
     * 用来描述SQL语句中的与、或逻辑
     */
    public class Logic<RESULT_CLASS, TARGET_CLASS> extends Result<RESULT_CLASS> {
        @Override
        protected Logic<RESULT_CLASS, TARGET_CLASS> clone() {
            Logic<RESULT_CLASS, TARGET_CLASS> clobj = new Logic<RESULT_CLASS, TARGET_CLASS>(this.logicType);
            if (this.condition != null) {
                if (this.condition instanceof Grammar) {
                    clobj.condition = ((Grammar)this.condition).clone();
                    ((Grammar)clobj.condition).setParent(clobj);
                    clobj.subs().add((Grammar)clobj.condition);
                } else {
                    clobj.condition = this.condition;
                }
            }
            this.cloneSubs(clobj);
            return clobj;
        }
        /**
         * @param logicType 逻辑类型
         */
        private Logic(LogicType logicType) {
            this.logicType = logicType;
        }
        /**
         * @param logicType 逻辑类型
         * @param condition 逻辑条件
         */
        private Logic(LogicType logicType, Condition<RESULT_CLASS, TARGET_CLASS> condition) {
            this.logicType = logicType;
            this.condition = condition;
        }
        /**
         * @param logicType 逻辑类型
         * @param examples 逻辑条件样本
         */
        private Logic(LogicType logicType, List<TARGET_CLASS> examples) {
            this.logicType = logicType;
            this.condition = examples;
        }
        /**
         * @param logicType 逻辑类型
         * @param examples 逻辑条件样本
         */
        private Logic(LogicType logicType, ExtQueryConditions<TARGET_CLASS> extQueryCondition) {
            this.logicType = logicType;
            this.condition = extQueryCondition;
        }
        /**
         * 分析当前逻辑语法包含的条件数量
         * @return 当前逻辑语法包含的条件数量，当分析过程发现的条件数量>1时则终止分析，因此最大返回值为2
         */
        @SuppressWarnings("unchecked")
        private int thisLogicConditionCount() {
            int count = 0;
            if (this.condition != null) {
                count++;
            }
            if (this.subs().size() > 0) {
                Iterator<Grammar> it = this.subs().iterator();
                while (it.hasNext()) {
                    Grammar logic = it.next();
                    if (logic instanceof Logic && ((Logic<RESULT_CLASS, TARGET_CLASS>)logic).thisLogicConditionCount() > 0) {
                        count++;
                        if (count > 1) {
                            break;
                        }
                    }
                }
            }
            return count;
        }
        @Override
        public Grammar build(StringBuilder building,
                             Map<String, Object> params) {
            int conditionCount = this.thisLogicConditionCount();

            if (this.logicType == LogicType.Start) {
                if (conditionCount > 1) {
                    building.append('(');
                }
            } else if (this.logicType == LogicType.AndLogic) {
                building.append(" AND ");
            } else if (this.logicType == LogicType.OrLogic) {
                building.append(" OR ");
            }

            if (this.condition != null) {
                if (this.condition instanceof Condition) {
                    ((Grammar)this.condition).build(building, params);
                } else if (this.condition instanceof ExtQueryConditions) {
                    @SuppressWarnings("unchecked")
                    ExtQueryConditions<TARGET_CLASS> extQueryConditions = (ExtQueryConditions<TARGET_CLASS>) this.condition;
                    this.buildByExtQueryConditions(extQueryConditions, building, params);
                } else if (this.condition instanceof List){
                    @SuppressWarnings("unchecked")
                    List<TARGET_CLASS> examples = (List<TARGET_CLASS>) this.condition;
                    this.buildByExamples(examples, building, params);
                }
            }

            if (this.subs().size() > 0) {
                Iterator<Grammar> it = this.subs().iterator();
                while (it.hasNext()) {
                    Grammar sub = it.next();
                    if (sub instanceof Logic) {
                        sub.build(building, params);
                    }
                }
            }

            if (this.logicType == LogicType.Start && conditionCount > 1) {
                building.append(')');
            }

            return this;
        }
        private void buildByExtQueryConditions(
            ExtQueryConditions<TARGET_CLASS> extQueryCondition,
            StringBuilder building, Map<String, Object> params) {
            building.append('(');
            List<TARGET_CLASS> likes = extQueryCondition.getLikeExamples();
            List<RangeExample<TARGET_CLASS>> ranges = extQueryCondition.getRangeExamples();
            List<Object> inValues = extQueryCondition.getInValues();
            List<Object> notInValues = extQueryCondition.getNotInValues();
            String pkName = null;
            if (inValues != null && inValues.size() > 0 ||
                notInValues != null && notInValues.size() > 0) {
                // 如果指定了in或者not in语法，则必须获取到目标类型的主键名称
                Object target = this.target();
                if (target != null) {
                    Class<?> targetClass = null;
                    while (targetClass == null) {
                        if (target instanceof Class) {
                            targetClass = (Class<?>)target;
                        } else if (target instanceof Grammar){
                            target = ((Grammar)target).target();
                        } else {
                            break;
                        }
                    }
                    if (targetClass != null) {
                        HibernateSessionBox sessionBox = this.getSessionBox();
                        Session session = sessionBox == null ? null : sessionBox.getSession(false);
                        if (session != null){
                            try {
                                ClassMetadata cm = session.getSessionFactory().getClassMetadata(targetClass);
                                if (cm != null) {
                                    pkName = cm.getIdentifierPropertyName();
                                } else {
                                    F.log().dbd(String.format("Jsql逻辑错误：无法通过hibernate获取'%s'的主键名称%s.", ((Class<?>)target).getName(), Jsql.callMeAt()));
                                }
                            } finally {
                                sessionBox.freeSession(session);
                            }
                        } else {
                            F.log().dbd(String.format("Jsql逻辑错误：没有指定有效的hibernate会话%s.", Jsql.callMeAt()));
                        }
                    } else {
                        F.log().dbd(String.format("Jsql逻辑错误：没有指定有效的语法目标类型%s.", Jsql.callMeAt()));
                    }
                } else {
                    F.log().dbd(String.format("Jsql逻辑错误：没有指定有效的语法目标%s.", Jsql.callMeAt()));
                }
            }
            if (pkName == null) {
                // 没有正确的获取目标类型的主键名称则in和not in语法参数都没有用了
                inValues = null;
                notInValues = null;
            }
            if (likes != null && likes.size() > 0) {
                if (likes.size() > 1 &&
                    (ranges != null && ranges.size() > 0 ||
                        pkName != null)) {
                    // like样本不止一个，而且还指定了条件样本或范围样本，则like样本部分要用括弧包括起来
                    building.append('(');
                }
                boolean insertOr = false;
                for (TARGET_CLASS example : likes) {
                    if (this.buildConditions(example, " LIKE ", " AND ", true, false, insertOr, building, params) > 0) {
                        insertOr = true;
                    }
                }
                if (likes.size() > 1 &&
                    (ranges != null && ranges.size() > 0 ||
                        pkName != null)) {
                    // like样本不止一个，而且还指定了条件样本或范围样本，则like样本部分要用括弧包括起来
                    building.append(')');
                }
                if (ranges != null && ranges.size() > 0 ||
                    pkName != null) {
                    // 后续还指定了其他扩展条件
                    building.append(" AND ");
                }
            }
            if (ranges != null && ranges.size() > 0) {
                if (ranges.size() > 1 &&
                    (likes != null && likes.size() > 0 ||
                        pkName != null)) {
                    // 范围样本不止一个，而且还指定了条件样本或like样本，则范围样本部分需要用括弧包括起来
                    building.append('(');
                }
                boolean insertOr = false;
                for (RangeExample<TARGET_CLASS> example : ranges) {
                    if (example.getLowerLimitExample() != null) {
                        if (this.buildConditions(example.getLowerLimitExample(), ">=", " AND ", true, false, insertOr, building, params) > 0) {
                            insertOr = true;
                        }
                    }
                    if (example.getUpperLimitExample() != null) {
                        if (example.getLowerLimitExample() != null) {
                            building.append(" AND ");
                        }
                        if (this.buildConditions(example.getUpperLimitExample(), "<=", " AND ", true, false, example.getLowerLimitExample() == null && insertOr, building, params) > 0) {
                            insertOr = true;
                        }
                    }
                }
                if (ranges.size() > 1 &&
                    (likes != null && likes.size() > 0 ||
                        pkName != null)) {
                    // 范围样本不止一个，而且还指定了条件样本或like样本，则范围样本部分需要用括弧包括起来
                    building.append(')');
                }
                if (pkName != null) {
                    // 后续还指定了其他扩展条件
                    building.append(" AND ");
                }
            }
            if (pkName != null && inValues != null && inValues.size() > 0) {
                String alias = this.alias();
                if (alias != null && alias.length() > 0) {
                    building.append(alias);
                    building.append('.');
                }
                building.append(this.getPropertyName(pkName));
                building.append(" IN (");
                for (int i = 0; i < inValues.size(); i++) {
                    if (i > 0) {
                        building.append(',');
                    }
                    String paramName = String.format("p%d_", this.paramNameId());
                    building.append(":" + paramName);
                    params.put(paramName, inValues.get(i));
                }
                building.append(')');
                if (notInValues != null && notInValues.size() > 0) {
                    // 后续还指定了其他扩展条件
                    building.append(" AND ");
                }
            }
            if (pkName != null && notInValues != null && notInValues.size() > 0) {
                String alias = this.alias();
                if (alias != null && alias.length() > 0) {
                    building.append(alias);
                    building.append('.');
                }
                building.append(this.getPropertyName(pkName));
                building.append(" NOT IN (");
                for (int i = 0; i < notInValues.size(); i++) {
                    if (i > 0) {
                        building.append(',');
                    }
                    String paramName = String.format("p%d_", this.paramNameId());
                    building.append(":" + paramName);
                    params.put(paramName, notInValues.get(i));
                }
                building.append(')');
            }
            building.append(')');
        }
        /**
         * 根据样本对象生成HQL的条件部分
         * @param examples 条件样本对象
         * @param building 用来进行HQL字符串合并的StringBuilder对象
         * @param params 生成过程中构造的HQL参量，其中key=参量名；value=参量值
         */
        private void buildByExamples(List<TARGET_CLASS> examples, StringBuilder building, Map<String, Object> params) {
            boolean insertOr = false;
            if (examples.size() > 1) {
                building.append('(');
            }
            for (TARGET_CLASS example : examples) {
                if (this.buildConditions(example, "=", " AND ", true, false, insertOr, building, params) > 0) {
                    insertOr = true;
                }
            }
            if (examples.size() > 1) {
                building.append(')');
            }
        }
        /**
         * 当前逻辑语法对象的逻辑类型
         */
        private LogicType logicType;
        /**
         * 获取当前逻辑语法所在逻辑集的起始逻辑语法对象
         * @return 当前逻辑语法所在逻辑集的起始逻辑语法对象
         */
        private Grammar getStartLogic() {
            if (this.logicType == LogicType.Start) {
                return this;
            }
            return this.getParent();
        }
//		private Grammar getRootLogic() {
//			Grammar root = this.root();
//			Grammar logic = root;
//			while (logic != null && !(logic instanceof Logic)) {
//				logic = logic.subs() != null && logic.subs().size() > 0 ? logic.subs().get(0) : null;
//			}
//			return logic;
//		}
        /**
         * 当前逻辑语法对象的逻辑条件
         */
        private Object condition;
        private Logic<RESULT_CLASS, TARGET_CLASS> setCondition(Object condition) {
            this.condition = condition;
            return this;
        }
        private Condition<RESULT_CLASS, TARGET_CLASS> logic(String propName, LogicType logicType) {
            Class<?> targetClass = this.getTargetClass();
            if (targetClass != null) {
                Class<?> pc = this.getPropertyClass(targetClass, propName);
                if (pc == null) {
                    F.log().dbd(String.format("Jsql逻辑错误：'%s'不是'%s'的属性%s.", propName, targetClass.getSimpleName(), Jsql.callMeAt()));
                    return null;
                }
            }
            Logic<RESULT_CLASS, TARGET_CLASS> logic = new Logic<RESULT_CLASS, TARGET_CLASS>(logicType);
            Condition<RESULT_CLASS, TARGET_CLASS> cond = new Condition<RESULT_CLASS, TARGET_CLASS>(logic, propName);
            logic.setCondition(cond);
            Grammar startLogic = this.getStartLogic();
            logic.setParent(startLogic);
            startLogic.subs().add(logic);
            return cond;
        }
        public Condition<RESULT_CLASS, TARGET_CLASS> and(String propName) {
            return this.logic(propName, LogicType.AndLogic);
        }
        public Condition<RESULT_CLASS, TARGET_CLASS> or(String propName) {
            return this.logic(propName, LogicType.OrLogic);
        }
        private Logic<RESULT_CLASS, TARGET_CLASS> logic(Logic<RESULT_CLASS, TARGET_CLASS> logic, LogicType logicType) {
            Grammar newLogic = logic.rootLogic();
            if (newLogic != null) {
                Logic<RESULT_CLASS, TARGET_CLASS> addLogic = new Logic<RESULT_CLASS, TARGET_CLASS>(logicType);
                Grammar startLogic = this.getStartLogic();
                addLogic.setParent(startLogic);
                startLogic.subs().add(addLogic);

                newLogic.setParent(addLogic);
                addLogic.subs().add(newLogic);
            }
            return this;
        }
        /**
         * or语法<br>
         * 对另外一个逻辑树进行‘或’操作
         * @param logic 另外一个逻辑对象
         * @return 返回一个可以执行后续更多逻辑操作的逻辑对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> or(Logic<RESULT_CLASS, TARGET_CLASS> logic) {
            return this.logic(logic, LogicType.OrLogic);
        }
        /**
         * and语法<br>
         * 对另外一个逻辑树进行‘与’操作
         * @param logic 另外一个逻辑对象
         * @return 返回一个可以执行后续更多逻辑操作的逻辑对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> and(Logic<RESULT_CLASS, TARGET_CLASS> logic) {
            return this.logic(logic, LogicType.AndLogic);
        }
        /**
         * and语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param examples 样本条件对象
         * @return 当前逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> and(List<TARGET_CLASS> examples) {
            if (examples != null && examples.size() > 0) {
                Logic<RESULT_CLASS, TARGET_CLASS> logic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start, examples);
                return this.logic(logic, LogicType.AndLogic);
            }
            return this;
        }
        /**
         * and语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合。
         * @param example 样本条件对象
         * @return 当前逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> and(TARGET_CLASS example) {
            if (example == null) {
                return this;
            }
            List<TARGET_CLASS> examples = new ArrayList<TARGET_CLASS>();
            examples.add(example);
            return this.and(examples);
        }
        /**
         * or语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param examples 样本条件对象
         * @return 当前逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> or(List<TARGET_CLASS> examples) {
            if (examples != null && examples.size() > 0) {
                Logic<RESULT_CLASS, TARGET_CLASS> logic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start, examples);
                return this.logic(logic, LogicType.OrLogic);
            }
            return this;
        }
        /**
         * or语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合。
         * @param example 样本条件对象
         * @return 当前逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> or(TARGET_CLASS example) {
            if (example == null) {
                return this;
            }
            List<TARGET_CLASS> examples = new ArrayList<TARGET_CLASS>();
            examples.add(example);
            return this.or(examples);
        }
    }
    /**
     * <b>条件语法定义类</b>
     */
    public class Condition<RESULT_CLASS, TARGET_CLASS> extends GrammarAbstract {
        @Override
        protected Condition<RESULT_CLASS, TARGET_CLASS> clone() {
            Condition<RESULT_CLASS, TARGET_CLASS> clobj = new Condition<RESULT_CLASS, TARGET_CLASS>();
            clobj.propName = this.propName;
            clobj.operator = this.operator;
            if (this.value != null) {
                if (this.value instanceof Grammar) {
                    clobj.value = ((Grammar) this.value).clone();
                    ((Grammar) clobj.value).setParent(clobj);
                    clobj.subs().add((Grammar) clobj.value);
                } else {
                    clobj.value = this.value;
                }
            }
            return clobj;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected Grammar setParent(Grammar parent) {
            super.setParent(parent);
            this.logic = (Logic<RESULT_CLASS, TARGET_CLASS>) parent;
            return this;
        }
        /**
         * 当前条件语法对象所属的逻辑语法对象
         */
        private Logic<RESULT_CLASS, TARGET_CLASS> logic;
        /**
         * 当前的条件属性名称
         */
        private String propName;
        /**
         * 条件判定的操作符（=、>、<等）
         */
        private String operator;
        /**
         * 条件判定值
         */
        private Object value;
        private Condition() {
        }
        /**
         * @param logic 逻辑类型
         * @param propName 条件属性名称
         */
        private Condition(Logic<RESULT_CLASS, TARGET_CLASS> logic, String propName) {
            this.logic = logic;
            this.propName = propName;
            logic.subs().add(this);
            this.setParent(logic);
        }
        public boolean isPrimitive(Class<?> cls) {
            try {
                return ((Class<?>) cls.getField("TYPE").get(null)).isPrimitive();
            } catch (Exception e) {
                return String.class.equals(cls);
            }
        }		@Override
        public Grammar build(StringBuilder building,
                             Map<String, Object> params) {
            String alias = this.alias();
            if (alias != null && alias.length() > 0) {
                building.append(alias);
                building.append('.');
            }
            building.append(this.getPropertyName(this.propName));
            if (this.value == null) {
                if (this.operator == null || "IS".equals(this.operator.toString().trim().toUpperCase())) {
                    building.append(" IS NULL");
                } else {
                    building.append(" IS NOT NULL");
                }
            } else if (this.value instanceof Grammar) {
                building.append(' ');
                building.append(this.operator);
                building.append(' ');
                building.append('(');
                ((Grammar)this.value).build(building, params);
                building.append(')');
                if (this.operator.trim().toUpperCase().equals("IN")) {
                    Class<?> rc = ((Grammar)this.value).resultClass();
                    if (rc == null || !this.isPrimitive(rc)) {
                        F.log().dbd(String.format("Jsql逻辑错误：IN关键字被应用于非原生类型'%s'的查询结果集%s.", rc == null ? "*" : rc.getSimpleName(), Jsql.callMeAt()));
                    }
                }
            } else {
                boolean forLike = this.operator.toString().trim().toUpperCase().equals("LIKE");
                String paramName = String.format("p%d_", this.paramNameId());
                building.append(' ');
                building.append(this.operator);
                building.append(' ');
                building.append(':');
                building.append(paramName);
                if (forLike) {
                    if (!(this.value instanceof String)) {
                        F.log().dbd(String.format("Jsql逻辑错误：LIKE关键字被应用于非字符串属性'%s'%s.", this.propName, Jsql.callMeAt()));
                        params.put(paramName, "%" + this.value + "%");
                    } else {
                        if (((String)this.value).indexOf('%') >= 0) {
                            params.put(paramName, this.value);
                        } else {
                            params.put(paramName, "%" + this.value + "%");
                        }
                    }
                } else {
                    params.put(paramName, this.value);
                }
            }
            return this;
        }
        /**
         * 将指定的结果对象设置为当前条件对象的判定值
         * @param res 结果对象
         */
        private void setResultToValue(Result<?> res) {
            Grammar root = res.root();
            this.value = root;
            root.setParent(this);
            this.subs().add(root);
        }
        /**
         * in语法
         * @param res in语法的目标，是一个select树的结果对象
         * @return 当前条件对象所属的逻辑语法对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> in(Result<?> res) {
            this.operator = "IN";
            this.setResultToValue(res);
            return this.logic;
        }
        /**
         * not in语法
         * @param res not in语法的目标，是一个select树的结果对象
         * @return 当前条件对象所属的逻辑语法对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> inNot(Result<?> res) {
            this.operator = "NOT IN";
            this.setResultToValue(res);
            return this.logic;
        }
        /**
         * 条件判断语法
         * @param operator 断言符号，如：=、>、<、LIKE等
         * @param value 断言值
         * @return 当前条件对象所属的逻辑语法对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> is(String operator, Object value) {
            Class<?> targetClass = this.getTargetClass();
            if (targetClass != null) {
                Class<?> pc = this.getPropertyClass(targetClass, this.propName);
                if (pc == null) {
                    F.log().dbd(String.format("Jsql逻辑错误：'%s'不是'%s'的属性%s.", this.propName, targetClass.getSimpleName(), Jsql.callMeAt()));
                    return null;
                } else if (value != null && !pc.equals(value.getClass())) {
                    F.log().dbd(String.format("Jsql逻辑错误：'%s.%s'的'%s'类型与条件值(%s)的'%s'类型不一致%s.", targetClass.getSimpleName(), this.propName, pc.getSimpleName(), value.toString(), value.getClass().getSimpleName(), Jsql.callMeAt()));
                    return null;
                }
            }
            this.operator = operator;
            this.value = value;
            return this.logic;
        }
        /**
         * is null语法
         * @return 当前条件对象所属的逻辑语法对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> isNull() {
            return this.is(null, null);
        }
        /**
         * is not null语法
         * @return 当前条件对象所属的逻辑语法对象
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> isNotNull() {
            return this.is("IS NOT", null);
        }
    }
    /**
     * <b>where语法定义类</b>
     */
    public class Where<RESULT_CLASS, TARGET_CLASS> extends Result<RESULT_CLASS> {
        @Override
        protected Where<RESULT_CLASS, TARGET_CLASS> clone() {
            Where<RESULT_CLASS, TARGET_CLASS> clobj = new Where<RESULT_CLASS, TARGET_CLASS>();
            clobj.needClear = this.needClear;
            this.cloneSubs(clobj);
            return clobj;
        }
        @Override
        protected Grammar rootLogic() {
            if (this.subs().size() > 0) {
                return this.subs().get(0);
            }
            return null;
        }
        private boolean needClear = false;
        private Where() {
            this(null);
        }
        private Where(Grammar firstLogic) {
            if (firstLogic != null) {
                // 这里创建一个起始逻辑对象作为当前Where对象的根逻辑
                Logic<RESULT_CLASS, TARGET_CLASS> startLogic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start);
                // 将嵌套select语法树的根逻辑对象作为当前根逻辑对象的子逻辑
                startLogic.subs().add(firstLogic);
                firstLogic.setParent(startLogic);

                // 新建的起始逻辑挂接为当前Where语法对象的根逻辑对象
                this.subs().add(startLogic);
                startLogic.setParent(this);
            }
        }
        @SuppressWarnings("unchecked")
        @Override
        public Grammar build(StringBuilder building,
                             Map<String, Object> params) {
            Grammar rl = this.rootLogic();
            int conditionCount = 0;
            if (rl != null) {
                conditionCount = ((Logic<RESULT_CLASS, TARGET_CLASS>)rl).thisLogicConditionCount();
            }
            @SuppressWarnings("rawtypes")
            RootGrammarAbstract rootGrammar = this.checkLogicalization();
            if (conditionCount < 1 && rootGrammar == null) {
                return this;
            }
            building.append(" WHERE ");
            if (Jsql.this.isEnableDebug() && this.subs().size() > 1) {
                F.log().dbd(String.format("Jsql逻辑错误：Where语法指定了多个逻辑条件集%s.", Jsql.callMeAt()));
            }
            Iterator<Grammar> it = this.subs().iterator();
            while (it.hasNext()) {
                it.next().build(building, params);
            }
            if (rootGrammar != null) {
                if (conditionCount > 0) {
                    building.append(" AND ");
                }
                String alias = this.alias();
                if (alias != null && alias.length() > 0) {
                    building.append(alias);
                    building.append('.');
                }
                building.append(this.getPropertyName(rootGrammar.getLogicalizationConditionPropertyName()));
                if (rootGrammar.getLogicalizationConditionValue() == null) {
                    if ("IS NOT".equals(rootGrammar.getLogicalizationConditionOperator().trim().toUpperCase())) {
                        building.append(" IS NOT NULL ");
                    } else {
                        building.append(" IS NULL ");
                    }
                } else {
                    building.append(' ');
                    building.append(rootGrammar.getLogicalizationConditionOperator());
                    building.append(' ');
                    String paramName = String.format("p%d_", this.paramNameId());
                    building.append(":" + paramName);
                    building.append(' ');
                    params.put(paramName, rootGrammar.getLogicalizationConditionValue());
                }
            }
            return this;
        }
        /**
         * 检查当前语法树的逻辑化状态
         * @return 如果当前语法树支持逻辑化操作则返回根语法对象，否则返回null。
         */
        @SuppressWarnings("rawtypes")
        private RootGrammarAbstract checkLogicalization() {
            Grammar root = this.root();
            if (root instanceof RootGrammarAbstract &&
                ((RootGrammarAbstract)root).getLogicalizationConditionPropertyName() != null &&
                ((RootGrammarAbstract)root).isEnableLogicalization()) {
                Class<?> tarCls = ((RootGrammarAbstract)root).getTargetClass();
                if (tarCls != null) {
                    Class<?> propCls = ((RootGrammarAbstract)root).getPropertyClass(tarCls, ((RootGrammarAbstract)root).getLogicalizationConditionPropertyName());
                    if (propCls == null) {
                        //F.log().dbd(String.format("Jsql逻辑错误：设置的逻辑化查询属性'%s'不是'%s'的属性.", ((RootGrammarAbstract)root).getLogicalizationConditionPropertyName(), tarCls.getSimpleName()));
                        return null;
                    } else if (((RootGrammarAbstract)root).getLogicalizationConditionValue() != null) {
                        Class<? extends Object> vc = ((RootGrammarAbstract)root).getLogicalizationConditionValue().getClass();
                        if (!vc.equals(propCls)) {
                            F.log().dbd(String.format("Jsql逻辑错误：逻辑化查询参数指定的'%s'属性值类型'%s'与'%s.%s'的'%s'属性类型不一致%s.",
                                ((RootGrammarAbstract)root).getLogicalizationConditionPropertyName(),
                                vc.getSimpleName(),
                                tarCls.getSimpleName(),
                                ((RootGrammarAbstract)root).getLogicalizationConditionPropertyName(),
                                propCls.getSimpleName(),
                                Jsql.callMeAt()));
                        }
                    }
                }
                return (RootGrammarAbstract)root;
            }
            return null;
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合。
         * @param example 样本条件对象
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(TARGET_CLASS example) {
            return this.where(example, null);
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合。
         * @param example 样本条件对象
         * @param extQueryCondition 扩展查询条件
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(TARGET_CLASS example, ExtQueryConditions<TARGET_CLASS> extQueryCondition) {
            List<TARGET_CLASS> examples = new ArrayList<TARGET_CLASS>();
            examples.add(example);
            return this.where(examples, extQueryCondition);
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param examples 样本条件对象
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(TARGET_CLASS[] examples) {
            return this.where(examples, null);
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param examples 样本条件对象
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(List<TARGET_CLASS> examples) {
            return this.where(examples, null);
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param extQueryCondition 扩展查询条件
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(ExtQueryConditions<TARGET_CLASS> extQueryCondition) {
            return this.where((List<TARGET_CLASS>)null, extQueryCondition);
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param examples 样本条件对象
         * @param extQueryCondition 扩展查询条件
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(TARGET_CLASS[] examples, ExtQueryConditions<TARGET_CLASS> extQueryCondition) {
            List<TARGET_CLASS> exps = new ArrayList<TARGET_CLASS>();
            for (TARGET_CLASS example : examples) {
                exps.add(example);
            }
            return this.where(exps, extQueryCondition);
        }
        /**
         * where语法<br>
         * 这里采用样本对象作为条件，每一个样本对象的非空属性作为一个条件，多个非空属性构成的条件之间按'与'逻辑进行组合，多个样本对象构成的条件之间按'或'逻辑进行组合。
         * @param examples 样本条件对象
         * @param extQueryCondition 扩展查询条件
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        public Logic<RESULT_CLASS, TARGET_CLASS> where(List<TARGET_CLASS> examples, ExtQueryConditions<TARGET_CLASS> extQueryCondition) {
            Logic<RESULT_CLASS, TARGET_CLASS> logic = null;
            if (examples != null && examples.size() > 0) {
                logic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start, examples);
            }
            if (extQueryCondition != null) {
                if (logic == null) {
                    logic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start, extQueryCondition);
                } else {
                    Logic<RESULT_CLASS, TARGET_CLASS> andLogic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.AndLogic, extQueryCondition);
                    logic.subs().add(andLogic);
                    andLogic.setParent(logic);
                }
            }

            if (this.needClear) {
                this.clear();
            }
            this.needClear = true;
            if (this.subs().size() < 1) {
                this.subs().add(logic);
                logic.setParent(this);
            } else {
                // 当前Where对象在构造时被指定了首个Logic对象表示当前语法树是一个嵌套select语法树
                @SuppressWarnings("unchecked")
                Logic<RESULT_CLASS, TARGET_CLASS> startLogic = (Logic<RESULT_CLASS, TARGET_CLASS>)this.subs().get(0);
                // 用当前根逻辑对象将新建的逻辑对象用and连起来
                startLogic.and(logic);
            }
            return logic;
        }
        /**
         * where语法
         * @param propName 第一个条件的属性名称
         * @return 用来指定propName所指属性后续判断条件的条件语法对象。
         */
        public Condition<RESULT_CLASS, TARGET_CLASS> where(String propName) {
            Class<?> targetClass = this.getTargetClass();
            if (targetClass != null) {
                Class<?> pc = this.getPropertyClass(targetClass, propName);
                if (pc == null) {
                    F.log().dbd(String.format("Jsql逻辑错误：'%s'不是'%s'的属性%s.", propName, targetClass.getSimpleName(), Jsql.callMeAt()));
                    return null;
                }
            }
            Logic<RESULT_CLASS, TARGET_CLASS> logic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start);
            Condition<RESULT_CLASS, TARGET_CLASS> cond = new Condition<RESULT_CLASS, TARGET_CLASS>(logic, propName);
            logic.setCondition(cond);

            if (this.needClear) {
                this.clear();
            }
            this.needClear = true;
            if (this.subs().size() < 1) {
                this.subs().add(logic);
                logic.setParent(this);
            } else {
                // 当前Where对象在构造时被指定了首个Logic对象表示当前语法树是一个嵌套select语法树
                @SuppressWarnings("unchecked")
                Logic<RESULT_CLASS, TARGET_CLASS> startLogic = (Logic<RESULT_CLASS, TARGET_CLASS>)this.subs().get(0);
                // 用当前根逻辑对象将新建的逻辑对象用and连起来
                startLogic.and(logic);
            }
            return cond;
        }
        /**
         * where语法<br>
         * 直接引用另外一个Where语法对象
         * @param otherWhere 另外的Where语法对象
         * @return 可以进行其他逻辑操作逻辑语法对象。
         */
        @SuppressWarnings("unchecked")
        public Logic<RESULT_CLASS, TARGET_CLASS> where(Where<RESULT_CLASS, TARGET_CLASS> otherWhere) {
            Grammar parent = this.getParent();
            if (parent != null) {
                parent.subs().remove(this);
                parent.subs().add(otherWhere);
                otherWhere.setParent(parent);
                return (Logic<RESULT_CLASS, TARGET_CLASS>) otherWhere.subs().get(0);
            }
            F.log().dbd(String.format("Jsql逻辑错误：Where语法对象缺少前置关联语法对象%s.", Jsql.callMeAt()));
            return null;
        }
    }
    /**
     * <b>from语法的抽象基类</b>
     */
    private abstract class FromAbstract extends GrammarAbstract {
        private FromAbstract() {
        }
        @Override
        protected abstract Class<?> resultClass();
        @Override
        protected abstract Object target();
        @Override
        public Grammar build(StringBuilder building,
                             Map<String, Object> params) {
            Object target = this.target();
            // HQL的目标可以是一个类型或者是另外一个完整语法的结果对象
            if (target == null || !(target instanceof Class || target instanceof Select)) {
                F.log().dbd(String.format("Jsql逻辑错误：没有为From语法指定有效的目标%s.", Jsql.callMeAt()));
                return null;
            }

            Grammar retVal = this;
            building.append(" FROM ");
            if (target instanceof Class) {
                // 目标是一个类型
                building.append(this.getClassName((Class<?>)target));
            } else if (target instanceof GrammarAbstract) {
                // 目标是另外一个完整语法的结果对象则用这个结果对象树里面的目标类型作为当前类型
                // 这样处理器是为后续Where语法将嵌套select语法通过where条件合并转换为单一select语法做准备
                building.append(this.getClassName(((GrammarAbstract)target).getTargetClass()));
            }

            if (retVal != null) {
                if (this.getParent() != null) {
                    // 为目标指定别名
                    String alias = this.alias();
                    if (alias != null && alias.length() > 0) {
                        building.append(' ');
                        building.append(alias);
                    }
                } else if (Jsql.this.isEnableDebug()){
                    F.log().dbd(String.format("Jsql逻辑错误：From语法对象缺少前置的Select、Update或Delete语法对象%s.", Jsql.callMeAt()));
                }
                if (Jsql.this.isEnableDebug() && this.subs().size() != 1) {
                    F.log().dbd(String.format("Jsql逻辑错误：From语法包含%d个Where语法对象%s.", this.subs().size(), Jsql.callMeAt()));
                }
                Grammar where = this.subs().size() > 0 ? this.subs().get(0) : null;
                if (where == null || !(where instanceof Where)) {
                    if (Jsql.this.isEnableDebug()) {
                        F.log().dbd(String.format("Jsql逻辑错误：缺少Where语法对象%s.", Jsql.callMeAt()));
                    }
                    return null;
                }
                retVal = where.build(building, params) == null ? null : this;
            }
            return retVal;
        }
        @Override
        protected Grammar rootLogic() {
            if (this.subs().size() > 0) {
                Grammar where = this.subs().get(0);
                return ((Where<?, ?>)where).rootLogic();
            }
            return null;
        }
        protected Grammar getFirstLogic(Grammar result) {
            Grammar root = result.root();
            Grammar firstLogic = root;
            while (firstLogic != null && !(firstLogic instanceof Logic)) {
                firstLogic = firstLogic.subs() != null && firstLogic.subs().size() > 0 ? firstLogic.subs().get(0) : null;
            }
            if (firstLogic != null && firstLogic.getParent() != null) {
                firstLogic.getParent().subs().clear();
            }
            return firstLogic;
        }
    }
    /**
     * <b>目标类型和结果类型都未明确情况下适用的from语法定义类</b>
     */
    public class From extends FromAbstract {
        @Override
        protected From clone() {
            From clobj = new From();
            clobj.resultClass = this.resultClass;
            clobj.target = this.target;
            this.cloneSubs(clobj);
            return clobj;
        }
        private From() {
        }
        private Class<?> resultClass;
        @Override
        protected Class<?> resultClass() {
            return this.resultClass;
        }
        private Object target;
        @Override
        protected Object target() {
            return this.target;
        }
        /**
         * from语法
         * @param targetClass from语法的目标类型
         * @return 用于后续语法拼接的where语法对象
         */
        public <TARGET_CLASS> Where<TARGET_CLASS, TARGET_CLASS> from(Class<TARGET_CLASS> targetClass) {
            this.target = targetClass;
            this.resultClass = targetClass;
            this.clear();

            Where<TARGET_CLASS, TARGET_CLASS> where = new Where<TARGET_CLASS, TARGET_CLASS>();
            where.setParent(this);
            this.subs().add(where);
            return where;
        }
        /**
         * from语法<br>
         * 注意：参数result对象所在的语法树结构会被破坏，其Where语法的条件对象会被摘除
         * @param result from语法的目标结果，result指定另外一个select语法树的结果对象作为当前from语法的目标
         * @return 用于后续语法拼接的where语法对象
         */
        public <RESULT_CLASS> Where<RESULT_CLASS, RESULT_CLASS> from(Result<RESULT_CLASS> result) {
            this.target = result.root().target();
            this.resultClass = result.resultClass();
            this.clear();

            Grammar logic = result.rootLogic();
            if (logic != null) {
                logic = logic.clone();
            }
            Where<RESULT_CLASS, RESULT_CLASS> where = new Where<RESULT_CLASS, RESULT_CLASS>(logic);
            where.setParent(this);
            this.subs().add(where);
            return where;
        }
    }
    /**
     * <b>目标类型未知但结果类型已经明确的情况下适用的from语法定义类</b>
     */
    public class FromT<RESULT_CLASS>  extends FromAbstract {
        @Override
        protected FromT<RESULT_CLASS> clone() {
            @SuppressWarnings("unchecked")
            FromT<RESULT_CLASS> clobj = new FromT<RESULT_CLASS>((Class<RESULT_CLASS>) this.resultClass, this.propName);
            clobj.target = this.target;
            this.cloneSubs(clobj);
            return clobj;
        }
        /**
         * 结果属性名称
         */
        private String propName;
        /**
         * @param resultClass 结果属性类型
         * @param propName 结果属性名称
         */
        private FromT(Class<RESULT_CLASS> resultClass, String propName) {
            this.propName = propName;
            this.resultClass = resultClass;
        }
        private Class<?> resultClass;
        @Override
        protected Class<?> resultClass() {
            return this.resultClass;
        }
        private Object target;
        @Override
        protected Object target() {
            return this.target;
        }
        /**
         * from语法
         * @param targetClass from语法的目标类型
         * @return 用于后续语法拼接的where语法对象
         */
        public <TARGET_CLASS> Where<RESULT_CLASS, TARGET_CLASS> from(Class<TARGET_CLASS> targetClass) {
            Class<?> propCls = this.getPropertiesClass(targetClass, this.propName);
            if (propCls == null) {
                F.log().dbd(String.format("Jsql逻辑错误：Select语法指定的'%s'不是'%s'的有效属性%s.", this.propName, targetClass.getSimpleName(), Jsql.callMeAt()));
                return null;
            } else if (!propCls.equals(this.resultClass())) {
                F.log().dbd(String.format("Jsql逻辑错误：Select语法指定的'%s'属性其类型(%s)与'%s.%s'的属性类型(%s)不一致%s.", this.propName, this.resultClass.getName(), targetClass.getSimpleName(), this.propName, propCls.getName(), Jsql.callMeAt()));
                return null;
            }

            this.target = targetClass;
            this.clear();

            Where<RESULT_CLASS, TARGET_CLASS> where = new Where<RESULT_CLASS, TARGET_CLASS>();
            where.setParent(this);
            this.subs().add(where);
            return where;
        }
        /**
         * from语法
         * 注意：参数result对象所在的语法树结构会被破坏，其Where语法的条件对象会被摘除
         * @param result from语法的目标结果，result指定另外一个select语法树的结果对象作为当前from语法的目标
         * @return 用于后续语法拼接的where语法对象
         */
        public <TARGET_CLASS> Where<RESULT_CLASS, TARGET_CLASS> from(Result<TARGET_CLASS> result) {
            Class<?> tagCls = result.resultClass();
            if (tagCls == null) {
                F.log().dbd(String.format("Jsql逻辑错误：为From语法指定了未知类型的结果集作为操作目标%s.", Jsql.callMeAt()));
                return null;
            }
            Class<?> propCls = this.getPropertiesClass(tagCls, this.propName);
            if (propCls == null) {
                F.log().dbd(String.format("Jsql逻辑错误：Select语法指定的'%s'不是'%s'的有效属性%s.", this.propName, tagCls.getSimpleName(), Jsql.callMeAt()));
                return null;
            } else if (!propCls.equals(this.resultClass())) {
                F.log().dbd(String.format("Jsql逻辑错误：Select语法指定的'%s'属性其类型与'%s.%s'的属性类型不一致%s.", this.propName, tagCls.getSimpleName(), this.propName, Jsql.callMeAt()));
                return null;
            }

            this.target = result.target();
            this.clear();

            Grammar logic = result.rootLogic();
            if (logic != null) {
                logic = logic.clone();
            }
            Where<RESULT_CLASS, TARGET_CLASS> where = new Where<RESULT_CLASS, TARGET_CLASS>(logic);
            where.setParent(this);
            this.subs().add(where);
            return where;
        }
    }
    /**
     * 根语法(select、update、delete语法)定义类的抽象基类
     */
    private abstract class RootGrammarAbstract<T> extends GrammarAbstract {
        /**
         * 逻辑化操作开关
         */
        private boolean enableLogicalization = true;
        /**
         * Jsql执行回调机制开关
         */
        private boolean enableExecuteCallback = true;
        /**
         * Jsql执行回调机制开关
         */
        protected boolean isEnableExecuteCallback() {
            return enableExecuteCallback;
        }
        /**
         * Jsql执行回调机制开关
         */
        protected void setEnableExecuteCallback(boolean enableExecuteCallback) {
            this.enableExecuteCallback = enableExecuteCallback;
        }
        /**
         * 逻辑化操作开关
         */
        public boolean isEnableLogicalization() {
            return this.enableLogicalization;
        }
        /**
         * 逻辑化操作开关
         */
        @SuppressWarnings("unchecked")
        public T setEnableLogicalization(boolean enableLogicalization) {
            this.enableLogicalization = enableLogicalization;
            return (T)this;
        }
        /**
         * 逻辑化查询条件的属性名称
         */
        private String logicalizationConditionPropertyName;
        /**
         * 逻辑化查询条件的条件判定的操作符（=、>、<等）
         */
        private String logicalizationConditionOperator;
        /**
         * 逻辑化查询条件的条件判定值
         */
        private Object logicalizationConditionValue;
        protected String getLogicalizationConditionPropertyName() {
            return logicalizationConditionPropertyName;
        }
        protected String getLogicalizationConditionOperator() {
            return logicalizationConditionOperator;
        }
        protected Object getLogicalizationConditionValue() {
            return logicalizationConditionValue;
        }
        /**
         * 设置逻辑化查询条件
         * @param propName 属性名称
         * @param operator 条件判定的操作符（=、>、<等）
         * @param value 条件判定值
         * @return 当前根语法对象
         */
        @SuppressWarnings("rawtypes")
        protected RootGrammarAbstract setLogicalizationCondition(String propName, String operator, Object value) {
            if (propName == null) {
                this.logicalizationConditionOperator = null;
                this.logicalizationConditionPropertyName = null;
                this.logicalizationConditionValue = null;
            } else {
                this.logicalizationConditionOperator = operator;
                this.logicalizationConditionPropertyName = propName;
                this.logicalizationConditionValue = value;
            }
            return this;
        }
    }
    /**
     * <b>select语法定义类</b>
     */
    public class Select extends RootGrammarAbstract<Select> {
        @Override
        protected Select clone() {
            Select clobj = new Select(this.sessionBox, this.propName, this.distinct);
            clobj.defaultPageSize = this.defaultPageSize;
            clobj.forCount = this.forCount;
            this.cloneSubs(clobj);
            return clobj;
        }
        @Override
        protected Grammar rootLogic() {
            if (this.subs().size() > 0) {
                Grammar from = this.subs().get(0);
                return ((FromAbstract)from).rootLogic();
            }
            return null;
        }
        /**
         * select目标的别名<br>
         * 由select、update、delete语法对象决定
         */
        private String alias;
        /* (non-Javadoc)
         * @see com.aj.frame.um.usm.procs.impl.client.UserSearchProcessor.Hql#getAlias()
         */
        @Override
        protected String alias() {
            if (this.alias == null) {
                this.alias = String.format("t%d_", this.paramNameId());
            }
            return this.alias;
        }
        @Override
        protected Class<?> resultClass() {
            Grammar from = this.subs().size() == 1 ? this.subs().get(0) : null;
            if (from != null && (from instanceof FromAbstract)) {
                return from.resultClass();
            }
            return null;
        }
        @Override
        protected Object target() {
            Grammar from = this.subs().size() == 1 ? this.subs().get(0) : null;
            if (from != null && (from instanceof FromAbstract)) {
                return from.target();
            }
            return null;
        }
        /**
         * count标记<br>
         * 指定当前Select语法是查询内容还是数量
         */
        private boolean forCount = false;
        /**
         * count标记<br>
         * 指定当前Select语法是查询内容还是数量
         */
        protected boolean isForCount() {
            return forCount;
        }
        /**
         * count标记<br>
         * 指定当前Select语法是查询内容还是数量
         */
        protected void setForCount(boolean forCount) {
            this.forCount = forCount;
        }
        /* (non-Javadoc)
         * @see com.aj.frame.db.hibernate.jsql.Jsql.Grammar#build(java.lang.StringBuilder, java.util.Map)
         */
        @Override
        public Grammar build(StringBuilder building,
                             Map<String, Object> params) {
            this.alias = null;
            // 合成select部分
            building.append("SELECT ");
            if (this.isForCount()) {
                Class<?> tcls = this.getTargetClass();
                if (tcls != null) {
                    ClassMetadata cm = this.sessionBox.getSessionFactory().getClassMetadata(tcls);
                    String ipName = null;
                    if (cm != null) {
                        ipName = cm.getIdentifierPropertyName();
                    }
                    if (ipName != null && ipName.length() > 0) {
                        building.append(String.format("COUNT(%s)", ipName));
                    } else {
                        building.append("COUNT(1)");
                    }
                } else {
                    building.append("COUNT(1)");
                }
            } else {
                String alias = this.alias();
                if (this.propName != null && !("*".equals(this.propName))) {
                    if (this.distinct) {
                        building.append("DISTINCT ");
                    }
                    if (this.propName.indexOf(",") > 0) {
                        String[] propNames = this.propName.split(",");
                        boolean isFirst = true;
                        for (String pn : propNames) {
                            if (!isFirst) {
                                building.append(',');
                            }
                            if (alias != null && alias.length() > 0) {
                                building.append(alias);
                                building.append('.');
                            }
                            building.append(this.getPropertyName(pn.trim()));
                            isFirst = false;
                        }
                    } else {
                        if (alias != null && alias.length() > 0) {
                            building.append(alias);
                            building.append('.');
                        }
                        building.append(this.getPropertyName(this.propName));
                    }
                } else {
                    if (this.isBuildAsSql()) {
                        building.append('*');
                    } else {
                        if (alias != null && alias.length() > 0) {
                            building.append(alias);
                        }
                    }
                }
            }

            if (Jsql.this.isEnableDebug() && this.subs().size() != 1) {
                F.log().dbd(String.format("Jsql逻辑错误：Select语法包含%d个From语法对象%s.", this.subs().size(), Jsql.callMeAt()));
            }
            Grammar from = this.subs().size() > 0 ? this.subs().get(0) : null;
            if (Jsql.this.isEnableDebug() && (from == null || !(from instanceof FromAbstract))) {
                F.log().dbd(String.format("Jsql逻辑错误：Select语法缺少From语法对象%s.", Jsql.callMeAt()));
            }
            // 继续合成from部分的语法
            return from == null ? null : (from.build(building, params) == null ? null : this);
        }

        private HibernateSessionBox sessionBox;
        @Override
        public Session currentSession() {
            HibernateSessionBox sessionBox = this.getSessionBox();
            return sessionBox == null ? null : sessionBox.getSession(true);
        }
        @Override
        protected HibernateSessionBox getSessionBox() {
            if (this.getParent() == null) {
                return this.sessionBox;
            }
            return this.root().getSessionBox();
        }
        /**
         * 基本构造
         * @param sessionBox 用来获取有效的hibernate会话对象的对象
         */
        private Select(HibernateSessionBox sessionBox) {
            this(sessionBox, null, false);
        }
        /**
         * 等同于"select * "
         * @return From语法对象
         */
        public From select() {
            // 新建From语法对象
            From from = new From();
            // 当前Select对象只是select()方法的载体，并不参与后续的HQL生成，因此这里指定一个独立Select对象作与From语法对象建立父子关系
            Select select = new Select(this.getSessionBox());
            select.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
            from.setParent(select);
            select.subs().add(from);
            return from;
        }
        private String propName;
        /**
         * 指定了结果属性名的构造
         * @param sessionBox 用来获取有效的hibernate会话对象的对象
         * @param propName 结果属性名
         */
        private Select(HibernateSessionBox sessionBox, String propName, boolean distinct) {
            this.sessionBox = sessionBox;
            this.propName = propName == null ? "*" : propName;
            this.distinct = distinct;
        }
        private boolean distinct;
        /**
         * 等同于"select <propName> "
         * @param propType 属性类型，其值决定最终结果集的数据类型
         * @param propName 属性名
         * @return From语法对象
         */
        public <RESULT_CLASS> FromT<RESULT_CLASS> select(Class<RESULT_CLASS> propType, String propName) {
            return this.select(propType, propName, false);
        }
        /**
         * 等同于"select <propName> "
         * @param propType 属性类型，其值决定最终结果集的数据类型
         * @param propName 属性名
         * @param distinct 排重标记
         * @return From语法对象
         */
        public <RESULT_CLASS> FromT<RESULT_CLASS> select(Class<RESULT_CLASS> propType, String propName, boolean distinct) {
            // 新建From语法对象
            FromT<RESULT_CLASS> from = new FromT<RESULT_CLASS>(propType, propName.trim());
            // 当前Select对象只是select()方法的载体，并不参与后续的HQL生成，因此这里指定一个独立Select对象作与From语法对象建立父子关系
            Select select = new Select(this.getSessionBox(), propName.trim(), distinct);
            select.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
            from.setParent(select);
            select.subs().add(from);
            return from;
        }

        private static final int DEFAULT_PAGE_SIZE = 256;
        /**
         * 缺省分页大小<br>
         * 用于在执行查询类操作时，未指定PagingInfo对象或者指定的PagingInfo对象未指定有效的pageSize属性。
         */
        private Integer defaultPageSize = DEFAULT_PAGE_SIZE;
        /**
         * 缺省分页大小<br>
         * 用于在执行查询类操作时，未指定PagingInfo对象或者指定的PagingInfo对象未指定有效的pageSize属性。
         */
        protected Integer getDefaultPageSize() {
            return defaultPageSize;
        }
        /**
         * 缺省分页大小<br>
         * 用于在执行查询类操作时，未指定PagingInfo对象或者指定的PagingInfo对象未指定有效的pageSize属性。
         */
        protected void setDefaultPageSize(Integer defaultPageSize) {
            this.defaultPageSize = defaultPageSize;
            if (this.defaultPageSize == null || this.defaultPageSize.intValue() <= 0) {
                this.defaultPageSize = DEFAULT_PAGE_SIZE;
            }
        }
    }

    /**
     * <b>更新类语法对象抽象基类</b>
     */
    private abstract class UpdateAbstract<T> extends RootGrammarAbstract<T> {
        /**
         * @param sessionBox 用来获取有效的hibernate会话对象的对象
         */
        private UpdateAbstract(HibernateSessionBox sessionBox) {
            this(sessionBox, null, false);
        }
        /**
         * @param sessionBox 用来获取有效的hibernate会话对象的对象
         * @param targetClass 目标类型
         * @param updateAll 是否允许无条件执行更新操作
         */
        private UpdateAbstract(HibernateSessionBox sessionBox, Class<?> targetClass, boolean updateAll) {
            this.sessionBox = sessionBox;
            this.targetClass = targetClass;
            this.updateAll = updateAll;
        }
        private HibernateSessionBox sessionBox;
        private Class<?> targetClass;
        /**
         * 是否允许无条件执行更新操作<br>
         * 设置这个条件的目的在于防止意外的执行无条件更新类操作。
         */
        private boolean updateAll;
        protected boolean isUpdateAll() {
            return updateAll;
        }
        @Override
        public Session currentSession() {
            HibernateSessionBox sessionBox = this.getSessionBox();
            return sessionBox == null ? null : sessionBox.getSession(true);
        }
        @Override
        protected HibernateSessionBox getSessionBox() {
            if (this.getParent() == null) {
                return this.sessionBox;
            }
            return this.root().getSessionBox();
        }
        /**
         * select目标的别名<br>
         * 由select、update、delete语法对象决定
         */
        protected String alias;
        /* (non-Javadoc)
         * @see com.aj.frame.um.usm.procs.impl.client.UserSearchProcessor.Hql#getAlias()
         */
        @Override
        protected String alias() {
            if (this.alias == null) {
                this.alias = String.format("t%d_", this.paramNameId());
            }
            return this.alias;
        }
        @Override
        protected Class<?> resultClass() {
            return Integer.class;
        }
        @Override
        protected Object target() {
            return this.targetClass;
        }
    }
    /**
     * <b>set语法定义类</b>
     * @param <TARGET_CLASS>
     */
    public class Set<TARGET_CLASS> extends GrammarAbstract {
        @Override
        protected Set<TARGET_CLASS> clone() {
            Set<TARGET_CLASS> clobj = new Set<TARGET_CLASS>();
            clobj.args = this.args;
            clobj.forAdditional = this.forAdditional;
            this.cloneSubs(clobj);
            return clobj;
        }
        @Override
        protected Grammar rootLogic() {
            if (this.subs().size() > 0) {
                Grammar where = this.subs().get(0);
                return ((Where<?, ?>)where).rootLogic();
            }
            return null;
        }
        private Set() {
        }
        @Override
        public Grammar build(StringBuilder building, Map<String, Object> params) {
            building.append("SET ");
            if (this.args == null) {
                F.log().dbd(String.format("Jsql逻辑错误：没有为Set语法指定有效的参数%s.", Jsql.callMeAt()));
                return null;
            }
            if (this.args.getClass().isArray()) {
                String alias = this.alias();
                String[] names = (String[]) this.args;
                for (int i = 0; i < names.length; i++) {
                    if (i > 0) {
                        building.append(',');
                    }
                    if (alias != null && alias.length() > 0) {
                        building.append(alias);
                        building.append('.');
                    }
                    building.append(this.getPropertyName(names[i]));
                    building.append("=NULL");
                }
            } else if (this.args instanceof String) {
                String alias = this.alias();
                if (alias != null && alias.length() > 0) {
                    building.append(alias);
                    building.append('.');
                }
                building.append(this.getPropertyName((String)this.args));
                building.append("=NULL");
            } else if (this.args instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>)this.args;
                Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
                String alias = this.alias();
                boolean isFirst = true;
                while (it.hasNext()) {
                    if (!isFirst) {
                        building.append(',');
                    }
                    if (alias != null && alias.length() > 0) {
                        building.append(alias);
                        building.append('.');
                    }
                    Map.Entry<String, Object> en = it.next();
                    building.append(this.getPropertyName(en.getKey()));
                    if (en.getValue() == null) {
                        building.append("=NULL");
                    } else {
                        building.append('=');
                        if (this.forAdditional) {
                            building.append(this.getPropertyName(en.getKey()));
                            building.append('+');
                        }
                        String paramName = String.format("p%d_", this.paramNameId());
                        building.append(":" + paramName);
                        params.put(paramName, en.getValue());
                    }
                    isFirst = false;
                }
            } else {
                this.buildConditions(this.args, "=", ",", false, this.forAdditional, false, building, params);
            }

            if (Jsql.this.isEnableDebug() && this.subs().size() != 1) {
                F.log().dbd(String.format("Jsql逻辑错误：Update语法包含%d个Where语法对象%s.", this.subs().size(), Jsql.callMeAt()));
            }
            Grammar where = this.subs().size() > 0 ? this.subs().get(0) : null;
            if (Jsql.this.isEnableDebug() && (where == null || !(where instanceof Where))) {
                F.log().dbd(String.format("Jsql逻辑错误：Update语法缺少Where语法对象%s.", Jsql.callMeAt()));
            }
            // 继续合成where部分的语法
            return where == null ? null : (where.build(building, params) == null ? null : this);
        }
        /**
         * set语法的参数<br>
         * 可以是一个样本对象，也可以是一个或多个指定需要设置为null的属性名称
         */
        private Object args;
        /**
         * 追加模式标记<br>
         * 如果为true则生成"set propName = propName + value"格式的set语法，否则生成常规"set propName = value"格式的set语法
         */
        private boolean forAdditional;
        /**
         * set语法的通用实现
         * @param args set语法的参数
         * @return 用于后续语法拼接的where语法对象
         */
        private Where<TARGET_CLASS, TARGET_CLASS> setArgs(Object args) {
            this.args = args;
            // 新建Where语法对象
            Where<TARGET_CLASS, TARGET_CLASS> where = new Where<TARGET_CLASS, TARGET_CLASS>();
            where.setParent(this);
            this.subs().add(where);
            return where;
        }
        /**
         * set语法<br>
         * 样本对象example的所有非空属性都将作为set语法的执行参数
         * @param example set语法参数的样本对象
         * @return 用于后续语法拼接的where语法对象
         */
        public Where<TARGET_CLASS, TARGET_CLASS> set(TARGET_CLASS example) {
            return this.setArgs(example);
        }
        /**
         * set语法<br>
         * 样本对象example的所有非空属性都将作为set语法的执行参数
         * @param example set语法参数的样本对象
         * @param forAdditional 如果为true则生成"set propName = propName + value"格式的set语法
         * @return 用于后续语法拼接的where语法对象
         */
        public Where<TARGET_CLASS, TARGET_CLASS> set(TARGET_CLASS example, boolean forAdditional) {
            this.forAdditional = forAdditional;
            return this.setArgs(example);
        }
        /**
         * 专用于将指定属性设置为null的set语法
         * @param propNames 需要设置为null的属性名称
         * @return 用于后续语法拼接的where语法对象
         */
        public Where<TARGET_CLASS, TARGET_CLASS> setNull(String[] propNames) {
            return this.setArgs(propNames);
        }
        /**
         * 专用于将指定属性设置为null的set语法
         * @param propName 需要设置为null的属性名称
         * @return 用于后续语法拼接的where语法对象
         */
        public Where<TARGET_CLASS, TARGET_CLASS> setNull(String propName) {
            return this.setArgs(propName);
        }
    }
    /**
     * <b>update语法定义类</b>
     */
    public class Update extends UpdateAbstract<Update> {
        @Override
        protected Update clone() {
            Update clobj = new Update(super.sessionBox, super.targetClass, super.updateAll);
            this.cloneSubs(clobj);
            return clobj;
        }
        @Override
        protected Grammar rootLogic() {
            if (this.subs().size() > 0) {
                Grammar set = this.subs().get(0);
                return ((Set<?>)set).rootLogic();
            }
            return null;
        }
        private Update(HibernateSessionBox sessionBox) {
            super(sessionBox, null, false);
        }
        private Update(HibernateSessionBox sessionBox, Class<?> targetClass, boolean updateAll) {
            super(sessionBox, targetClass, updateAll);
        }
        @Override
        public Grammar build(StringBuilder building, Map<String, Object> params) {
            this.alias = null;
            // 合成delete部分
            building.append("UPDATE ");
            building.append(this.getClassName((Class<?>)this.target()));
            building.append(' ');
            String alias = this.alias();
            if (alias != null && alias.length() > 0) {
                building.append(alias);
                building.append(' ');
            }

            if (Jsql.this.isEnableDebug() && (this.subs().size() != 1)) {
                F.log().dbd(String.format("Jsql逻辑错误：Delete语法包含%d个Set语法对象%s.", this.subs().size(), Jsql.callMeAt()));
            }
            Grammar set = this.subs().size() > 0 ? this.subs().get(0) : null;
            if (Jsql.this.isEnableDebug() && (set == null || !(set instanceof Set))) {
                F.log().dbd(String.format("Jsql逻辑错误：Delete语法缺少Set语法对象%s.", Jsql.callMeAt()));
            }
            // 继续合成set部分的语法
            return set == null ? null : (set.build(building, params) == null ? null : this);
        }
        /**
         * update语法
         * @param targetClass update语法的目标类型
         * @return 用于后续语法拼接的set语法对象
         */
        public <TARGET_CLASS> Set<TARGET_CLASS> update(Class<TARGET_CLASS> targetClass) {
            // 新建Set语法对象
            Set<TARGET_CLASS> set = new Set<TARGET_CLASS>();
            // 当前Update对象只是update()方法的载体，并不参与后续的HQL生成，因此这里指定一个独立Update对象作与Set语法对象建立父子关系
            Update update = new Update(this.getSessionBox(), targetClass, false);
            update.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
            set.setParent(update);
            update.subs().add(set);
            return set;
        }
        /**
         * 支持无条件更新的update语法
         * @param targetClass update语法的目标类型
         * @return 用于后续语法拼接的set语法对象
         */
        public <TARGET_CLASS> Set<TARGET_CLASS> updateAll(Class<TARGET_CLASS> targetClass) {
            // 新建Set语法对象
            Set<TARGET_CLASS> set = new Set<TARGET_CLASS>();
            // 当前Update对象只是update()方法的载体，并不参与后续的HQL生成，因此这里指定一个独立Update对象作与Set语法对象建立父子关系
            Update update = new Update(this.getSessionBox(), targetClass, true);
            update.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
            set.setParent(update);
            update.subs().add(set);
            return set;
        }
    }
    /**
     * <b>delete语法定义类</b>
     */
    public class Delete extends UpdateAbstract<Delete> {
        @Override
        protected Delete clone() {
            Delete clobj = new Delete(super.sessionBox, super.targetClass, super.updateAll);
            clobj.updatePropertyNameForRemove = this.updatePropertyNameForRemove;
            clobj.updateValueForRemove = this.updateValueForRemove;
            this.cloneSubs(clobj);
            return clobj;
        }
        @Override
        protected Grammar rootLogic() {
            if (this.subs().size() > 0) {
                Grammar where = this.subs().get(0);
                return ((Where<?, ?>)where).rootLogic();
            }
            return null;
        }
        @Override
        protected String alias() {
            return null;
        }
        /**
         * 实现逻辑删除效果的更新属性名称
         */
        private String updatePropertyNameForRemove;
        /**
         * 实现逻辑删除效果的更新属性值
         */
        private Object updateValueForRemove;
        private Delete(HibernateSessionBox sessionBox, String propName, Object value) {
            super(sessionBox, null, false);
            this.updatePropertyNameForRemove = propName;
            this.updateValueForRemove = value;
        }
        private Delete(HibernateSessionBox sessionBox, Class<?> targetClass, boolean updateAll) {
            super(sessionBox, targetClass, updateAll);
        }
        @Override
        public Grammar build(StringBuilder building, Map<String, Object> params) {
            this.alias = null;
            // 合成delete部分
            building.append("DELETE FROM ");
            building.append(this.getClassName((Class<?>)this.target()));
            building.append(' ');

            if (Jsql.this.isEnableDebug() && this.subs().size() != 1) {
                F.log().dbd(String.format("Jsql逻辑错误：Delete语法包含%d个Where语法对象%s.", this.subs().size(), Jsql.callMeAt()));
            }
            Grammar where = this.subs().size() > 0 ? this.subs().get(0) : null;
            if (Jsql.this.isEnableDebug() && (where == null || !(where instanceof Where))) {
                F.log().dbd(String.format("Jsql逻辑错误：Delete语法缺少Where语法对象%s.", Jsql.callMeAt()));
            }
            // 继续合成where部分的语法
            return where == null ? null : (where.build(building, params) == null ? null : this);
        }
        /**
         * 逻辑化操作开关
         */
        public boolean isEnableLogicalization() {
            return super.isEnableLogicalization() && this.updatePropertyNameForRemove != null;
        }
        /**
         * delete语法<br>
         * 如果创建当前Delete语法对象的Jsql对象配置了有效逻辑删除参数，并且当前Delete语法对象的逻辑化操作未被禁止，则<br>
         * 删除操作将通过Update语法实现逻辑删除效果。
         * @param targetClass delete语法的目标类型
         * @return 用于后续语法拼接的where语法对象
         */
        public <TARGET_CLASS> Where<TARGET_CLASS, TARGET_CLASS> delete(Class<TARGET_CLASS> targetClass) {
            if (this.updatePropertyNameForRemove != null && this.isEnableLogicalization()) {
                // 通过获取逻辑删除标志属性的类型判断目标类型是否支持逻辑删除操作
                Class<?> pc = this.getPropertyClass(targetClass, this.updatePropertyNameForRemove);
                if (pc != null) { // 目标类型支持逻辑删除操作
                    Class<?> vc = null;
                    if (this.updateValueForRemove != null) {
                        vc = this.updateValueForRemove.getClass();
                    }
                    if (vc == null || vc.equals(pc)) {
                        // 通过Update语法实现逻辑删除效果
                        Update update = new Update(this.getSessionBox());
                        update.setEnableExecuteCallback(this.isEnableExecuteCallback());
                        update.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
                        Map<String, Object> args = new HashMap<String, Object>();
                        args.put(this.updatePropertyNameForRemove, this.updateValueForRemove);
                        return update.update(targetClass).setArgs(args);
                    } else {
                        F.log().dbd(String.format("Jsql逻辑错误：逻辑删除参数指定的'%s'属性值类型与'%s.%s'的'%s'属性类型不一致%s.", this.updatePropertyNameForRemove, targetClass.getSimpleName(), this.updatePropertyNameForRemove, vc.getSimpleName(), Jsql.callMeAt()));
                        return null;
                    }
                }
            }

            // 新建Where语法对象
            Where<TARGET_CLASS, TARGET_CLASS> where = new Where<TARGET_CLASS, TARGET_CLASS>();
            // 当前Delete对象只是delete()方法的载体，并不参与后续的HQL生成，因此这里指定一个独立Delete对象作与Where语法对象建立父子关系
            Delete delete = new Delete(this.getSessionBox(), targetClass, false);
            delete.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
            where.setParent(delete);
            delete.subs().add(where);
            return where;
        }
        /**
         * 支持无条件删除的delete语法<br>
         * 如果创建当前Delete语法对象的Jsql对象配置了有效逻辑删除参数，并且当前Delete语法对象的逻辑化操作未被禁止，则<br>
         * 删除操作将通过Update语法实现逻辑删除效果。
         * @param targetClass delete语法的目标类型
         * @return 用于后续执行的结果对象
         */
        public <TARGET_CLASS> Result<TARGET_CLASS> deleteAll(Class<TARGET_CLASS> targetClass) {
            if (this.updatePropertyNameForRemove != null && this.isEnableLogicalization()) {
                // 通过获取逻辑删除标志属性的类型判断目标类型是否支持逻辑删除操作
                Class<?> pc = this.getPropertyClass(targetClass, this.updatePropertyNameForRemove);
                if (pc != null) { // 目标类型支持逻辑删除操作
                    Class<?> vc = null;
                    if (this.updateValueForRemove != null) {
                        vc = this.updateValueForRemove.getClass();
                    }
                    if (vc == null || vc.equals(pc)) {
                        // 通过Update语法实现逻辑删除效果
                        Update update = new Update(this.getSessionBox());
                        update.setEnableExecuteCallback(this.isEnableExecuteCallback());
                        update.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
                        Map<String, Object> args = new HashMap<String, Object>();
                        args.put(this.updatePropertyNameForRemove, this.updateValueForRemove);
                        return update.updateAll(targetClass).setArgs(args);
                    } else {
                        F.log().dbd(String.format("Jsql逻辑错误：逻辑删除参数指定的'%s'属性值类型与'%s.%s'的'%s'属性类型不一致%s.", this.updatePropertyNameForRemove, targetClass.getSimpleName(), this.updatePropertyNameForRemove, vc.getSimpleName(), Jsql.callMeAt()));
                        return null;
                    }
                }
            }
            // 新建Where语法对象
            Where<TARGET_CLASS, TARGET_CLASS> where = new Where<TARGET_CLASS, TARGET_CLASS>();
            // 当前Delete对象只是delete()方法的载体，并不参与后续的HQL生成，因此这里指定一个独立Delete对象作与Result语法对象建立父子关系
            Delete delete = new Delete(this.getSessionBox(), targetClass, true);
            delete.setLogicalizationCondition(this.getLogicalizationConditionPropertyName(), this.getLogicalizationConditionOperator(), this.getLogicalizationConditionValue()).setEnableLogicalization(this.isEnableLogicalization());
            where.setParent(delete);
            delete.subs().add(where);
            return where;
        }
    }
    /**
     * 逻辑化查询条件的属性名称
     */
    private String logicalizationConditionPropertyName;
    /**
     * 逻辑化查询条件的条件判定的操作符（=、>、<等）
     */
    private String logicalizationConditionOperator;
    /**
     * 逻辑化查询条件的条件判定值
     */
    private Object logicalizationConditionValue;
    /**
     * 用来获取可用的hibernate会话对象的对象
     */
    private HibernateSessionBox sessionBox;
    /**
     * 是否开启调试模式
     */
    private boolean enableDebug = true;
    /**
     * 使用当前Jsql对象的应用系统的会话对象
     */
    private Object applicationSession;
    /**
     * <b>用来防止Jsql三个回调接口同线程重入的回调接口代理</b>
     */
    private static class JsqlExecuteXProxy implements JsqlExecuteBefore, JsqlExecuteAfter, JsqlExecuteCompleted, JsqlExecuteBeanChang {
        /**
         * 用来标记回调接口是否已经进入的线程变量
         */
        private final static ThreadLocal<Boolean> currentThreadEntered = new ThreadLocal<Boolean>() {
            protected Boolean initialValue(){
                return false;
            }
        };
        /**
         * 当前Jsql对象的执行前回调接口
         */
        private JsqlExecuteBefore executeBefore;
        /**
         * 当前Jsql对象的执行后回调接口
         */
        private JsqlExecuteAfter executeAfter;
        /**
         * 当前Jsql对象的执行完成回调接口<br>
         * 这个回调主要用于分析SQL执行效率
         */
        private JsqlExecuteCompleted executeCompleted;
        /**
         * 当前Jsql对象的持久化Bean对象修改操作回调接口<br>
         * 这个回调主要为数据库同步提供支持
         */
        private JsqlExecuteBeanChang executeBeanChanged;

        public JsqlExecuteBeanChang getExecuteBeanChanged() {
            if (currentThreadEntered.get()) {
                return null;
            }
            return this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged;
        }

        public void setExecuteBeanChanged(JsqlExecuteBeanChang executeBeanChanged) {
            if (executeBeanChanged == null) {
                this.executeBeanChanged = null;
            } else {
                JsqlExecuteBeanChang jsqlChang = this.getDefaultJsqlExecuteX().getExecuteBeanChanged();
                if (jsqlChang == null || this.executeBeanChanged == null) {
                    // 这里留下一个特殊应用模式，先把属性设置为null，再设置目标回调接口就可以绕过Jsql的缺省设置
                    this.executeBeanChanged = executeBeanChanged;
                } else {
                    this.executeBeanChanged = new JsqlExecuteBeanChangSet(new JsqlExecuteBeanChang[]{executeBeanChanged, jsqlChang});
                }
                if (this.executeBeanChanged instanceof JsqlExecuteBeanChangSet) {
                    List<JsqlExecuteBeanChang> changs = new ArrayList<JsqlExecuteBeanChang>();
                    List<JsqlExecuteBeanChang> orders = new ArrayList<JsqlExecuteBeanChang>();
                    Iterator<JsqlExecuteBeanChang> it = ((JsqlExecuteBeanChangSet) this.executeBeanChanged).getExecuteBeanChangSet().iterator();
                    while (it.hasNext()) {
                        JsqlExecuteBeanChang next = it.next();
                        if (next instanceof JsqlExecuteXOrder) {
                            orders.add(next);
                        } else {
                            changs.add(next);
                        }
                    }
                    ((JsqlExecuteBeanChangSet) this.executeBeanChanged).getExecuteBeanChangSet().clear();
                    if (changs.size() > 1) {
                        ((JsqlExecuteBeanChangSet) this.executeBeanChanged).getExecuteBeanChangSet().addAll(changs);
                    }
                    if (orders.size() > 1) {
                        Collections.sort(orders, new Comparator<JsqlExecuteBeanChang>(){

                            @Override
                            public int compare(JsqlExecuteBeanChang o1,
                                               JsqlExecuteBeanChang o2) {
                                return ((JsqlExecuteXOrder)o1).getExecuteOrderId() - ((JsqlExecuteXOrder)o2).getExecuteOrderId();
                            }});
                        it = orders.iterator();
                        while (it.hasNext()) {
                            JsqlExecuteBeanChang next = it.next();
                            ((JsqlExecuteBeanChangSet) this.executeBeanChanged).getExecuteBeanChangSet().add(next);
                        }
                    }
                }
            }
        }

        public JsqlExecuteCompleted getExecuteCompleted() {
            if (currentThreadEntered.get()) {
                return null;
            }
            return this.executeCompleted == this ? this.getDefaultJsqlExecuteX().getExecuteCompleted() : this.executeCompleted;
        }

        public void setExecuteCompleted(JsqlExecuteCompleted executeCompleted) {
            if (executeCompleted == null) {
                this.executeCompleted = null;
            } else {
                JsqlExecuteCompleted jsqlCompleted = this.getDefaultJsqlExecuteX().getExecuteCompleted();
                if (jsqlCompleted == null || this.executeCompleted == null) {
                    // 这里留下一个特殊应用模式，先把属性设置为null，再设置目标回调接口就可以绕过Jsql的缺省设置
                    this.executeCompleted = executeCompleted;
                } else {
                    this.executeCompleted = new JsqlExecuteCompletedSet(new JsqlExecuteCompleted[] {executeCompleted, jsqlCompleted});
                }
            }
        }

        public JsqlExecuteBefore getExecuteBefore() {
            if (currentThreadEntered.get()) {
                return null;
            }
            return this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore;
        }

        public void setExecuteBefore(JsqlExecuteBefore executeBefore) {
            if (executeBefore == null) {
                this.executeBefore = null;
            } else {
                JsqlExecuteBefore jsqlBefore = this.getDefaultJsqlExecuteX().getExecuteBefore();
                if (jsqlBefore == null || this.executeBefore == null) {
                    // 这里留下一个特殊应用模式，先把属性设置为null，再设置目标回调接口就可以绕过Jsql的缺省设置
                    this.executeBefore = executeBefore;
                } else {
                    this.executeBefore = new JsqlExecuteBeforeSet(new JsqlExecuteBefore[]{executeBefore, jsqlBefore});
                }
            }
        }

        public JsqlExecuteAfter getExecuteAfter() {
            if (currentThreadEntered.get()) {
                return null;
            }
            return this.executeAfter == this ? this.getDefaultJsqlExecuteX().getExecuteAfter() : this.executeAfter;
        }

        public void setExecuteAfter(JsqlExecuteAfter executeAfter) {
            if (executeAfter == null) {
                this.executeAfter = null;
            } else {
                JsqlExecuteAfter jsqlAfter = this.getDefaultJsqlExecuteX().getExecuteAfter();
                if (jsqlAfter == null || this.executeAfter == null) {
                    // 这里留下一个特殊应用模式，先把属性设置为null，再设置目标回调接口就可以绕过Jsql的缺省设置
                    this.executeAfter = executeAfter;
                } else {
                    this.executeAfter = new JsqlExecuteAfterSet(new JsqlExecuteAfter[]{executeAfter, jsqlAfter});
                }
            }
        }

        public JsqlExecuteXProxy() {
            this.executeAfter = this;
            this.executeBeanChanged = this;
            this.executeBefore = this;
            this.executeCompleted = this;
        }

        @Override
        public void listBefore(Jsql jsql, Result<?> result, Class<?> targetClass) {
            try {
                currentThreadEntered.set(true);
                (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).listBefore(jsql, result, targetClass);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void executeUpdateBefore(Jsql jsql, Result<?> result,
                                        Class<?> targetClass, Object setArgs) {
            try {
                currentThreadEntered.set(true);
                (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).executeUpdateBefore(jsql, result, targetClass, setArgs);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        @Override
        public void saveBefore(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).saveBefore(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        @Override
        public void updateBefore(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).updateBefore(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        @Override
        public void mergeBefore(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).mergeBefore(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        @Override
        public void deleteBefore(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).deleteBefore(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        @Override
        public boolean loadBefore(Jsql jsql, Class<?> targetClass,
                                  Serializable pkValue) {
            try {
                currentThreadEntered.set(true);
                return (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).loadBefore(jsql, targetClass, pkValue);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        @Override
        public boolean getBefore(Jsql jsql, Class<?> targetClass,
                                 Serializable pkValue) {
            try {
                currentThreadEntered.set(true);
                return (this.executeBefore == this ? this.getDefaultJsqlExecuteX().getExecuteBefore() : this.executeBefore).getBefore(jsql, targetClass, pkValue);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void listAfter(Jsql jsql, List<?> results) {
            try {
                currentThreadEntered.set(true);
                (this.executeAfter == this ? this.getDefaultJsqlExecuteX().getExecuteAfter() : this.executeAfter).listAfter(jsql, results);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public Object getAfter(Jsql jsql, Object result) {
            try {
                currentThreadEntered.set(true);
                return (this.executeAfter == this ? this.getDefaultJsqlExecuteX().getExecuteAfter() : this.executeAfter).getAfter(jsql, result);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public Object loadAfter(Jsql jsql, Object result) {
            try {
                currentThreadEntered.set(true);
                return (this.executeAfter == this ? this.getDefaultJsqlExecuteX().getExecuteAfter() : this.executeAfter).loadAfter(jsql, result);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void listCompleted(Jsql jsql, Result<?> result, String hql,
                                  long executeTime) {
            try {
                currentThreadEntered.set(true);
                (this.executeCompleted == this ? this.getDefaultJsqlExecuteX().getExecuteCompleted() : this.executeCompleted).listCompleted(jsql, result, hql, executeTime);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void executeUpdateCompleted(Jsql jsql, Result<?> result,
                                           String hql, long executeTime) {
            try {
                currentThreadEntered.set(true);
                (this.executeCompleted == this ? this.getDefaultJsqlExecuteX().getExecuteCompleted() : this.executeCompleted).executeUpdateCompleted(jsql, result, hql, executeTime);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void saved(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).saved(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void deleted(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).deleted(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void updated(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).updated(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void batchDeleting(Jsql jsql, Delete deleter, Class<?> targetClass, UUID batchId) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).batchDeleting(jsql, deleter, targetClass, batchId);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void batchDeleted(Jsql jsql, Delete deleter, Class<?> targetClass, UUID batchId, int deletedObjects) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).batchDeleted(jsql, deleter, targetClass, batchId, deletedObjects);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void batchDeleteCanceled(Jsql jsql, Delete deleter, Class<?> targetClass, UUID batchId) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).batchDeleteCanceled(jsql, deleter, targetClass, batchId);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void batchUpdating(Jsql jsql, Update updater, Class<?> targetClass, UUID batchId) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).batchUpdating(jsql, updater, targetClass, batchId);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void batchUpdated(Jsql jsql, Update updater, Class<?> targetClass, UUID batchId, int updatedObjects) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).batchUpdated(jsql, updater, targetClass, batchId, updatedObjects);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void batchUpdateCanceled(Jsql jsql, Update updater, Class<?> targetClass, UUID batchId) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).batchUpdateCanceled(jsql, updater, targetClass, batchId);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void saving(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).saving(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void saveCanceled(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).saveCanceled(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void deleting(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).deleting(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void deleteCanceled(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).deleteCanceled(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void updating(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).updating(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }

        @Override
        public void updateCanceled(Jsql jsql, Object bean) {
            try {
                currentThreadEntered.set(true);
                (this.executeBeanChanged == this ? this.getDefaultJsqlExecuteX().getExecuteBeanChanged() : this.executeBeanChanged).updateCanceled(jsql, bean);
            } finally {
                currentThreadEntered.set(false);
            }
        }
        private static DefaultJsqlExecuteX[] defaultJsqlExecuteX = new DefaultJsqlExecuteX[1];
        private DefaultJsqlExecuteX getDefaultJsqlExecuteX() {
            synchronized (defaultJsqlExecuteX) {
                if (defaultJsqlExecuteX[0] == null) {
                    defaultJsqlExecuteX[0] = new DefaultJsqlExecuteX();
                }
            }
            return defaultJsqlExecuteX[0];
        }
    }
    private JsqlExecuteXProxy executeXProxy = new JsqlExecuteXProxy();
    /**
     * 使用当前Jsql对象的应用系统的会话对象
     */
    public Object getApplicationSession() {
        return applicationSession;
    }
    /**
     * 使用当前Jsql对象的应用系统的会话对象
     */
    public void setApplicationSession(Object applicationSession) {
        this.applicationSession = applicationSession;
    }
    /**
     * 当前Jsql对象的执行前回调接口
     */
    public JsqlExecuteBefore getExecuteBefore() {
        return this.executeXProxy.getExecuteBefore() == null ? null : this.executeXProxy;
    }
    /**
     * 当前Jsql对象的执行前回调接口
     */
    public void setExecuteBefore(JsqlExecuteBefore executeBefore) {
        this.executeXProxy.setExecuteBefore(executeBefore);
    }
    /**
     * 当前Jsql对象的执行后回调接口
     */
    public JsqlExecuteAfter getExecuteAfter() {
        return this.executeXProxy.getExecuteAfter() == null ? null : this.executeXProxy;
    }
    /**
     * 当前Jsql对象的执行后回调接口
     */
    public void setExecuteAfter(JsqlExecuteAfter executeAfter) {
        this.executeXProxy.setExecuteAfter(executeAfter);
    }
    /**
     * 当前Jsql对象的执行完成回调接口<br>
     * 这个回调主要用于分析SQL执行效率
     */
    public JsqlExecuteCompleted getExecuteCompleted() {
        return this.executeXProxy.getExecuteCompleted() == null ? null : this.executeXProxy;
    }
    /**
     * 当前Jsql对象的执行完成回调接口<br>
     * 这个回调主要用于分析SQL执行效率
     */
    public void setExecuteCompleted(JsqlExecuteCompleted executed) {
        this.executeXProxy.setExecuteCompleted(executed);
    }
    /**
     * 当前Jsql对象的持久化Bean对象修改操作回调接口<br>
     * 这个回调主要为数据库同步提供支持
     */
    public JsqlExecuteBeanChang getExecuteBeanChanged() {
        return this.executeXProxy.getExecuteBeanChanged() == null ? null : this.executeXProxy;
    }
    /**
     * 当前Jsql对象的持久化Bean对象修改操作回调接口<br>
     * 这个回调主要为数据库同步提供支持
     */
    public void setExecuteBeanChanged(JsqlExecuteBeanChang executeBeanChanged) {
        this.executeXProxy.setExecuteBeanChanged(executeBeanChanged);
    }
    /**
     * 是否开启调试模式
     */
    public boolean isEnableDebug() {
        return enableDebug;
    }
    /**
     * 是否开启调试模式
     */
    public void setEnableDebug(boolean enableDebug) {
        this.enableDebug = enableDebug;
    }
    /**
     * 当前Jsql对象所使用的数据源名称
     */
    public String getDataSourceName() {
        String ds = this.sessionBox == null ? null : this.sessionBox.getDataSourceName();
        return ds == null ? "jsql" : ds;
    }
    public Jsql(Session session) {
        this(session, null);
    }
    public Jsql(HibernateSessionBox sessionBox) {
        this.sessionBox = sessionBox;
    }
    public Jsql(Session session, String dataSourceName) {
        this.sessionBox = new DefaultHibernateSessionBox(session).setDataSourceName(dataSourceName);
    }
    /**
     * 因为完全原因已经取消<br>
     * @see com.aj.frame.db.hibernate.jsql.Jsql#doWork
     * @return 当前Jsql对象内部的hibernate会话对象。
     */
    @Deprecated
    public Session currentSession() {
        return this.sessionBox == null ? null : this.sessionBox.getSession(true);
    }
    /**
     * 直接使用Jsql内部的hibernate会话对象
     * @param work 回调接口
     * @param forUpdate 如果为true表示需要改写数据，否则表示仅用于查询
     * @return 当前Jsql对象
     */
    public Jsql doWork(JsqlWork work, boolean forUpdate) {
        Session session = this.sessionBox.getSession(forUpdate);
        try {
            work.work(session);
        } catch (Exception e) {
            throw new AJFrameRuntimeException(Ids.Ajf.DbErrors.数据库未知错误, e);
        } finally {
            this.sessionBox.freeSession(session);
        }
        return this;
    }
    /**
     * hibernate会话对象doWork方法的封装
     * @param work hibernate的jdbc应用回调接口
     * @param forUpdate 如果为true表示需要改写数据，否则表示仅用于查询
     * @return 当前Jsql对象
     */
    public Jsql doWork(final Work work, boolean forUpdate) {
        this.doWork(new JsqlWork(){
            @Override
            public void work(Session session) throws Exception {
                session.doWork(work);
            }}, forUpdate);
        return this;
    }
    /**
     * 由jsql对象开启了事务的hibernate会话对象
     */
    private Session transactionSession;
    /**
     * 开启事务<br>
     * 如果当前Jsql会话对象处于事务托管模式，则有可能因为事务已经开启而产生异常。
     * @return 当前Jsql对象
     */
    public Jsql beginTransaction() {
        synchronized (this) {
            Session session = this.sessionBox.getSession(true);
            boolean hasError = true;
            try {
                session.beginTransaction();
                this.transactionSession = session;
                hasError = false;
            } finally {
                if (hasError) {
                    this.sessionBox.freeSession(session);
                }
            }
        }
        return this;
    }
    /**
     * 提交事务
     * @return 当前Jsql对象
     */
    public Jsql commit() {
        synchronized (this) {
            boolean commited = false;
            try {
                this.transactionSession.getTransaction().commit();
                commited = true;
            } finally {
                if (this.transactionSession != null) {
                    if (!commited) {
                        try {
                            this.transactionSession.getTransaction().rollback();
                        } catch (Exception e) {}
                    }
                    this.sessionBox.freeSession(this.transactionSession);
                    this.transactionSession = null;
                }
            }
        }
        return this;
    }
    /**
     * 回滚事务
     * @return 当前Jsql对象
     */
    public Jsql rollback() {
        synchronized (this) {
            try {
                if (this.transactionSession != null) {
                    Transaction trans = this.transactionSession.getTransaction();
                    if (trans != null) {
                        trans.rollback();
                    }
                }
            } finally {
                if (this.transactionSession != null) {
                    this.sessionBox.freeSession(this.transactionSession);
                    this.transactionSession = null;
                }
            }
        }
        return this;
    }
    public Jsql flush() {
        this.doWork(new JsqlWork(){
            @Override
            public void work(Session session) throws Exception {
                session.flush();
            }}, false);
        return this;
    }
    public Jsql clear() {
        this.doWork(new JsqlWork(){
            @Override
            public void work(Session session) throws Exception {
                session.clear();
            }}, false);
        return this;
    }
    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> cls, Serializable pkValue) {
        if (this.getExecuteBefore() != null) {
            if (this.getExecuteBefore().loadBefore(this, cls, pkValue)) {
                return null;
            }
        }
        T obj = null;
        Session session = this.sessionBox.getSession(false);
        try {
            if (this.logicalizationConditionPropertyName != null && this.logicalizationConditionPropertyName.length() > 0
                && this.logicalizationConditionOperator != null && this.logicalizationConditionOperator.length() > 0) {
                // 如果当前对象已经指定了逻辑化操作相关参数则通过select语法实现get效果，籍此以统一的方式排出逻辑删除了的对象
                ClassMetadata cm = session.getSessionFactory().getClassMetadata(cls);
                String pkName = cm.getIdentifierPropertyName();
                if (pkName != null && pkName.length() > 0) {
                    Logic<T, T> selector = this
                        .select()
                        .from(cls)
                        .where(pkName).is("=", pkValue);
                    selector.setEnableExecuteCallback(false);
                    List<T> objs = selector.list();
                    if (objs != null && objs.size() > 0) {
                        obj = objs.get(0);
                    }
                }
            } else {
                long time = System.currentTimeMillis();
                obj = (T) session.get(cls, pkValue);
                if (Jsql.this.getExecuteCompleted() != null) {
                    Jsql.this.getExecuteCompleted().listCompleted(Jsql.this, null, String.format("get(%s, %s)", cls.getName(), pkValue.toString()), System.currentTimeMillis() - time);
                }
            }
        } finally {
            this.sessionBox.freeSession(session);
        }
        if (this.getExecuteAfter() != null) {
            obj = (T)this.getExecuteAfter().getAfter(this, obj);
        }
        return obj;
    }
    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> cls, Serializable pkValue) {
        if (this.getExecuteBefore() != null) {
            if (this.getExecuteBefore().loadBefore(this, cls, pkValue)) {
                return null;
            }
        }
        T obj = null;
        Session session = this.sessionBox.getSession(false);
        try {
            if (this.logicalizationConditionPropertyName != null && this.logicalizationConditionPropertyName.length() > 0
                && this.logicalizationConditionOperator != null && this.logicalizationConditionOperator.length() > 0) {
                // 如果当前对象已经指定了逻辑化操作相关参数则通过select语法实现load效果，籍此以统一的方式排出逻辑删除了的对象
                ClassMetadata cm = session.getSessionFactory().getClassMetadata(cls);
                String pkName = cm.getIdentifierPropertyName();
                if (pkName != null && pkName.length() > 0) {
                    Logic<T, T> selector = this
                        .select()
                        .from(cls)
                        .where(pkName).is("=", pkValue);
                    selector.setEnableExecuteCallback(false);
                    List<T> objs = selector.list();
                    if (objs != null && objs.size() > 0) {
                        obj = objs.get(0);
                    }
                }
            } else {
                long time = System.currentTimeMillis();
                obj = (T) session.load(cls, pkValue);
                if (Jsql.this.getExecuteCompleted() != null) {
                    Jsql.this.getExecuteCompleted().listCompleted(Jsql.this, null, String.format("load(%s, %s)", cls.getName(), pkValue.toString()), System.currentTimeMillis() - time);
                }
            }
        } finally {
            this.sessionBox.freeSession(session);
        }
        if (this.getExecuteAfter() != null) {
            obj = (T)this.getExecuteAfter().loadAfter(this, obj);
        }
        return obj;
    }
    public <T> void delete(Class<T> cls, Object object) {
        if (this.getExecuteBefore() != null) {
            this.getExecuteBefore().deleteBefore(this, object);
        }
        boolean isOk = false;
        Session session = null;
        try {
            session = this.sessionBox.getSession(true);
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().deleting(this, object);
            }
            if (this.logicalizationConditionPropertyName != null && this.logicalizationConditionPropertyName.length() > 0
                && this.logicalizationConditionOperator != null && this.logicalizationConditionOperator.length() > 0) {
                // 如果当前对象已经指定了逻辑化操作相关参数则通过select语法实现load效果，籍此以统一的方式排出逻辑删除了的对象
                ClassMetadata cm = session.getSessionFactory().getClassMetadata(cls);
                String pkName = cm.getIdentifierPropertyName();
                if (pkName != null && pkName.length() > 0) {
                    Object pkValue = BeanUtil.getBeanProperticeValue(object, pkName);
                    if (pkValue != null) {
                        Logic<T, T> deleter = this
                            .delete(cls)
                            .where(pkName).is("=", pkValue);
                        deleter.setEnableExecuteCallback(false);
                        deleter.executeUpdate();
                        isOk = true;
                    }
                }
            } else {
                session.delete(object);
                isOk = true;
            }
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().deleted(this, object);
            }
        } finally {
            try {
                if (!isOk && this.getExecuteBeanChanged() != null) {
                    this.getExecuteBeanChanged().deleteCanceled(this, object);
                }
            } finally {
                if (session != null) {
                    this.sessionBox.freeSession(session);
                }
            }
        }
    }
    public Serializable save(final Object object) {
        if (this.getExecuteBefore() != null) {
            this.getExecuteBefore().saveBefore(this, object);
        }
        boolean isOk = false;
        Serializable seri = null;
        Session session = null;
        try {
            session = this.sessionBox.getSession(true);
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().saving(this, object);
            }
            seri = session.save(object);
            isOk = true;
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().saved(this, object);
            }
        } finally {
            try {
                if (!isOk && this.getExecuteBeanChanged() != null) {
                    this.getExecuteBeanChanged().saveCanceled(this, object);
                }
            } finally {
                if (session != null) {
                    this.sessionBox.freeSession(session);
                }
            }
        }
        return seri;
    }
    public void update(final Object object) {
        if (this.getExecuteBefore() != null) {
            this.getExecuteBefore().updateBefore(this, object);
        }
        boolean isOk = false;
        Session session = null;
        try {
            session = this.sessionBox.getSession(true);
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().updating(this, object);
            }
            session.update(object);
            isOk = true;
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().updated(this, object);
            }
        } finally {
            try {
                if (!isOk && this.getExecuteBeanChanged() != null) {
                    this.getExecuteBeanChanged().updateCanceled(this, object);
                }
            } finally {
                if (session != null) {
                    this.sessionBox.freeSession(session);
                }
            }
        }
    }
    public void merge(final Object object) {
        if (this.getExecuteBefore() != null) {
            this.getExecuteBefore().mergeBefore(this, object);
        }
        boolean isOk = false;
        Session session = null;
        try {
            session = this.sessionBox.getSession(true);
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().updating(this, object);
            }
            session.merge(object);
            isOk = true;
            if (this.getExecuteBeanChanged() != null) {
                this.getExecuteBeanChanged().updated(this, object);
            }
        } finally {
            try {
                if (!isOk && this.getExecuteBeanChanged() != null) {
                    this.getExecuteBeanChanged().updateCanceled(this, object);
                }
            } finally {
                if (session != null) {
                    this.sessionBox.freeSession(session);
                }
            }
        }
    }
    /**
     * 实现逻辑删除效果的更新属性名称
     */
    private String updatePropertyNameForRemove;
    /**
     * 实现逻辑删除效果的更新属性值
     */
    private Object updateValueForRemove;
    /**
     * 设置实现逻辑删除效果的更新属性
     * @param propName 实现逻辑删除效果的更新属性名称
     * @param propValue 实现逻辑删除效果的更新属性值
     * @return 当前Jsql对象
     */
    public Jsql setRemoveProperty(String propName, Object propValue) {
        if (propName == null) {
            this.updatePropertyNameForRemove = null;
            this.updateValueForRemove = null;
        } else {
            this.updatePropertyNameForRemove = propName;
            this.updateValueForRemove = propValue;
        }
        return this;
    }
    /**
     * 设置逻辑化查询条件
     * @param propName 属性名称
     * @param operator 条件判定的操作符（=、>、<等）
     * @param value 条件判定值
     * @return 当前Jsql对象
     */
    public Jsql setLogicalizationCondition(String propName, String operator, Object value) {
        if (propName == null) {
            this.logicalizationConditionOperator = null;
            this.logicalizationConditionPropertyName = null;
            this.logicalizationConditionValue = null;
        } else {
            this.logicalizationConditionOperator = operator;
            this.logicalizationConditionPropertyName = propName;
            this.logicalizationConditionValue = value;
        }
        return this;
    }
    /**
     * 缺省分页大小<br>
     * 用于在执行查询类操作时，未指定PagingInfo对象或者指定的PagingInfo对象未指定有效的pageSize属性。
     */
    private Integer defaultPageSize;
    /**
     * 缺省分页大小<br>
     * 用于在执行查询类操作时，未指定PagingInfo对象或者指定的PagingInfo对象未指定有效的pageSize属性。
     */
    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }
    /**
     * 缺省分页大小<br>
     * 用于在执行查询类操作时，未指定PagingInfo对象或者指定的PagingInfo对象未指定有效的pageSize属性。
     */
    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }
    /**
     * 全局SystemTimeFactory集<br>
     * Map的key值是数据源名称
     */
    private static Map<String, SystemTimeFactory> systemTimeFactorys = new HashMap<String, SystemTimeFactory>();
    /**
     * 从全局SystemTimeFactory集中安全获取当前数据源的SystemTimeFactory对象
     * @return 当前数据源的SystemTimeFactory对象
     */
    private SystemTimeFactory getSystemTimeFactory() {
        SystemTimeFactory systemTimeFactory = null;
        synchronized (systemTimeFactorys) {
            systemTimeFactory = systemTimeFactorys.get(this.getDataSourceName());
            if (systemTimeFactory == null) {
                systemTimeFactory = new SystemTimeFactory();
                systemTimeFactorys.put(this.getDataSourceName(), systemTimeFactory);
            }
        }
        return systemTimeFactory;
    }
    /**
     * 查询数据库服务器的系统时间<br>
     * 这个方法与selectCurrentTimestamp方法的区别在于，这个方法优先通过内置的SystemTimeFactory获取数据库服务器的系统时间，可以大大减少对数据库的实际访问次数。
     * @return 数据库服务器的系统时间。
     */
    public Date selectSystemTime() {
        SystemTimeFactory systemTimeFactory = this.getSystemTimeFactory();
        if (System.currentTimeMillis() - systemTimeFactory.getLastFixTime() > 1000 * 60 * 60 * 24) {
            // 每隔24小时重新修正一次数据库服务器系统时间与当前物理系统时间的差值
            systemTimeFactory.fix(this.selectCurrentTimestamp());
        }
        return systemTimeFactory.now();
    }
    /**
     * 查询数据库的当前系统时间<br>
     * @param entityName HQL语法需要的目标映射bean的类名
     * @return 如果成功则返回数据库时间，否则返回null。
     */
    public Date selectCurrentTimestamp(String entityName) {
        Session session = this.sessionBox.getSession(false);
        try {
            String hql = String.format("select current_timestamp() as dbtime, count(1) as zc from %s", entityName);
            Query query = session.createQuery(hql);
            query.setMaxResults(1);
            List<?> rs = query.list();
            if (rs != null && rs.size() > 0) {
                Object currentTimestamp = rs.get(0);
                if (currentTimestamp instanceof Date) {
                    return (Date)currentTimestamp;
                } else if (currentTimestamp.getClass().isArray() && ((Object[])currentTimestamp)[0] instanceof Date) {
                    return (Date)((Object[])currentTimestamp)[0];
                }
            }
        } catch (Exception e) {
            if (e instanceof SQLGrammarException && ((SQLGrammarException)e).getSQLException() != null) {
                F.log().dbe(String.format("Jsql以'%s'为目标获取系统时间时出现运行时异常：%s.", entityName, ((SQLGrammarException)e).getSQLException().getMessage()));
            } else {
                F.log().dbe(String.format("Jsql以'%s'为目标获取系统时间时出现运行时异常：%s - %s.", entityName, e.getMessage(), e.getClass().getSimpleName()), e);
            }
        } finally {
            this.sessionBox.freeSession(session);
        }
        return null;
    }
    /**
     * 查询数据库的当前系统时间<br>
     * @return 数据库的当前系统时间。
     */
    public Date selectCurrentTimestamp() {
        String entityName = null;
        Map<String, ClassMetadata> acm = null;
        Session session = this.sessionBox.getSession(false);
        try {
            acm = session.getSessionFactory().getAllClassMetadata();
        } catch (Exception e) {
            F.log().dbe(String.format("Jsql运行时异常：%s - %s.", e.getMessage(), e.getClass().getSimpleName()), e);
        } finally {
            this.sessionBox.freeSession(session);
        }
        if (acm != null && acm.size() > 0) {
            Iterator<String> it = acm.keySet().iterator();
            while (it.hasNext()) {
                entityName = it.next();
                Date currentTimestamp = this.selectCurrentTimestamp(entityName);
                if (currentTimestamp != null) {
                    return currentTimestamp;
                }
            }
        }
        return new Date();
    }
    public Select createSelector() {
        Select select = new Select(this.sessionBox);
        select.setLogicalizationCondition(this.logicalizationConditionPropertyName, this.logicalizationConditionOperator, this.logicalizationConditionValue);
        select.setDefaultPageSize(this.getDefaultPageSize());
        return select;
    }
    /**
     * 等同于"select * "
     * @return From语法对象
     */
    public From select() {
        return this.createSelector().select();
    }
    /**
     * 等同于"select <propName> "
     * @param propType 属性类型，其值决定最终结果集的数据类型
     * @param propName 属性名
     * @return From语法对象
     */
    public <RESULT_CLASS> FromT<RESULT_CLASS> select(Class<RESULT_CLASS> propType, String propName) {
        return this.createSelector().select(propType, propName);
    }
    /**
     * 等同于"select <propName> "
     * @param propType 属性类型，其值决定最终结果集的数据类型
     * @param propName 属性名
     * @param distinct 排重标记
     * @return From语法对象
     */
    public <RESULT_CLASS> FromT<RESULT_CLASS> select(Class<RESULT_CLASS> propType, String propName, boolean distinct) {
        return this.createSelector().select(propType, propName, distinct);
    }
    public Delete createDeleter() {
        Delete delete = new Delete(this.sessionBox, this.updatePropertyNameForRemove, this.updateValueForRemove);
        delete.setLogicalizationCondition(this.logicalizationConditionPropertyName, this.logicalizationConditionOperator, this.logicalizationConditionValue);
        return delete;
    }
    /**
     * delete语法
     * @param targetClass delete语法的目标类型
     * @return 用于后续语法拼接的where语法对象
     */
    public <TARGET_CLASS> Where<TARGET_CLASS, TARGET_CLASS> delete(Class<TARGET_CLASS> targetClass) {
        return this.createDeleter().delete(targetClass);
    }
    /**
     * 支持无条件删除的delete语法
     * @param targetClass delete语法的目标类型
     * @return 用于后续执行的结果对象
     */
    public <TARGET_CLASS> Result<TARGET_CLASS> deleteAll(Class<TARGET_CLASS> targetClass) {
        return this.createDeleter().deleteAll(targetClass);
    }
    public Update createUpdater() {
        Update update = new Update(this.sessionBox);
        update.setLogicalizationCondition(this.logicalizationConditionPropertyName, this.logicalizationConditionOperator, this.logicalizationConditionValue);
        return update;
    }
    /**
     * update语法
     * @param targetClass update语法的目标类型
     * @return 用于后续语法拼接的set语法对象
     */
    public <TARGET_CLASS> Set<TARGET_CLASS> update(Class<TARGET_CLASS> targetClass) {
        return this.createUpdater().update(targetClass);
    }
    /**
     * 支持无条件更新的update语法
     * @param targetClass update语法的目标类型
     * @return 用于后续语法拼接的set语法对象
     */
    public <TARGET_CLASS> Set<TARGET_CLASS> updateAll(Class<TARGET_CLASS> targetClass) {
        return this.createUpdater().updateAll(targetClass);
    }
    /**
     * 创建一个独立的新条件<br>
     * 创建一个独立的新条件可以用来指定嵌套的逻辑关系，例如：propName1=1 and (propName1=2 or propName2='xxx')
     * @param resCls 条件对象适用的Select语法的返回类型
     * @param tagCls 条件对象适用的Select语法的源类型
     * @param propName 条件所指的目标对象属性
     * @return 独立的新条件对象
     */
    public <RESULT_CLASS, TARGET_CLASS> Condition<RESULT_CLASS, TARGET_CLASS> createNewCondition(Class<RESULT_CLASS> resCls, Class<TARGET_CLASS> tagCls, String propName) {
        Logic<RESULT_CLASS, TARGET_CLASS> logic = new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start);
        Condition<RESULT_CLASS, TARGET_CLASS> cond = new Condition<RESULT_CLASS, TARGET_CLASS>(logic, propName);
        logic.setCondition(cond);
        return cond;
    }
    /**
     * 创建一个独立的新条件<br>
     * 创建一个独立的新条件可以用来指定嵌套的逻辑关系，例如：propName1=1 and (propName1=2 or propName2='xxx')
     * @param resCls 条件对象适用的Select语法的返回类型
     * @param tagCls 条件对象适用的Select语法的源类型
     * @param propName 条件所指的目标对象属性
     * @return 独立的新条件对象
     */
    public <RESULT_CLASS, TARGET_CLASS> Condition<RESULT_CLASS, TARGET_CLASS> cnc(Class<RESULT_CLASS> resCls, Class<TARGET_CLASS> tagCls, String propName) {
        return this.createNewCondition(resCls, tagCls, propName);
    }
    /**
     * 创建一个独立的逻辑
     * @param resCls 样本对象适用的Select语法的返回类型
     * @param tagCls 样本对象适用的Select语法的源类型
     * @param examples 样本对象
     * @return 以样本对象为准构建的独立逻辑对象
     */
    public <RESULT_CLASS, TARGET_CLASS> Logic<RESULT_CLASS, TARGET_CLASS> createNewLogic(Class<RESULT_CLASS> resCls, Class<TARGET_CLASS> tagCls, List<TARGET_CLASS> examples) {
        return new Logic<RESULT_CLASS, TARGET_CLASS>(LogicType.Start, examples);
    }
    /**
     * 创建一个独立的逻辑
     * @param resCls 样本对象适用的Select语法的返回类型
     * @param tagCls 样本对象适用的Select语法的源类型
     * @param examples 样本对象
     * @return 以样本对象为准构建的独立逻辑对象
     */
    public <RESULT_CLASS, TARGET_CLASS> Logic<RESULT_CLASS, TARGET_CLASS> cnl(Class<RESULT_CLASS> resCls, Class<TARGET_CLASS> tagCls, List<TARGET_CLASS> examples) {
        return this.createNewLogic(resCls, tagCls, examples);
    }
    /**
     * 创建一个独立的逻辑
     * @param resCls 样本对象适用的Select语法的返回类型
     * @param tagCls 样本对象适用的Select语法的源类型
     * @param example 样本对象
     * @return 以样本对象为准构建的独立逻辑对象
     */
    public <RESULT_CLASS, TARGET_CLASS> Logic<RESULT_CLASS, TARGET_CLASS> cnl(Class<RESULT_CLASS> resCls, Class<TARGET_CLASS> tagCls, TARGET_CLASS example) {
        List<TARGET_CLASS> examples = new ArrayList<TARGET_CLASS>();
        examples.add(example);
        return this.createNewLogic(resCls, tagCls, examples);
    }
    /**
     * 创建一个独立的逻辑
     * @param resCls 样本对象适用的Select语法的返回类型
     * @param tagCls 样本对象适用的Select语法的源类型
     * @param examples 样本对象
     * @return 以样本对象为准构建的独立逻辑对象
     */
    public <RESULT_CLASS, TARGET_CLASS> Logic<RESULT_CLASS, TARGET_CLASS> cnl(Class<RESULT_CLASS> resCls, Class<TARGET_CLASS> tagCls, TARGET_CLASS[] examples) {
        List<TARGET_CLASS> examplelist = new ArrayList<TARGET_CLASS>();
        for (TARGET_CLASS example : examples) {
            examplelist.add(example);
        }
        return this.createNewLogic(resCls, tagCls, examplelist);
    }
    /**
     * 获取当前Jsql对象的事务编号
     * @return 如果当前Jsql开启了事务，则返回一个全局唯一的会话编号，否则返回null。
     */
    public String getTransactionSn() {
        if (this.sessionBox != null) {
            return this.sessionBox.getTransactionSn();
        }
        return null;
    }
    /**
     * 获取当前会话对象在当前事务下的一个递增序列值
     * @return 当前会话对象在当前事务下的一个递增序列值
     */
    public int nextTransactionSequence() {
        if (this.sessionBox != null) {
            return this.sessionBox.nextTransactionSequence();
        }
        return 0;
    }
    private ThreadLocal<Object> runtimeArg;
    /**
     * 获取Jsql对象的运行时参数<br>
     * 运行时参数是由应用层进行设置的，并不影响Jsql对象的行为，Jsql对象只是一个载体。
     * @return 当前Jsql对象的运行时参数
     */
    public Object getRuntimeArg() {
        return this.runtimeArg != null ? this.runtimeArg.get() : null;
    }
    /**
     * 设置Jsql对象的运行时参数<br>
     * 运行时参数是由应用层进行设置的，并不影响Jsql对象的行为，Jsql对象只是一个载体。
     * @param arg 运行时参数对象
     * @return 当前Jsql对象。
     */
    public Jsql setRuntimeArg(Object arg) {
        if (this.runtimeArg == null) {
            this.runtimeArg = new ThreadLocal<Object>();
        }
        this.runtimeArg.set(arg);
        return null;
    }
}
