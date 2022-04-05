package com.sequoia.fengfy.utils;

/**
 * @ClassName ToolUtil
 * @Description 工具类
 * @Author: FengFuyun
 * @date 2022-04-04 11:07
 * @Version 1.0
 */
public class ToolUtil {


    static final char[] DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
                    'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
                    'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 生成62进制字符串
     * @param seq
     * @return
     */
    public static String to62RadixString(long seq) {
        StringBuilder sb = new StringBuilder();
        while (true) {
            int remainder = (int) (seq % 62);
            sb.append(DIGITS[remainder]);
            seq = seq / 62;
            if (seq == 0) {
                break;
            }
        }
        return sb.toString();
    }
}
