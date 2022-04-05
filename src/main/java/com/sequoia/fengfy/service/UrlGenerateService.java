package com.sequoia.fengfy.service;

/**
 * @ClassName UrlGenerateService
 * @Description 域名生成服务
 * @Author: FengFuyun
 * @date 2022-04-04 09:32
 * @Version 1.0
 */
public interface UrlGenerateService {


    /**
     * 生成短域名
     * @param nativeUrl
     * @return
     */
    String generateShortUrl(String nativeUrl);

    /**
     * 解析为原生域名
     * @param shortUrl
     * @return
     */
    String parseToNativeUrl(String shortUrl);


}
