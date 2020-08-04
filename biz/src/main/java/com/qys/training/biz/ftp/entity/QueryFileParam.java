package com.qys.training.biz.ftp.entity;

import com.qys.training.base.dto.PagerDto;

/**
 * @author Zed, shadowl91@163.com
 * @date 17:01 2020/8/4
 */
public class QueryFileParam extends PagerDto {
    private String fileName;

    private Long startSize;

    private Long endSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getStartSize() {
        return startSize;
    }

    public void setStartSize(Long startSize) {
        this.startSize = startSize;
    }

    public Long getEndSize() {
        return endSize;
    }

    public void setEndSize(Long endSize) {
        this.endSize = endSize;
    }
}
