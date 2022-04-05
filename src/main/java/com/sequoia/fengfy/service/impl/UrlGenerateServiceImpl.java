package com.sequoia.fengfy.service.impl;

import com.google.common.hash.Hashing;
import com.sequoia.fengfy.cache.CacheContainer;
import com.sequoia.fengfy.constant.HttpStatus;
import com.sequoia.fengfy.exception.CustomException;
import com.sequoia.fengfy.service.UrlGenerateService;
import com.sequoia.fengfy.utils.ToolUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @ClassName UrlGenerateServiceImpl
 * @Description 域名生成服务实现
 * @Author: FengFuyun
 * @date 2022-04-04 09:36
 * @Version 1.0
 */

@Service
public class UrlGenerateServiceImpl implements UrlGenerateService {


    @Value("${domain}")
    private String domain;

    /**
     * 超时时间，默认1天
     */
    @Value("${expire}")
    private Long expire;

    @Autowired
    private CacheContainer cacheContainer;


    @Override
    public String generateShortUrl(String nativeUrl) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (!urlValidator.isValid(nativeUrl)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "url不正确");
        }

        //生成短域名
        long seq = Hashing.murmur3_32().hashUnencodedChars(nativeUrl).padToLong();
        String code = ToolUtil.to62RadixString(seq);

        //将短域名存储在缓存中
        String shortUrl = domain.concat("/").concat(code);
        Object data = cacheContainer.put(shortUrl, nativeUrl, expire);
        if (data == null) {
            throw new CustomException("当前任务忙，请稍后再试");
        }
        return shortUrl;
    }

    @Override
    public String parseToNativeUrl(String shortUrl) {
        String[] schemes = {"http", "https"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (!urlValidator.isValid(shortUrl)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "短地址不正确");
        }

        String nativeUrl = (String) cacheContainer.getData(shortUrl);
        if (StringUtils.isEmpty(nativeUrl)) {
            throw new CustomException("您访问的地址不存在");
        }

        return nativeUrl;
    }
}
