package com.example.library;

import com.example.library.convert.IJsonDataListener;
import com.example.library.request.IHttpRequest;
import com.example.library.request.JsonHttpRequest;
import com.example.library.response.ICallBackListener;
import com.example.library.response.JsonCallBackListener;

/**
 * author:  ycl
 * date:  2019/06/04 10:45
 * desc:
 */
public class EasyOKHttp {
    public static <T, M> void sendJsonRequest(T requestData,
                                              String url,
                                              Class<M> response,
                                              IJsonDataListener listener) {
        IHttpRequest httpRequest = new JsonHttpRequest();
        ICallBackListener callBackListener = new JsonCallBackListener<>(response, listener);
        HttpTask httpTask = new HttpTask(url, requestData, httpRequest, callBackListener);
        ThreadPoolManager.getInstance().addTask(httpTask);
    }
}
