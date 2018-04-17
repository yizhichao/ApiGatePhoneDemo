/**
 * @Copyright: HangZhou Hikvision System Technology Co., Ltd.
 * All Right Reserved.
 * @address: http://www.hikvision.com
 * @date: 2015-8-7
 * @Description:
 */
package com.hik.apigatephonedemo.utils;

/**
 * FastClickUtil
 * 快速点击判断
 *
 * @author xiadaidai
 * @ date 2015-8-7 version 1.0.0
 */
public class FastClickUtil {
    private static long lastClickTime;

    /**
     * 判断是否点击过快
     *
     * @param interval 指定的间隔时间
     * @return true表示点击过快。false表示否
     */
    public static boolean isFastClick(long interval) {
        if (interval < 0) {
            return false;
        }

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= interval) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
