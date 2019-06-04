package com.example.library.request;


import com.example.library.response.ICallBackListener;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author:  ycl
 * date:  2019/06/03 22:42
 * desc:
 */
public class JsonHttpRequest implements IHttpRequest {

    private String mUrl;
    private byte[] mData;
    private ICallBackListener mICallBackListener;
    private HttpURLConnection mHttpURLConnection;

    @Override
    public void setUrl(String url) {
        this.mUrl = url;
    }

    @Override
    public void setData(byte[] data) {
        this.mData = data;
    }

    @Override
    public void setListener(ICallBackListener listener) {
        this.mICallBackListener = listener;
    }

    @Override
    public void execute() throws RuntimeException {
        URL url = null;

        try {
            url = new URL(this.mUrl);
            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            mHttpURLConnection.setConnectTimeout(6000);
            mHttpURLConnection.setUseCaches(false);
            mHttpURLConnection.setInstanceFollowRedirects(true);// 开启重定向
            mHttpURLConnection.setReadTimeout(3000);
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.setDoOutput(true);
            mHttpURLConnection.setRequestMethod("POST");
            mHttpURLConnection.setRequestProperty("content-type", "application/json;charset=UTF-8");

            // 字节流发送数据
            OutputStream out = mHttpURLConnection.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(out);
            bos.write(mData);
            bos.flush();
            out.close();
            bos.close();

            // 字节流写入数据
            if (mHttpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = mHttpURLConnection.getInputStream();
                mICallBackListener.onSuccess(in);
            } else {
                System.out.println("request failed" );
                throw new RuntimeException("request failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception "+e.toString());;
            throw new RuntimeException("request exception");
        } finally {
            mHttpURLConnection.disconnect();
        }
    }
}
