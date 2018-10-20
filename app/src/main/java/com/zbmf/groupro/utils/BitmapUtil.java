package com.zbmf.groupro.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by xuhao on 2016/6/20.
 */
public class BitmapUtil {
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static Bitmap Bytes2Bimap(byte[] b) {
     if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
     }
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,100,output);
        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,150, baos);
        return baos.toByteArray();
    }
    public static Bitmap drawableToBitamp(Drawable drawable)
    {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }
    /**
     * 根据网址获得图片，优先从本地获取，本地没有则从网络下载
     *
     * @param url 图片网址
     * @param context 上下文
     * @return 图片
     */
    public static Bitmap getBitmap(String url, Context context)
    {

        Bitmap bitmap = null;

        if (url == null)
        {
            return bitmap;
        }


        //Log.e(TAG, "URL = " + url);
        String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
        File file = new File(getPath(context), imageName);

        if (file.exists())
        {
            //Log.e(TAG, "getBitmap from local");
            return BitmapFactory.decodeFile(file.getPath());
        }else{
            return getNetBitmap(url, file, context);
        }
    }
    /**
     * 获取图片的存储目录，在有sd卡的情况下为 “/sdcard/apps_images/本应用包名/cach/images/”
     * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
     *
     * @param context
     *            上下文
     * @return 本地图片存储目录
     */
    private static String getPath(Context context)
    {
        String path = null;

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName() + "/cach/images/";

        if (hasSDCard)
        {
            path = "/sdcard/apps_images/" + packageName;

        } else {

            path = "/data/data/" + packageName;
        }

        File file = new File(path);
        boolean isExist = file.exists();

        if (!isExist)
        {
            file.mkdirs();
        }
        return file.getPath();
    }
    /**
     * 网络可用状态下，下载图片并保存在本地
     *
     * @param strUrl 图片网址
     * @param file 本地保存的图片文件
     * @param context 上下文
     * @return 图片
     */
    private static Bitmap getNetBitmap(String strUrl, File file, Context context)
    {
        Bitmap bitmap = null;
        //Log.e(TAG, "getBitmap from net");
        if (strUrl==null||strUrl.trim().equals("")){
            return bitmap;
        }
        if (isConnnected(context))
        {
            try
            {
                HttpGet httpRequest = new HttpGet(strUrl);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
                InputStream inputStream = bufferedHttpEntity.getContent();
                bitmap = BitmapFactory.decodeStream(inputStream);
                FileOutputStream outputStream = new FileOutputStream(file.getPath());
                if(bitmap!=null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("IOException>>>>>",e.toString());

            }
        }
        return bitmap;
    }
    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivityManager)
        {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo)
            {
                for (NetworkInfo info : networkInfo)
                {
                    if (info.getState() == NetworkInfo.State.CONNECTED)
                    {
                        //Log.e(TAG, "the net is ok");
                        return true;
                    }
                }
            }
        }

        //Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();

        return false;
    }

    /***
     * 图片的缩放方法
     *
     * @param bgimage
     *            ：源图片资源
     * @param newWidth
     *            ：缩放后宽度
     * @param newHeight
     *            ：缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,(int) height, matrix, true);
        return bitmap;
    }
}
