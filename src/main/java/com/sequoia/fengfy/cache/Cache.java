package com.sequoia.fengfy.cache;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.Future;

/**
 * @ClassName Cache
 * @Description 缓存DTO
 * @Author: FengFuyun
 * @date 2022-04-04 10:12
 * @Version 1.0
 */

public class Cache {

    /**
     * 缓存数据
     */
    private Object data;

    /**
     * 定时器Future
     */
    public Future future;


    public Cache() {
    }

    public Cache(Object data) {
        this.data = data;
    }

    public Cache(Object data, Future future ) {
        this.data = data;
        this.future = future;
    }

    public Object getData(){
        return data;
    }


}
