package com.example.library;

import com.alibaba.fastjson.JSON;
import com.example.library.request.IHttpRequest;
import com.example.library.response.ICallBackListener;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * author:  ycl
 * date:  2019/06/03 22:23
 * desc:
 */
class HttpTask<T> implements Runnable, Delayed {

    private IHttpRequest mIHttpRequest;

    public HttpTask(String url, T requestData, IHttpRequest httpRequest, ICallBackListener listener) {
        mIHttpRequest = httpRequest;
        mIHttpRequest.setUrl(url);
        mIHttpRequest.setListener(listener);

        String content = JSON.toJSONString(requestData);
        try {
            mIHttpRequest.setData(content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            mIHttpRequest.execute();
        } catch (RuntimeException e) {
            e.printStackTrace();
            ThreadPoolManager.getInstance().addDelayTask(this);
        }
    }

    // -----------------  重试机制 start ------------------

    private int retryCount;
    private long delayTime;

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = System.currentTimeMillis() + delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.delayTime-System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }
}
