package com.sequoia.fengfy.exception;

/**
 * @ClassName CustomException
 * @Description 通用自定义异常
 * @Author: FengFuyun
 * @date 2022-04-04 09:48
 * @Version 1.0
 */
public class CustomException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public CustomException(String message)
    {
        this.message = message;
    }

    public CustomException(Integer code, String message)
    {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public Integer getCode()
    {
        return code;
    }
}