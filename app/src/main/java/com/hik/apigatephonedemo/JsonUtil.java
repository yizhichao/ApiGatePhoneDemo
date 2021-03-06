package com.hik.apigatephonedemo;

import android.text.TextUtils;
import android.util.Log;

import com.hik.apigatephonedemo.bean.CameraInfo;
import com.hik.apigatephonedemo.bean.ControlUnit;
import com.hik.apigatephonedemo.bean.RecordingInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkuilin on 2017/9/22.
 */

public class JsonUtil {

    private static String TAG = "JsonUtil";
    private static JsonUtil instance;

    public static JsonUtil getInstance(){
        if (null == instance){
            instance = new JsonUtil();
        }

        return instance;
    }

    /***
     *  解析区域和组织
     * @param strJson
     {
    "code": "200",
    -"data": [
    -{
    "createTime": 1492668561000,
    "indexCode": "14",
    "name": "演示设备",
    "parentIndexCode": "0",
    "parentTree": "0",
    "unitLevel": 1,
    "unitType": 1,
    "updateTime": 1506043063826
    },
    -{
    "createTime": 1492671861167,
    "indexCode": "71",
    "name": "科澜项目",
    "parentIndexCode": "0",
    "parentTree": "0",
    "unitLevel": 1,
    "unitType": 1,
    "updateTime": 1506043063826
    },
    -{
    "createTime": 1379320437000,
    "description": "系统初始化时创建",
    "indexCode": "0",
    "name": "主控制中心",
    "parentIndexCode": "0",
    "parentTree": "0",
    "unitLevel": 0,
    "unitType": 1,
    "updateTime": 1506043063826
    }
    ],
    "msg": "成功"
    }
     *
     * @return
     */
    public List<ControlUnit> parseControlUnit(String strJson){
        if (null == strJson || "".equals(strJson)){
            return null;
        }

        List<ControlUnit> list = new ArrayList<ControlUnit>();

        try{
            JSONObject jsonObject = new JSONObject(strJson);

            if (!"200".equals(jsonObject.getString("code"))){
                return null;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i=0; i<jsonArray.length(); i++){
                ControlUnit temp = parseControlUnit(jsonArray.getJSONObject(i));
                if (null != temp){
                    // 主控中心
                    if (null != temp.getIndexCode() && temp.getIndexCode().equals("0")){
                        list.add(0, temp);
                        continue;
                    }
                    list.add(temp);
                }
            }

            if (list.size() > 0){
                return list;
            }
            return null;

        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public List<RecordingInfo> parseRecordingInfo(String strJson){
        if (TextUtils.isEmpty(strJson)){
            return null;
        }
        List<RecordingInfo> list = new ArrayList<RecordingInfo>();
        try{
            JSONObject jsonObject = new JSONObject(strJson);
            if (!"200".equals(jsonObject.getString("code"))){
                return null;
            }

            JSONObject jsonObject1 =  jsonObject.getJSONObject("data");
            if (0 ==(jsonObject1.getInt("total"))){
                return null;
            }

            JSONArray jsonArray = jsonObject1.getJSONArray("list");
            for (int i=0; i<jsonArray.length(); i++){
                RecordingInfo temp = parseRecordingInfo(jsonArray.getJSONObject(i));
                if (null != temp){
                    list.add(temp);
                }
            }

            if (list.size() > 0){
                return list;
            }
            return null;

        }catch (JSONException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /***
     * 解析区域和组织
     * @param jsonObject
     {
    "createTime": 1492668561000,
    "indexCode": "14",
    "name": "演示设备",
    "parentIndexCode": "0",
    "parentTree": "0",
    "unitLevel": 1,
    "unitType": 1,
    "updateTime": 1506043063826
    }
     * @return
     */
    private ControlUnit parseControlUnit(JSONObject jsonObject){
        if (null == jsonObject){
            return null;
        }

        try {
            ControlUnit controlUnit = new ControlUnit();
            controlUnit.setCreateTime(jsonObject.getLong("createTime"));
            controlUnit.setIndexCode(jsonObject.getString("indexCode"));
            controlUnit.setName(jsonObject.getString("name"));
            controlUnit.setParentIndexCode(jsonObject.getString("parentIndexCode"));
            controlUnit.setParentTree(jsonObject.getString("parentTree"));
            controlUnit.setUnitLevel(jsonObject.getInt("unitLevel"));
            controlUnit.setUnitType(jsonObject.getInt("unitType"));
            controlUnit.setUpdateTime(jsonObject.getLong("updateTime"));
            /** 附加项 */
            controlUnit.setExpend(false);

            return controlUnit;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private RecordingInfo parseRecordingInfo(JSONObject jsonObject){
        if (null == jsonObject){
            return null;
        }
        try {
            RecordingInfo recordingInfo = new RecordingInfo();
            recordingInfo.setBeginTime(jsonObject.getLong("beginTime"));
            recordingInfo.setEndTime(jsonObject.getLong("endTime"));
            recordingInfo.setPlaybackUrl(jsonObject.getString("playbackUrl"));
            return recordingInfo;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析监控点信息
     * @param strJson
     * @return
     */
    public List<ControlUnit> parseCameraInfo(String strJson, ControlUnit parentControlUnit){
        if (null == strJson || "".equals(strJson)){
            return null;
        }

        List<ControlUnit> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(strJson);

            if (!"200".equals(jsonObject.getString("code"))){
                return null;
            }

            JSONObject page = (JSONObject) jsonObject.get("page");
            int total = page.getInt("total");
            if (total> 0) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < total; i++) {
                    CameraInfo cameraInfo = parseCameraInfo(jsonArray.getJSONObject(i));
                    if (null != cameraInfo) {
                        ControlUnit controlUnit = convertCameraInfo2ControlUnit(cameraInfo, parentControlUnit);
                        list.add(controlUnit);
                    }
                }
                if (list.size() > 0){
                    return list;
                }
            }
            return list;

        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    private CameraInfo parseCameraInfo(JSONObject jsonObject){
        if (null == jsonObject){
            return null;
        }

        try {
            CameraInfo cameraInfo = new CameraInfo();
            cameraInfo.setCameraId(jsonObject.getString("cameraId"));
            cameraInfo.setCameraType(jsonObject.getInt("cameraType"));
            cameraInfo.setCreateTime(jsonObject.getInt("createTime"));
            // ...
            cameraInfo.setIndexCode(jsonObject.getString("indexCode"));
            cameraInfo.setName(jsonObject.getString("name"));
            cameraInfo.setParentIndexCode(jsonObject.getString("parentIndexCode"));

            return cameraInfo;

        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;
    }

    private ControlUnit convertCameraInfo2ControlUnit(CameraInfo cameraInfo, ControlUnit parentControlUnit){
        ControlUnit controlUnit = new ControlUnit();
        controlUnit.setCreateTime(cameraInfo.getCreateTime());
        controlUnit.setUpdateTime(cameraInfo.getUpdateTime());
        controlUnit.setIndexCode(cameraInfo.getIndexCode());
        controlUnit.setName(cameraInfo.getName());
        controlUnit.setParentIndexCode(cameraInfo.getParentIndexCode());
        controlUnit.setParentTree(cameraInfo.getParentIndexCode());
        controlUnit.setUnitType(ControlUnit.UNIT_TYPE_CAMERAINFO);
//        controlUnit.setUnitLevel(parentControlUnit.getUnitLevel()+1);
        controlUnit.setCameraInfo(cameraInfo);
        /** 附加项 */
        controlUnit.setExpend(false);

        return controlUnit;
    }

    public String parseHls(String strJson){
        if (null == strJson || "".equals(strJson)){
            return null;
        }

        try{
            JSONObject jsonObject = new JSONObject(strJson);
            if (!"0".equals(jsonObject.getString("code"))){
                Log.e(TAG, "获取HLS失败： " + strJson);
                return strJson;
            }

            return jsonObject.getString("data");

        }catch (JSONException e){
            e.printStackTrace();
        }

        return  null;
    }

    public String parsePTZBack(String strJson){
        if (null == strJson || "".equals(strJson)){
            return null;
        }
        try{
            JSONObject jsonObject = new JSONObject(strJson);
            if (!"200".equals(jsonObject.getString("code"))){
                return jsonObject.getString("msg");
            } else if ("200".equals(jsonObject.getString("code"))) {
                return jsonObject.getString("msg");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  null;
    }

    public String parsePlayBackUrl(String strJson){
        if (null == strJson || "".equals(strJson)){
            return null;
        }
        try{
            JSONObject jsonObject = new JSONObject(strJson);
            if (!"200".equals(jsonObject.getString("code"))){
                return jsonObject.getString("msg");
            } else if ("200".equals(jsonObject.getString("code"))) {
                return jsonObject.getString("playbackUrl");
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        return  null;
    }
}
