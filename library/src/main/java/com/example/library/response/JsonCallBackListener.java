package com.example.library.response;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.example.library.convert.IJsonDataListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * author:  ycl
 * date:  2019/06/04 10:32
 * desc:
 */
public class JsonCallBackListener<T> implements ICallBackListener{

    private Class<T> mResponseClass;
    private IJsonDataListener mJsonDataListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public JsonCallBackListener(Class<T> responseClass, IJsonDataListener jsonDataListener) {
        mResponseClass = responseClass;
        mJsonDataListener = jsonDataListener;
    }

    @Override
    public void onSuccess(InputStream inputStream) {
        String response = getConnect(inputStream);
        final T clazz = JSON.parseObject(response, mResponseClass);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mJsonDataListener.onSuccess(clazz);
            }
        });
    }

    private String getConnect(InputStream inputStream) {
        try {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("exception "+e.toString());
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    streamReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception "+e.toString());
        }
        return null;
    }

    @Override
    public void onFailure() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mJsonDataListener.onFailure();
            }
        });
    }
}
