package com.hik.apigatephonedemo.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @ author hwf
 * create at 2016/3/17 19:07
 */
public class ToastUtil {
    private static Toast mToast = null;


    /**
     * 弱提示方法
     *
     * @param str 提示的内容
     * @since V1.0
     */
    public static void showToast(Context context, String str) {
        if (str == null) {
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }
}
