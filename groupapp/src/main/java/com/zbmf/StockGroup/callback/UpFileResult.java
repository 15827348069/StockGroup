package com.zbmf.StockGroup.callback;

import org.apache.http.Header;

/**
 * Created by pq
 * on 2018/7/5.
 */

public interface UpFileResult {
    void success(int statusCode, Header[] headers, byte[] responseBody);
    void failure(int statusCode, Header[] headers, byte[] responseBody, Throwable error);
    void onProgress(int count);
    void onRetry(int retryNo);
    void failNoExist(String tip);
}
