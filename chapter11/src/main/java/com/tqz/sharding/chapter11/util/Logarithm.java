package com.tqz.sharding.chapter11.util;

/**
 * @author <a href="https://github.com/tian-qingzhao">tianqingzhao</a>
 * @since 2024/1/26 16:24
 */
public class Logarithm {

    /**
     * 计算以base为底数的对数值
     *
     * @param base  底数
     * @param value 要计算的值
     * @return 对数值
     */
    public static double log(int base, int value) {
        return Math.log(value) / Math.log(base);
    }

    /**
     * 判断一个数是否是整数
     *
     * @param value 要判断的数
     * @return 如果是整数返回true，否则返回false
     */
    public static boolean isInt(double value) {
        return Math.abs(value - Math.round(value)) < Double.MIN_VALUE;
    }
}
