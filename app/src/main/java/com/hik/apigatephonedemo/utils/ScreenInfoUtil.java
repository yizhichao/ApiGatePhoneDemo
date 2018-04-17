/* 
 * @ProjectName iVMS-5060_V3.0
 * @Copyright HangZhou Hikvision System Technology Co.,Ltd. All Right Reserved
 * 
 * @FileName ScreenInfoUtil.java
 * @Description 这里对文件进行描述
 * 
 * @author mlianghua
 * @data Jul 13, 2012
 * 
 * @note 这里写本文件的详细功能描述和注释
 * @note 历史记录
 * 
 * @warning 这里写本文件的相关警告
 */
package com.hik.apigatephonedemo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * 在此对类做相应的描述
 *
 * @author mlianghua
 * @Data Jul 13, 2012
 */
public class ScreenInfoUtil {
    private static String TAG = "ScreenInfoUtil";
    private static float mScreenDensity = 0;
    private static float mScreenHight = 0;
    private static float mScreenWidth = 0;
    private static int mStatusBarHeight;

    private ScreenInfoUtil() {
    }

    private static void initialize(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        if(wm!=null) {
            Display display = wm.getDefaultDisplay();
            if (display != null) {
                display.getMetrics(dm);
            }
            Rect frame = new Rect();
            mScreenDensity = dm.density;
            mScreenHight = dm.heightPixels;
            mScreenWidth = dm.widthPixels;
            if(context instanceof Activity){
                ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            }
            mStatusBarHeight = frame.top;
        }

    }

    /**
     * 这里对方法做描述
     *
     * @param context 上下文
     * @return screen density
     * @since V1.0
     */
    public static float getScreenDensity(Context context) {
        if (0 == mScreenDensity) {
            initialize(context);
        }

        return mScreenDensity;
    }

    /**
     * 这里对方法做描述
     *
     * @param context 上下文
     * @return screen hight
     * @since V1.0
     */
    public static float getScreenHight(Context context) {
        if (0 == mScreenHight) {
            initialize(context);
        }

        return mScreenHight;
    }

    /**
     * 这里对方法做描述
     *
     * @param context 上下文
     * @return screen width
     * @since V1.0
     */
    public static float getScreenWidth(Context context) {
        if (0 == mScreenWidth) {
            initialize(context);
        }

        return mScreenWidth;
    }

    /**
     * 这里对方法做描述
     *
     * @param context 上下文
     * @return screen hight
     * @since V1.0
     */
    public static float getStatusBarHight(Context context) {
        if (0 == mStatusBarHeight) {
            initialize(context);
        }

        return mStatusBarHeight;
    }

    /**
     * @ Description:将px值转换为dip或dp值，保证尺寸大小不变
     * @ author ZJ
     * @ date 2016/3/27 17:17
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @ Description:将dip或dp值转换为px值，保证尺寸大小不变
     * @ author ZJ
     * @ date 2016/3/27 17:17
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * @ Description:将px值转换为sp值，保证文字大小不变
     * @ author ZJ
     * @ date 2016/3/27 17:17
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * @ Description:将sp值转换为px值，保证文字大小不变
     * @ author ZJ
     * @ date 2016/3/27 17:17
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
