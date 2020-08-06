package com.qys.training.biz.hazelcast.service;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:07 2020/8/5
 */
public interface CacheService {
    void insertCacheMap(String cacheKey, Object key, Object value);

    Object getValueByKey(String key);

}
