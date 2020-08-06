package com.qys.training.biz.hazelcast.impl;

import clojure.lang.Obj;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import com.qys.training.biz.hazelcast.CacheService;
import com.qys.training.biz.hazelcast.QYSHazelcastClientConfig;
import com.qys.training.biz.hazelcast.mapper.SysConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:14 2020/8/5
 */
@Service
public class CacheServiceImpl implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    @Autowired
    private HazelcastInstance instance;

    @Autowired
    private QYSHazelcastClientConfig config;

    @Autowired
    private SysConfigMapper mapper;

    @Override
    public void insertCacheMap(String cacheKey, Object key, Object value) {
        logger.info("param in {},  {}  ,{}",cacheKey, key, value);
        final IMap<Object, Object> iMap = this.getIMap(cacheKey);
        iMap.put(key, value, config.expire, TimeUnit.SECONDS);
    }

    @Override
    public Object getValueByKey(String key) {
        final IMap<Object, Object> iMap = this.getIMap(config.sysConfigCacheKey);
        Object o = iMap.get(key);
        if (o == null) {
            o = mapper.getConfigValue(key);
            if (o == null)
                throw new QysException(BizCodeEnum.ERROR.getCode(), BizCodeEnum.ERROR.getDescription());
            iMap.put(key, o, config.expire, TimeUnit.SECONDS);
        }
        return o;
    }


    // 获取imap
    private IMap<Object, Object> getIMap(String cacheKey) {
        if (StringUtils.isEmpty(cacheKey))
            throw new QysException(BizCodeEnum.WRONG_PARAM.getCode(), BizCodeEnum.WRONG_PARAM.getDescription());
        return instance.getMap(cacheKey);
    }
}
