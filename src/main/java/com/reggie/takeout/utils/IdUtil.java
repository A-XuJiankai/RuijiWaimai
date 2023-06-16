package com.reggie.takeout.utils;


import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Description ID生成
 * @date 2020/11/27 4:11 下午
 */
@Component
public class IdUtil {

    /** 分布式id序列号 */
    private static String INDEX="1";
    /** 当前时间戳 */
    private static long currentTimeMillis;
    /** 毫秒序列当前值 */
    private static int sequence = 1;
    /** 毫秒序列最小值 */
    private static int sequenceMin = 100;
    /** 毫秒序列最大值 */
    private static int sequenceMax = 999;



    /**
     * 获取19位id
     * @return
     */
    public static String getId(){
        return new StringBuilder(INDEX).append(System.currentTimeMillis()).append(getIdPerfix()).toString();
    }

    /**
     * 获取指定位数的id，最小19位
     * @param length 位数
     * @return
     */
    public static String getIdByLength(int length){
        return StringUtils.rightPad(new StringBuilder(INDEX).append(System.currentTimeMillis()).append(getIdPerfix()).toString(), length, "0");
    }

    /**
     * 获取序列
     * @return
     */
    private static synchronized long getIdPerfix(){
        if (System.currentTimeMillis() == currentTimeMillis){
            if (sequence > sequenceMax){
                return getIdPerfix();
            }
        }else {
            currentTimeMillis = System.currentTimeMillis();
            sequence = sequenceMin;
        }
        return sequence++;
    }

}

