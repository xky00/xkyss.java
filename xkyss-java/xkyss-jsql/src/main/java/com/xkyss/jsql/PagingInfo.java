/**
 *
 */
package com.xkyss.jsql;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>查询类业务的分页信息对象</b><br>
 * 分页信息对象既可以作为查询参数，也用来反馈结果的数量信息。
 * @author rechard
 *
 */
public class PagingInfo {
    private static final long serialVersionUID = 3595896499319855400L;
    /**
     * 查询排序类型枚举定义
     */
    public enum OrderType {
        ASC, // 升序
        DESC, // 降序
        NOORDER // 不排序
    };
    /**
     * 排序条件
     */
    public static class Order {
        /**
         * 排序字段
         */
        private String[] orderPropertyNames;
        /**
         * 排序字段的排序类型
         */
        private OrderType orderType = OrderType.NOORDER;
        public String[] getOrderPropertyNames() {
            return orderPropertyNames;
        }
        public void setOrderPropertyNames(String[] orderPropertyNames) {
            this.orderPropertyNames = orderPropertyNames;
        }
        public OrderType getOrderType() {
            return orderType;
        }
        public void setOrderType(OrderType orderType) {
            this.orderType = orderType;
        }
        public Order() {
        }
        /**
         * @param orderPropertyNames 排序字段
         * @param orderType 排序字段的排序类型
         */
        public Order(String[] orderPropertyNames, OrderType orderType) {
            this.orderPropertyNames = orderPropertyNames;
            this.orderType = orderType;
        }
        /**
         * @param orderPropertyName 排序字段
         * @param orderType 排序字段的排序类型
         */
        public Order(String orderPropertyName, OrderType orderType) {
            this(new String[]{orderPropertyName}, orderType);
        }
        /**
         * @param orderPropertyNames 排序字段，默认增序排序
         */
        public Order(String[] orderPropertyNames) {
            this(orderPropertyNames, OrderType.ASC);
        }
        /**
         * @param orderPropertyName 排序字段，默认增序排序
         */
        public Order(String orderPropertyName) {
            this(orderPropertyName, OrderType.ASC);
        }
    }
    /**
     * 排序参数集
     */
    private List<Order> orders;
    /**
     * 排序类型
     */
    private OrderType orderType = OrderType.NOORDER;
    /**
     * 参与排序的属性名称
     */
    private String[] orderPropertyNames;
    /**
     * 分页大小，用作分页参数
     */
    private Integer pageSize = 100;
    /**
     * 分页页码，从1开始，用作分页参数
     */
    private Integer pageNo = 1;
    /**
     * 结果总数，用来反馈查询结果
     */
    private Integer totalSize;
    /**
     * 结果总页数，用来反馈查询结果
     */
    private Integer totalPageNumber;
    /**
     *
     */
    public PagingInfo() {
    }
    public PagingInfo(Integer pageSize, List<Order> orders) {
        this.pageSize = pageSize;
        this.orders = orders;
    }
    public PagingInfo(Integer pageSize, Order[] orders) {
        this.pageSize = pageSize;
        this.orders = new ArrayList<Order>();
        for (Order order : orders) {
            this.orders.add(order);
        }
    }
    public PagingInfo(Integer pageSize, Order order) {
        this(pageSize, new Order[]{order});
    }
    public PagingInfo(Integer pageSize, String[] orderPropertyNames, OrderType orderType) {
        this(pageSize, new Order(orderPropertyNames, orderType));
    }
    public PagingInfo(Integer pageSize, String[] orderPropertyNames) {
        this(pageSize, new Order(orderPropertyNames));
    }
    public PagingInfo(Integer pageSize, String orderPropertyName, OrderType orderType) {
        this(pageSize, new Order(orderPropertyName, orderType));
    }
    public PagingInfo(Integer pageSize, String orderPropertyName) {
        this(pageSize, new Order(orderPropertyName));
    }

    public PagingInfo(Integer pageSize) {
        this.pageSize = pageSize;
    }
    public PagingInfo(Integer pageSize, Integer pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }
    public PagingInfo(Integer pageSize, OrderType orderType, String[] orderPropertyNames) {
        this.pageSize = pageSize;
        this.orderType = orderType;
        this.orderPropertyNames = orderPropertyNames;
    }
    public PagingInfo(Integer pageSize, Integer pageNo, OrderType orderType, String[] orderPropertyNames) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
        this.orderType = orderType;
        this.orderPropertyNames = orderPropertyNames;
    }
    /**
     * 分页大小，用作分页参数
     * @return 分页大小
     */
    public Integer getPageSize() {
        return pageSize;
    }
    /**
     * 分页大小，用作分页参数
     * @param pageSize 分页大小
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    /**
     * 分页页码，从1开始，用作分页参数
     * @return 分页页码
     */
    public Integer getPageNo() {
        return pageNo;
    }
    /**
     * 分页页码，从1开始，用作分页参数
     * @param pageNo 分页页码
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
    /**
     * 结果总数，用来反馈查询结果
     * @return 结果总数
     */
    public Integer getTotalSize() {
        return totalSize;
    }
    /**
     * 结果总数，用来反馈查询结果
     * @param totalSize 结果总数
     */
    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }
    /**
     * 结果总页数，用来反馈查询结果
     * @return 结果总页数
     */
    public Integer getTotalPageNumber() {
        return totalPageNumber;
    }
    /**
     * 结果总页数，用来反馈查询结果
     * @param totalPageNumber 结果总页数
     */
    public void setTotalPageNumber(Integer totalPageNumber) {
        this.totalPageNumber = totalPageNumber;
    }
    /**
     * 排序类型
     * @return 排序类型
     */
    public OrderType getOrderType() {
        return orderType;
    }
    /**
     * 排序类型
     * @param orderType 排序类型
     */
    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }
    /**
     * 参与排序的属性名称
     * @return 属性名称
     */
    public String[] getOrderPropertyNames() {
        return orderPropertyNames;
    }
    /**
     * 参与排序的属性名称
     * @param orderPropertyNames 属性名称
     */
    public void setOrderPropertyNames(String[] orderPropertyNames) {
        this.orderPropertyNames = orderPropertyNames;
    }
    /**
     * 排序参数集
     * @return 排序参数集
     */
    public List<Order> getOrders() {
        return orders;
    }
    /**
     * 排序参数集
     * @param orders 排序参数集
     */
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    // @Override
    // public String[] importantFieldValues() {
    // 	return new String[]{"" + this.getPageSize(), "" + this.getPageNo(), "" + this.getTotalSize()};
    // }
}
