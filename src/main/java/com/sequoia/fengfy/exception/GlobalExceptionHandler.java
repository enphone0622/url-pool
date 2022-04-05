package com.sequoia.fengfy.exception;

import com.sequoia.fengfy.constant.HttpStatus;
import com.sequoia.fengfy.constant.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @ClassName GlobalExceptionHandler
 * @Description 全局异常处理器
 * @Author: FengFuyun
 * @date 2022-04-04 09:36
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 业务异常
     */
    @ExceptionHandler(CustomException.class)
    public R businessException(CustomException e) {
        if (e.getCode() == null) {
            return R.error(e.getMessage());
        }
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(HttpStatus.NOT_FOUND, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

}
