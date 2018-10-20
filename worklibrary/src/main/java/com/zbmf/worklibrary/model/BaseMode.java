package com.zbmf.worklibrary.model;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zbmf.worklibrary.rxjava.BaseObserver;
import com.zbmf.worklibrary.rxjava.NullStringToEmptyAdapterFactory;
import com.zbmf.worklibrary.rxjava.RetrofitService;
import com.zbmf.worklibrary.rxjava.RxSchedulers;
import com.zbmf.worklibrary.util.Logx;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xuhao
 * on 2017/11/21.
 */

public abstract class BaseMode implements IBaseMode{
    private final long TIMEOUT = 8;
    protected abstract String getHost();
    private boolean isDestory;
    BaseObserver observer;
    @Override
    public void onDestory() {
        Logx.e("销毁"+this.getClass().getName());
        isDestory=true;
        if(observer!=null){
            observer.setDestory(true);
            observer.onComplete();
        }
    }

    private OkHttpClient httpClient = new OkHttpClient.Builder()
            // 添加通用的Header
            .addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder builder = chain.request().newBuilder();
                    builder.addHeader("token", "123");
                    return chain.proceed(builder.build());
                }
            })
            /*
            这里可以添加一个HttpLoggingInterceptor，因为Retrofit封装好了从Http请求到解析，
            出了bug很难找出来 问题，添加HttpLoggingInterceptor拦截器方便调试接口
             */
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Logx.e(message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();
    private Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
    }
    /**
     * 从服务器获取数据
     * @param map
     */
    public void getSubscrube(Map<String, String> map,CallBack callBack) {
        observer=new BaseObserver(callBack);
        RetrofitService retrofitService = new Retrofit.Builder()
                .baseUrl(getHost())
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(RetrofitService.class);
        if(!isDestory){
            Observable<Object> observable = retrofitService.get(getHost(),map);
            observable.compose(RxSchedulers.compose()).subscribe(observer);
        }else{
            observer.onComplete();
        }
    }

    /**
     * 提交数据到服务器
     * @param map
     */
    public void postSubscrube(String method, Map<String, String> map,CallBack callBack) {
        observer=new BaseObserver(callBack);
        RetrofitService retrofitService = new Retrofit.Builder()
                .baseUrl(getHost())
                // 添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                // 添加Retrofit到RxJava的转换器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build()
                .create(RetrofitService.class);
        if(!isDestory){
            Observable<Object> observable = retrofitService.post(getHost(),method, map);
            observable.compose(RxSchedulers.compose()).subscribe(observer);
        }else{
            observer.onComplete();
        }
    }
}
