package com.hik.apigatephonedemo.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.Calendar;

/**
 * SDCard工具类
 *
 * @author xiadaidai
 * @Data 2016-4-14
 */
public class SDCardUtil {
    /**
     * 256MB ,单位为MB
     */
    public static final long SDCARD_SMALLEST_CAPACITY = 256;

    private static final String TAG = "SDCardUtil";

    /**
     * 获取SDCard的状态
     *
     * @return SDCard 可用的状态
     */
    public synchronized static boolean isSDCardUsable() {
        boolean SDCardMounted = false;
        String sDStateString = Environment.getExternalStorageState();
        if (sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            SDCardMounted = true;
        }

        // 是否正在检测SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)
                || Environment.getExternalStorageState().equals(Environment.MEDIA_NOFS)) {
            SDCardMounted = false;
        }

        // 检测是否插有SD卡
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_REMOVED)
                || Environment.getExternalStorageState().equals(Environment.MEDIA_UNMOUNTED)) {
            SDCardMounted = false;
        }

        // 检测SD卡是否连接电脑共享
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED)) {
            SDCardMounted = false;
        }

        return SDCardMounted;
    }

    /**
     * 获取SDCard剩下的大小 返回单位为MB
     *
     * @return SDCard剩下的大小
     * @since V1.0
     */
    @SuppressWarnings("description")
    public synchronized static long getSDCardRemainSize() {
        StatFs statfs = new StatFs(Environment.getExternalStorageDirectory().getPath());

//        long blockSize;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
//            blockSize = statfs.getBlockSizeLong();
//        }else{
//            blockSize = statfs.getBlockSize();
//        }
//
//        long availableBlocks;
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
//            availableBlocks = statfs.getAvailableBlocksLong();
//        }else{
//            availableBlocks = statfs.getAvailableBlocks();
//        }
        long blockSize = statfs.getBlockSize();
        long availableBlocks = statfs.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * SD卡剩余空间 返回单位为MB
     *
     * @return
     * @since V1.0
     */
    public synchronized static long getSDFreeSize() {

        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();

//        long blockSize;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
//            blockSize = sf.getBlockSizeLong();
//        }else{
//            blockSize = sf.getBlockSize();
//        }

        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();

//        long freeBlocks;
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
//            freeBlocks = sf.getAvailableBlocksLong();
//        }else{
//            freeBlocks = sf.getAvailableBlocks();
//        }
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * SD卡总容量 返回单位为MB
     *
     * @return
     * @since V1.0
     */
    public synchronized static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();

//        long blockSize;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
//            blockSize = sf.getBlockSizeLong();
//        }else{
//            blockSize = sf.getBlockSize();
//        }

        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();

//        long allBlocks;
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR2){
//            allBlocks = sf.getBlockCountLong();
//        }else{
//            allBlocks = sf.getBlockCount();
//        }

        // 返回SD卡大小
        // return allBlocks * blockSize; //单位Byte
        // return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 创建文件夹
     *
     * @param path 文件路径
     * @param time 当前时间
     * @return 文件夹路径
     * @since V1.0
     */

    public synchronized static String createFileDir(String path, Calendar time) {
        if (null == path || path.equals("")) {
            return "";
        }

        int year = time.get(Calendar.YEAR);
        int month = time.get(Calendar.MONTH) + 1;
        int day = time.get(Calendar.DAY_OF_MONTH);

        File tempFile = null;

        tempFile = new File(path + File.separator + year);
        if ((!tempFile.exists())) {
            try {
                boolean ret = tempFile.mkdir();
                if (!ret) {
                    return "";
                }
                ret = tempFile.createNewFile();
                if (!ret) {
                    return "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        try {
            tempFile = new File(tempFile.getAbsolutePath() + File.separator + (month > 9 ? month : "0" + month));
            if ((!tempFile.exists())) {
                boolean ret = tempFile.mkdir();
                if (!ret) {
                    return "";
                }
                ret = tempFile.createNewFile();
                if (!ret) {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        try {
            tempFile = new File(tempFile.getAbsolutePath() + File.separator + (day > 9 ? day : "0" + day));
            if ((!tempFile.exists())) {
                boolean ret = tempFile.mkdir();
                if (!ret) {
                    return "";
                }
                ret = tempFile.createNewFile();
                if (!ret) {
                    return "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        return tempFile.getAbsolutePath();
    }

    /**
     * SDCard是否小于最低内存
     *
     * @return
     * @since V1.0
     */
    public synchronized static boolean checkSDCardSizeUsable() {
        long size = SDCardUtil.getSDFreeSize();
        if (size < SDCARD_SMALLEST_CAPACITY) {
            return false;
        }
        return true;
    }

    /**
     * @ Description:检测SD卡是否可用
     * @ author ZJ
     * @ date 2016/4/15 10:33
     */
    public static boolean checkSDCardIsAvailable() {
        boolean ret = isSDCardUsable();
        if (!ret) {
            return false;
        }

        long size = getSDFreeSize();
        if (size < SDCARD_SMALLEST_CAPACITY) {
            return false;
        }
        return true;
    }

}
