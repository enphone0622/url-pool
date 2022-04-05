package com.sequoia.fengfy.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName CacheContainer
 * @Description 缓存容器
 * @Author: FengFuyun
 * @date 2022-04-04 15:41
 * @Version 1.0
 */
@Component
public class CacheContainer {

    /**
     * 缓存
     */
    private static Map<String, Cache> caches = new HashMap<>();

    /**
     * 定时器线程池，用于清除过期缓存
     */
    private final static ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10);

    /**
     * 容量，默认为1000
     */
    @Value("${capacity}")
    private Integer capacity;

    /**
     * 存入缓存
     *
     * @param key
     * @param cache
     */
    public Object put(String key, Cache cache) {
        synchronized (caches) {
            if (capacity == null || capacity.intValue() == 0 || caches.size() < capacity) {
                caches.put(key, cache);
                return cache.getData();
            }
            return null;
        }
    }

    /**
     * 存入缓存
     *
     * @param key
     * @param data
     * @param expire
     */
    public Object put(String key, Object data, long expire) {
        expire = expire > 0 ? expire : 0L;
        return put(key, new Cache(data, executor.schedule(()->{
            synchronized (caches) {
                caches.remove(key);
            }
        }, expire, TimeUnit.MILLISECONDS)));
    }

    /**
     * 获取对应缓存
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        if (isContains(key)) {
            return caches.get(key);
        }
        return null;
    }

    /**
     * 获取对应缓存
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        if (isContains(key)) {
            return caches.get(key).getData();
        }
        return null;
    }

    /**
     * 判断是否在缓存中
     *
     * @param key
     * @return
     */
    public boolean isContains(String key) {
        return caches.containsKey(key);
    }


    /**
     * 缓存数量
     *
     * @return
     */
    public int size() {
        return caches.size();
    }

}
