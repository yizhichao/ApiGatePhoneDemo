package com.hik.apigatephonedemo;


import android.util.Log;

import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangkuilin on 2017/9/14.
 */

public class HttpClient {

    private static final String TAG = "/HttpClient";

    public static void init(String host, String userName, String password) {
                ArtemisConfig.host = "open8200.hikvision.com";
                ArtemisConfig.appKey = "23114629";
                ArtemisConfig.appSecret ="NOxUXbEAgMCL94mOYrOO";
        //        ArtemisConfig.host = "10.33.25.19:9999";
        //        ArtemisConfig.appKey = "28523404";
        //        ArtemisConfig.appSecret ="TDEt4xs6rniO6jlcYlsv";
//        ArtemisConfig.host = host;
//        ArtemisConfig.appKey = userName;
//        ArtemisConfig.appSecret = password;

    }

    public static String doGet(final String strUrl, Map<String, String> querys){
        Map<String, String> path = new HashMap<String, String>(2){
            {
                put("https://", strUrl);
            }
        };
        Log.e(TAG, "path: " + "https://" + ArtemisConfig.host + strUrl);
        Log.e(TAG, "querys: " + querys);
//        String httpSchema = (String)path.keySet().toArray()[0];
//        Log.e("HttpClient","path:"+path.get(httpSchema));
//        Log.e("HttpClient","path:"+httpSchema);
        String str = ArtemisHttpUtil.doGetArtemis(path, querys,null,null);
        Log.e(TAG,"CallBack:"+str);
        return str;
    }

    public static String doPost(Map<String, String> path, Map<String, String> paramMap){
        return "";
//        return ArtemisHttpUtil.doPostFormArtemis(path, paramMap,null,null,null);
    }
}
