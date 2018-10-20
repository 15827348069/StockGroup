package com.zbmf.StocksMatch.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.zbmf.StocksMatch.callback.ResultCallback;
import com.zbmf.worklibrary.util.Logx;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Hashtable;

/**
 * Created by xuhao
 * on 2016/6/20.
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
        if (url==null)
        {
            return null;
        }
        Bitmap bitmap = getBitmap(url);
        if(bitmap!=null){
            return bitmap;
        }else{
            String imageName = url.substring(url.lastIndexOf("/") + 1, url.length());
            File file = new File(getPath(context),imageName);
            return getNetBitmap(url, file, context);
        }
    }

    /**
     * 获取本地图片
     * @param url
     * @return
     */
    public static Bitmap getBitmap(String url)
    {
        Bitmap bitmap = null;
        if (url == null)
        {
            return bitmap;
        }
        File file = new File(url);
        if (file.exists())
        {
            return BitmapFactory.decodeFile(file.getPath());
        }else{
            return null;
        }
    }
    public static void getBitmap(String url, Context context, ResultCallback<Bitmap> callback){

    }
    /**
     * @param context
     *            上下文
     * @return 本地图片存储目录
     */
    private static String getPath(Context context)
    {
        return FileUtils.getIntence().isCacheFileIsExit(context);
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
                HttpResponse response =  httpclient.execute(httpRequest);
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
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        int max_length=32;
        while ( baos.toByteArray().length/1024 >max_length) {  //循环判断如果压缩后图片是否大于32kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            if(options>5){
                options -= 5;//每次都减少10
            }else{
                break;
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        return  BitmapFactory.decodeStream(isBm, null, null);
    }
    public static byte[] Bitmap2Bytes(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 50, baos);
        return baos.toByteArray();
    }
    /***//**
     * 图片去色,返回灰度图片
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    //判断是否有二维码
    public static Result handleQRCodeFormBitmap(Bitmap bitmap) {
        Bitmap bmp=toGrayscale(bitmap);
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType,String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels,0,width,0,0,width,height);
        RGBLuminanceSource source =new RGBLuminanceSource(width,height,pixels);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        MultiFormatReader reader2= new MultiFormatReader();
        Result result = null;
        try {
            result = reader2.decode(bitmap1,hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
            Logx.e("NotFoundException"+e.getMessage());
        }
        return result;
    }
    /**
     * 压缩图片
     * @param image
     * @return
     */
    public static Bitmap CImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        int max_length=1024*4;//最大2M
        while (baos.toByteArray().length / 1024 > max_length) {
            // 重置baos
            baos.reset();
            // 这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
            // 每次都减少10
            options -= 5;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        return bitmap;
    }
    /**
     * 获取屏幕bitmap对象
     *
     * @return
     */
    public static void takeScreenShot(final LinearLayout linearLayout,final ResultCallback<Bitmap>callback) {
        // 创建对应大小的bitmap
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = Bitmap.createBitmap(linearLayout.getWidth(),linearLayout.getBottom(),Bitmap.Config.RGB_565);
                final Canvas canvas = new Canvas(bitmap);
                linearLayout.draw(canvas);
                if(bitmap==null){
                    callback.onFail("生成图片失败");
                }else{
                    callback.onSuccess(CImage(bitmap));
                }
            }
        });
    }
    public static String saveBitmap(Context context,String path, Bitmap bitmap){
        try {
            if (path != null && bitmap != null) {
                File _file = new File(path);
                // 如果文件夹不存在则创建一个新的文件
                if (!_file.exists()) {
                    _file.getParentFile().mkdirs();
                    _file.createNewFile();
                }
                // 创建输出流
                OutputStream write = new FileOutputStream(_file);
                // 获取文件名
                String fileName = _file.getName();
                // 取出文件的格式名
                String endName = fileName.substring(fileName.lastIndexOf(".") + 1);
                if ("png".equalsIgnoreCase(endName)) {
                    // bitmap的压缩格式
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, write);
                    write.close();
                } else {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, write);
                    write.close();
                }
                return _file.getPath();
            }
        }catch (IOException e){
            Logx.e(e.getMessage());
            return null;
        }
        return null;
    }
}
