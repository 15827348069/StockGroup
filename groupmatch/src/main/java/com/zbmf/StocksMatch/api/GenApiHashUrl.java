package com.zbmf.StocksMatch.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;


public class GenApiHashUrl {
    private static GenApiHashUrl INSTANCE = null;
    public static  String apiUrlm = "http://www.zbmf.com/newapi/json/";
    public static String loginUrl="http://rest.zbmf.com";
//    public static final String apiUrln = "http://www.zbmf.cn/newapi/json/";
    private static int requestCount = 0;

    public static GenApiHashUrl getInstance() {
        requestCount = 0;
        if (INSTANCE == null) {
            INSTANCE = new GenApiHashUrl();
        }
        return INSTANCE;
    }

    private GenApiHashUrl() {
    }


    public String Http_Get(String postParams) {
        String ret = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000); // 读取超时

            HttpGet request = new HttpGet(apiUrlm + postParams);
            HttpResponse httpResponse = httpClient.execute(request);
            // 解析返回的内容
            int rs = httpResponse.getStatusLine().getStatusCode();
            if (rs != 404) {
                ret = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.e("atan", ret.toString());
        return ret;
    }
    public String Http_Get(String url,String postParams) {
        String ret = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); // 请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000); // 读取超时

            HttpGet request = new HttpGet(url + postParams);
            HttpResponse httpResponse = httpClient.execute(request);
            // 解析返回的内容
            int rs = httpResponse.getStatusLine().getStatusCode();
            if (rs != 404) {
                ret = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.e("atan", ret.toString());
        return ret;
    }

    public String Http_Post(List<NameValuePair> params) {
        List<NameValuePair> temp = params;
        String ret = "";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
            //读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
            HttpPost request = new HttpPost(apiUrlm.replace("?", "")); // 根据内容来源地址创建一个Http请求
            Log.e("request", "request:" + request.getURI());
//			ByteArrayEntity entity = new ByteArrayEntity(mImageBuffer);
//			entity.setContentType("binary/octet-stream");
//			entity.setChunked(true);
//			request.setEntity(entity);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(temp, HTTP.UTF_8);
            request.setEntity(formEntity);

            HttpResponse httpResponse = httpClient.execute(request); // 发送请求并获取反馈
            // 解析返回的内容
            int rs = httpResponse.getStatusLine().getStatusCode();
            if (rs != 404) {
                ret = EntityUtils.toString(httpResponse.getEntity());
            }


        } catch (Exception e) {
            Log.e("atan", "error.Http_Post:" + e.toString());
            return null;
        }
        Log.e("atan", "ret:" + ret);
        return ret;
    }

    public String Http_Post(String url, List<NameValuePair> params) {
        String ret = "";
        List<NameValuePair> temp = params;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            //请求超时
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 6000);
            //读取超时
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 6000);
            HttpPost request = new HttpPost(url.replace("?", "")); // 根据内容来源地址创建一个Http请求
//			ByteArrayEntity entity = new ByteArrayEntity(mImageBuffer);
//			entity.setContentType("binary/octet-stream");
//			entity.setChunked(true);
//			request.setEntity(entity);
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(temp, HTTP.UTF_8);
            request.setEntity(formEntity);
            HttpResponse httpResponse = httpClient.execute(request); // 发送请求并获取反馈
            // 解析返回的内容
            int rs = httpResponse.getStatusLine().getStatusCode();
            if (rs != 404) {
                ret = EntityUtils.toString(httpResponse.getEntity());
            }
            Log.e("atan","apiUrl:"+request.getURI()+"?"+request.getEntity());
        } catch (Exception e) {
            return null;
        }
        Log.e("atan", "ret:" + ret);

        return ret;
    }


    /**
     * 下载图片资源
     *
     * @param urlStr
     * @return Bitmap
     */
    public Bitmap downloadPic(String urlStr) {
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        Bitmap bitmap = null;

        try {
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30 * 1000);
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}
