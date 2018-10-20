package com.zbmf.worklibrary.rxjava;


import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by xuhao on 2018/1/3.
 */

public class MultipartBuilder {
    /**
     * 单文件上传构造.
     *
     * @param file 文件
     * @param requestBody 请求体
     * @return MultipartBody
     */
    public static MultipartBody fileToMultipartBody(File file, Map<String,String>request, RequestBody requestBody) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String key : request.keySet()) {
            builder.addFormDataPart(key,request.get(key));
        }
        builder.addFormDataPart("avatar", file.getName(), requestBody);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

}
