package com.sequoia.fengfy.controller;

import com.sequoia.fengfy.constant.R;
import com.sequoia.fengfy.service.UrlGenerateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName LinkController
 * @Description 链接服务
 * @Author: FengFuyun
 * @date 2022-04-04 18:51
 * @Version 1.0
 */

@RestController
@Api(tags = "链接服务")
@RequestMapping("/link")
public class LinkController {

    @Autowired
    private UrlGenerateService urlGenerateService;


    @GetMapping("/short")
    @ApiOperation(value = "生成短地址", notes = "根据原生地址信息生成短地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nativeUrl", value = "原生地址串", dataType = "String", required = true, paramType = "query")
    })
    public R generate(String nativeUrl) {
        String shortUrl = urlGenerateService.generateShortUrl(nativeUrl);
        return R.success(shortUrl);
    }


    @GetMapping("/native")
    @ApiOperation(value = "返回原生地址", notes = "根据短地址信息返回原生地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shortUrl", value = "短地址串", dataType = "String", required = true, paramType = "query")
    })
    public R parse(String shortUrl) {
        String nativeUrl = urlGenerateService.parseToNativeUrl(shortUrl);
        return R.success(nativeUrl);
    }
}
