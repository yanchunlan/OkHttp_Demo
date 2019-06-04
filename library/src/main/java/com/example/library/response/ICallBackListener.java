package com.example.library.response;

import java.io.InputStream;

/**
 * author:  ycl
 * date:  2019/06/03 22:21
 * desc:
 */
public interface ICallBackListener {

    void onSuccess(InputStream inputStream);

    void onFailure();
}
