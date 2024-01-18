package com.xkyss.quarkus.server.dto;

public class PageInfo {
    /**
     * 总数
     */
    private Integer totalSize;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 当前第几页
     */
    private Integer page;

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
