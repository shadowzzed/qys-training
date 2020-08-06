package com.qys.training.server.controller;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.biz.hazelcast.entity.CacheDTO;
import com.qys.training.biz.hazelcast.service.impl.CacheServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zed, shadowl91@163.com
 * @date 14:32 2020/8/5
 */
@RestController()
@RequestMapping("/cache")
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Autowired
    CacheServiceImpl cacheService;

    @PostMapping("/map")
    @ResponseBody
    public BaseResult insertMap(@RequestBody CacheDTO cacheDTO) {
        cacheService.insertCacheMap(cacheDTO.getCacheKey(), cacheDTO.getKey(), cacheDTO.getValue());
        return BaseResult.success();
    }

    @GetMapping("/value")
    @ResponseBody
    public BaseResult getValue(@RequestParam("configKey") String configKey) {
        logger.info(configKey);
        final Object value = cacheService.getValueByKey(configKey);
        return BaseResult.success(value);
    }
}
