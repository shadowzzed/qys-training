package com.qys.training.biz.hazelcast.entity;

import com.qys.training.base.entity.BaseEntity;

/**
 * @author Zed, shadowl91@163.com
 * @date 15:02 2020/8/5
 */
public class SysConfig extends BaseEntity {
    private String configKey;

    private String configValue;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public SysConfig() {
    }
}
