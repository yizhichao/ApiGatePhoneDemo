package com.hik.apigatephonedemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * 文件操作工具类
 *
 * @ author xiadaidai
 * @ data 2016/4/20
 */
public class FileUtil {

    private static final String TAG = "FileUtil";

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值

    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值

    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值

    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 获取图片目录
     *
     * @return 图片存储路径
     * @ since V1.0
     */
    public static String getPictureDirPath(Context context) {
        File SDFile = null;
        File SDFolder = null;
        try {

            SDFile = Environment.getExternalStorageDirectory();

            SDFolder = new File(SDFile.getAbsolutePath() + File.separator + "iVMS-8200" + File.separator + "Photo"
                    + File.separator + SharePrefenceUtil.getValue(context,"SP_USER_NAME", ""));
            if ((null != SDFolder) && (!SDFolder.exists())) {
                boolean ret = SDFolder.mkdirs();
                if (ret) {
                    Log.d(TAG, "SDFolder.mkdir success");
                }
                ret = SDFolder.createNewFile();
                if (ret) {
                    Log.d(TAG, "SDFolder.createNewFile success");
                }
            }
            return SDFolder.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取录像目录
     *
     * @return
     * @since V1.0
     */
    public static String getVideoDirPath(Context context) {
        File SDFile = null;
        File SDFolder = null;
        try {

            SDFile = Environment.getExternalStorageDirectory();

            SDFolder = new File(SDFile.getAbsolutePath() + File.separator + "iVMS-8200" + File.separator + "Video"
                + File.separator + SharePrefenceUtil.getValue(context,"SP_USER_NAME", ""));
            if ((null != SDFolder) && (!SDFolder.exists())) {
                boolean ret = SDFolder.mkdirs();
                if (ret) {
                    Log.d(TAG, "SDFolder.mkdir success");
                }
                ret = SDFolder.createNewFile();
                if (ret) {
                    Log.d(TAG, "SDFolder.createNewFile success");
                }
            }
            return SDFolder.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取日志目录
     *
     * @return
     * @since V1.0
     */
    public static String getLogDirPath() {
        File SDFile = null;
        File SDFolder = null;
        try {

            SDFile = Environment.getExternalStorageDirectory();

            SDFolder = new File(SDFile.getAbsolutePath() + File.separator + "iVMS-8200" + File.separator + "Log" +
                    File.separator + "FilePlugin");
            if ((null != SDFolder) && (!SDFolder.exists())) {
                boolean ret = SDFolder.mkdirs();
                if (ret) {
                    Log.d(TAG, "SDFolder.mkdir success");
                }
                ret = SDFolder.createNewFile();
                if (ret) {
                    Log.d(TAG, "SDFolder.createNewFile success");
                }
            }
            return SDFolder.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件名称（监控点名称_年月日时分秒毫秒）
     *
     * @return 文件名称
     * @since V1.0
     */
    public static String getFileName(Calendar calendar, String name) {
        if (null == calendar) {
            return name;
        }
        String fileName = name + "_" +
                String.format("%04d%02d%02d%02d%02d%02d%03d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        calendar.get(Calendar.SECOND),
                        calendar.get(Calendar.MILLISECOND));

        return fileName;
    }

    /**
     * 创建文件夹
     *
     * @param path 文件路径
     * @param Time 当前时间
     * @return 文件夹路径
     * @since V1.0
     */

    public static String createFileDir(String path, Calendar Time) {
        if (null == path || path.equals("")) {
            Log.e(TAG, "createFileDir() fail path is null");
            return "";
        }

        if (Time != null) {
            int year = Time.get(Calendar.YEAR);
            int month = Time.get(Calendar.MONTH) + 1;
            int day = Time.get(Calendar.DAY_OF_MONTH);
            StringBuffer folderName = new StringBuffer();
            folderName.append(year).append(month > 9 ? month : "0" + month).append(day > 9 ? day : "0" + day);
            File tempFile = null;
            try {
                tempFile = new File(path + File.separator + folderName);
                if ((null != tempFile) && (!tempFile.exists())) {
                    boolean ret = tempFile.mkdir();
                    if (!ret) {
                        Log.e(TAG, "createFileDir() fail tempFile makedir fail");
                        return null;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "createFileDir() fail:" + e.toString());
                return "";
            }
            return tempFile.getAbsolutePath();
        } else {
            Log.e(TAG, "createFileDir() fail time  is null");
            return null;
        }
    }

    /**
     * 数据写到SDCard
     *
     * @param dstfile
     * @param picData
     * @param length
     * @since V1.0
     */
    public static boolean writeDataToFile(File dstfile, byte[] picData, int length) {
        if (null == picData || length <= 0) {
            return false;
        }

        if (null == dstfile) {
            return false;
        }

        FileOutputStream fOut = null;
        try {
            if (!dstfile.exists() && !dstfile.createNewFile()) {
                return false;
            }
            fOut = new FileOutputStream(dstfile);
            fOut.write(picData, 0, length);
            fOut.flush();
            fOut.close();
            fOut = null;
            Thread.sleep(50);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {

            picData = null;
        }

        return true;
    }

    /**
     * 获取抓拍水印位图
     *
     * @param bitmapByteArray  源位图
     * @param bitmapDataLength 源位图缓存大小
     * @param bitmapSize       源位图的大小
     * @return 加水印后的位图
     * @since V1.0
     */
    public static Bitmap getWatermarkBitmap(int width, int height, byte[] bitmapByteArray, int bitmapDataLength,
                                            int bitmapSize) {
        if (bitmapByteArray == null || bitmapByteArray.length <= 0) {
            return null;
        }

        BitmapFactory.Options tempOptions = new BitmapFactory.Options();
        tempOptions.inSampleSize = (int) Math.abs(Math.sqrt((double) bitmapSize / (height * width * 3.0)) - 0.5);

        Bitmap bmp = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapDataLength, tempOptions);
        Bitmap scaleBmp = Bitmap.createScaledBitmap(bmp, width, height, true);
        if (bmp != null && !bmp.isRecycled()) {
            bmp.recycle();
        }
        bmp = null;
        if (null == scaleBmp) {
            return null;
        }

        Bitmap newbmp = Bitmap.createBitmap(width + 0, height + 0, Bitmap.Config.ARGB_4444);
        if (newbmp == null) {
            if (scaleBmp != null && !scaleBmp.isRecycled()) {
                scaleBmp.recycle();
            }
            scaleBmp = null;
            return null;
        }

        Canvas cv = new Canvas(newbmp);
        cv.drawColor(Color.WHITE);
        cv.drawBitmap(scaleBmp, 0, 0, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newbmp;
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @since V1.0
     */
    public static boolean removeFile(File file) {
        if (null == file || (!file.exists())) {
            return false;
        }

        try {
            if (file.delete()) {
                file = null;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 按照文件路径删除文件
     *
     * @param filePath 文件路径
     * @return 是否删除成功
     * @author xiadaidai
     * @date 2016/4/28 16:00
     */
    public static boolean deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return removeFile(file);
    }

    /**
     * 删除文件夹
     *
     * @param file 文件
     * @since V1.0
     */
    private static boolean removeDir(File file) {
        if (null == file || (!file.exists())) {
            return false;
        }

        try {
            if (file.delete()) {
                file = null;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 按照文件夹路径删除文件夹
     *
     * @param dirPath 文件夹路径
     * @return 是否删除成功
     * @author xiadaidai
     * @date 2016/4/28 16:00
     */
    private static boolean deleteDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }

        File file = new File(dirPath);
        return removeDir(file);
    }

    /**
     * 递归删除文件夹
     *
     * @param file 要删除的根目录
     */
    public static boolean recursionDeleteDir(File file) {
        if (file.isFile()) {
            return removeFile(file);
        }

        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                return removeDir(file);
            }
            for (File f : childFile) {
                recursionDeleteDir(f);
            }
            return removeDir(file);
        }
        return true;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     * @author xiadaidai
     * @date 2016/5/4 15:21
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取文件大小失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     * @author xiadaidai
     * @date 2016/5/4 15:22
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "获取文件大小失败!");
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     * @author xiadaidai
     * @date 2016/5/4 15:22
     */
    private static long getFileSize(File file){
        long size = 0;
        FileChannel fc = null;
        FileInputStream fis = null;
        try{
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                fc = fis.getChannel();
                size = fc.size();
            }else {
//            boolean ret = file.createNewFile();
                Log.e(TAG, "获取文件大小失败，文件不存在!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if( null != fc){
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹大小
     *
     * @param f
     * @return
     * @throws Exception
     * @author xiadaidai
     * @date 2016/5/4 15:22
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        if( null == flist){
            return 0;
        }
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     * @author xiadaidai
     * @date 2016/5/4 15:25
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return
     * @author xiadaidai
     * @date 2016/5/4 15:25
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
}
