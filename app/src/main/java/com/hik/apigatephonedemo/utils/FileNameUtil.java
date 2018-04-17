package com.hik.apigatephonedemo.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

//import com.hik.mcrsdk.util.CLog;

/**
 * 作者：xieyajuan on 2016/8/25 19:49
 * 邮箱：xieyajuan@hikvision.com
 */

public class FileNameUtil {
    private static final String TAG = FileNameUtil.class.getSimpleName();
    /**
    * @function  isNameConfToRl
    * @Description 判断图片或视频名称是否符合命名规范
    * @author xieyajuan
    * @date 2016/8/25 20:02
    * @param
    * @return
    */
    public static boolean isNameConfToRl(String resourcesName){
        //首先判断名称时候包含下划线，不包含，不符合命名规则
        int separatorIndex = resourcesName.lastIndexOf("_");
        if(separatorIndex < 0){
            return false;
        }
        //其次判断下划线后的字符串长度是否是17位，不是，不符合命名规则
        String lastName = resourcesName.substring(separatorIndex + 1, resourcesName.length());
        if(TextUtils.isEmpty(lastName) || lastName.length() != 17){
            return false;
        }
        //最后判断下划线后的17为字符是否全是数字，不是，不符合命名规则
        return isNumeric(lastName);
    }

    /**
    * @function  isNumeric
    * @Description 判断字符串是否全是数字
    * @author xieyajuan
    * @date 2016/8/25 20:02
    * @param
    * @return
    */
    public static boolean isNumeric(String str){
        if( TextUtils.isEmpty(str)){
            return false;
        }
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
    * @function  isValidDate
    * @Description 判断字符串是否是合法日期,年份必须大于等于1970
    * @author xieyajuan
    * @date 2016/8/26 11:18
    * @param
    * @return
    */
    public static boolean isValidDate(String inDate){
        if(inDate == null){
            return false;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        if(inDate.trim().length() != dateFormat.toPattern().length()){
            return false;
        }
        int year = Integer.parseInt(inDate.substring(0,4));
        //CLog.d(TAG, "int year = " + year);
        if( year < 1970){
            return false;
        }
        dateFormat.setLenient(false);
        try{
            dateFormat.parse(inDate.trim());
        }catch (ParseException e){
            return false;
        }
        return true;
    }
}
