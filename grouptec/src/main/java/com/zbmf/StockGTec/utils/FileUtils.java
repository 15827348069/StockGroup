package com.zbmf.StockGTec.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by iMac on 2017/2/16.
 */

public class FileUtils {

    private static final String DEFAULT_DATA_BASEPATH = "/GROUP_STOCK"; // 缓存目录
    public String DEFAULT_DATA_IMAGEPATH = DEFAULT_DATA_BASEPATH + "/IMAGE";
    public String DEFAULT_DATA_FILE = DEFAULT_DATA_BASEPATH + "/FILE";
    public String DEFAULT_DATA_TEMP = DEFAULT_DATA_BASEPATH + "/TEMP"; // 备份数据地址
    public static FileUtils fileUtils;
    public static FileUtils getIntence(){
        if (fileUtils==null){
            fileUtils=new FileUtils();
        }
        return fileUtils;
    }
    public FileUtils(){
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        DEFAULT_DATA_IMAGEPATH = rootPath + DEFAULT_DATA_IMAGEPATH;
        DEFAULT_DATA_FILE = rootPath + DEFAULT_DATA_FILE;
        DEFAULT_DATA_TEMP = rootPath + DEFAULT_DATA_TEMP;
    }

    public static boolean deletAllCacheFiles() {
        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + DEFAULT_DATA_BASEPATH);
            if (file.exists() && file.isDirectory()) {
                File[] arrayFiles = file.listFiles();
                for (File f : arrayFiles) {
                    File[] arrayImgFile = f.listFiles();
                    for (File Imgfile : arrayImgFile) {
                        Imgfile.delete();
                    }
                    f.delete();
                }
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public  boolean hasSdcard(){
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else{
            return false;
        }
    }
    public  String isCacheFileIsExit(Context context) {
        File file = null;
        String filePath = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filePath = DEFAULT_DATA_IMAGEPATH;
            file = new File(filePath);
        } else {
            file = context.getCacheDir();
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        return filePath;
    }
}
