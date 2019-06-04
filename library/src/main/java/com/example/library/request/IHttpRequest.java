package com.example.library.request;


import com.example.library.response.ICallBackListener;

/**
 * author:  ycl
 * date:  2019/06/03 22:19
 * desc:
 */
public interface IHttpRequest {
    void setUrl(String url);

    void setData(byte[] data);

    void setListener(ICallBackListener listener);

    void execute() throws RuntimeException;
}
