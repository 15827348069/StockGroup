package com.zbmf.StockGTec.utils;


import com.zbmf.StockGTec.GroupApplication;
import com.zbmf.StockGTec.beans.General;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 缓存管理
 * Created by lulu on 2016/7/28.
 */

public class ObjectUtil {

    private static final long CACHE_TIME = 60 * 60 * 1000;

    /**
     * 序列化至本地
     *
     * @param general
     * @param fileName
     * @return
     */
    public static boolean saveObj(General general, String fileName) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;

        try {
            fos = GroupApplication.getInstance().openFileOutput(fileName, GroupApplication.getInstance().MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(general);
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 从本地读取序列化对象
     *
     * @param fileName
     * @return
     */
    public synchronized static General readCache(String fileName) {
        General general = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = GroupApplication.getInstance().openFileInput(fileName);
            ois = new ObjectInputStream(fis);
            general = (General) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null && fis != null) {
                    ois.close();
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return general;
    }

    public static boolean isCacheDataFailure(String cachefile) {
        boolean failure = false;
        File data = GroupApplication.getInstance().getFileStreamPath(cachefile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }


}