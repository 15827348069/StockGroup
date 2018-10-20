package com.zbmf.StockGroup.callback;

import org.apache.http.Header;

/**
 * Created by pq
 * on 2018/7/5.
 */

public interface LoadFileResult {
    /**
     * 图片下载之后做的处理
     *
     * @param statusCode
     * @param headers
     * @param binaryData
     */
  /*  String tempPath = Environment.getExternalStorageDirectory()
            .getPath() + "/temp.jpg";
    // 下载成功后需要做的工作

    //
                Log.e("binaryData:", "共下载了：" + binaryData.length);
    //
    Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
            binaryData.length);

    File file = new File(tempPath);
    // 压缩格式
    CompressFormat format = Bitmap.CompressFormat.JPEG;
    // 压缩比例
    int quality = 100;
                try {
        // 若存在则删除
        if (file.exists())
            file.delete();
        // 创建文件
        file.createNewFile();
        //
        OutputStream stream = new FileOutputStream(file);
        // 压缩输出
        bmp.compress(format, quality, stream);
        // 关闭
        stream.close();

    } catch (IOException e) {
        e.printStackTrace();
    }*/
    void onSuccess(int statusCode, Header[] headers, byte[] binaryData);
    void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error);
    void onProgress(int count);
    void onRetry(int retryNo);
}
