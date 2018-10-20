package com.zbmf.worklibrary.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.zbmf.worklibrary.rxjava.NullStringToEmptyAdapterFactory;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by xiaote
 * on 2017/6/19.
 */

public class GsonUtil {
    private static Gson buildGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create();
    }
    public static <T> T parseData(Object data, Class<T> clazz) {
        Gson gson = buildGson();
        String jsonStr = gson.toJson(data);
        if (isEmptyJson(jsonStr)) {
            return null;
        }
        return gson.fromJson(jsonStr, clazz);
    }

    public static <T> List<T> parseData(Object data, Type type){
        try{
            Gson gson = buildGson();
            String jsonStr = gson.toJson(data);
            if(isEmptyJson(jsonStr)) {
                return null;
            }
            List<T> list = gson.fromJson(gson.toJson(data), type);
            return list;
        }catch (Exception e){
            Logx.e(e.toString());
            return null;
        }
    }

    public static <T> List<T> parseData(String jsonStr, Type type) {
        if (isEmptyJson(jsonStr)) {
            return null;
        }
        Gson gson = buildGson();
        List<T> list = gson.fromJson(jsonStr, type);
        return list;
    }

    public static <T> T parseData(String jsonStr, Class<T> clazz) {
        if (isEmptyJson(jsonStr)) {
            return null;
        }
        Gson gson = buildGson();
        T t = gson.fromJson(jsonStr, clazz);
        return t;
    }

    public static <T> String toJson(T t) {
        Gson gson = buildGson();
        return gson.toJson(t);
    }

    public static <T> String toJson(List<T> t, Type type) {
        Gson gson = buildGson();
        return gson.toJson(t, type);
    }
    private static boolean isEmpty(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }
    private static boolean isEmptyJson(String jsonStr) {
        if (isEmpty(jsonStr)) {
            return true;
        }
        switch (jsonStr) {
            case "[]":
            case "{}":
            case "null":
            case "false":
                return true;
        }
        return false;
    }
    private static boolean isGoodJson(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            System.out.println("bad json: " + json);
            return false;
        }
    }
}
