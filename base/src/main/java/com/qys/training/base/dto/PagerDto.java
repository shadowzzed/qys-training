package com.qys.training.base.dto;

/**
 * 分页查询-主类
 */
public class PagerDto {

    // 当前页数
    private int currentPage;
    // 页面大小
    private int pageSize;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
