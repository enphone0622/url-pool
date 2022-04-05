package com.sequoia.fengfy.constant;


import java.util.HashMap;

/**
 * @ClassName R
 * @Description 统一对外返回结果象
 * @Author: FengFuyun
 * @date 2022-04-04 09:38
 * @Version 1.0
 */
public class R extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";
    
    /** 扩展对象*/
    public static final String EXTRA_TAG  = "extra";

    /**
     * 初始化一个新创建的 Result 对象，使其表示一个空消息。
     */
    public R()
    {
    }


    /**
     * 初始化一个新创建的 Result 对象
     *
     * @param code 状态码
     * @param msg 返回内容
     * @param data 数据对象
     * @param extra 数据对象
     */
    public R(int code, String msg, Object data, Object extra)
    {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (data != null)
        {
            super.put(DATA_TAG, data);
        }

        if (extra != null) {
        	super.put(EXTRA_TAG, extra);
        }
    }


    /**
     * 返回成功数据
     * 
     * @return 成功消息
     */
    public static R success(Object data)
    {
        return R.success("succeed", data);
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static R success(Object data, Object extra)
    {
        return R.success("succeed", data, extra);
    }

    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static R success(String msg, Object data)
    {
        return new R(HttpStatus.SUCCESS, msg, data, null);
    }
    
    /**
     * 返回成功消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static R success(String msg, Object data, Object extra)
    {
        return new R(HttpStatus.SUCCESS, msg, data, extra);
    }
    

    /**
     * 返回错误消息
     * 
     * @return
     */
    public static R error()
    {
        return R.error("failure");
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @return 警告消息
     */
    public static R error(String msg)
    {
        return R.error(msg, null);
    }

    /**
     * 返回错误消息
     * 
     * @param msg 返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static R error(String msg, Object data)
    {
        return new R(HttpStatus.ERROR, msg, data, null);
    }

    /**
     * 返回错误消息
     * 
     * @param code 状态码
     * @param msg 返回内容
     * @return 警告消息
     */
    public static R error(int code, String msg)
    {
        return new R(code, msg, null, null);
    }
}
