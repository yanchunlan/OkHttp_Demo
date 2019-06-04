package com.example.library.convert;

/**
 * author:  ycl
 * date:  2019/06/04 10:35
 * desc:
 */
public interface IJsonDataListener<T> {

    void onSuccess(T m);

    void onFailure();
}
