package com.qys.training.biz.hazelcast.entity;

import java.io.Serializable;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:44 2020/8/5
 */
public class CacheDTO {
    private String cacheKey;

    private String key;

    private String value;

    public CacheDTO() {
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
