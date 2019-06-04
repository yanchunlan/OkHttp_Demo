package com.example.okhttp_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.library.EasyOKHttp;
import com.example.library.bean.ResponseEntity;
import com.example.library.convert.IJsonDataListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button mBtnRequest;
    private TextView mTvResponse;

    private String url = "http://v.juhe.cn/historyWeacher/citys?province_id=2&key=bb52107206585ab074f5e59a8c73875b";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mBtnRequest = (Button) findViewById(R.id.btn_request);
        mTvResponse = (TextView) findViewById(R.id.tv_response);

        mBtnRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                sendJsonRequest();
                //   未完待续 ： https://github.com/CodeLiuPu/UpdateHttp
                break;
        }
    }

    private void sendJsonRequest() {
        EasyOKHttp.sendJsonRequest(null, url, ResponseEntity.class, new IJsonDataListener<ResponseEntity>() {
            @Override
            public void onSuccess(ResponseEntity m) {
                Log.d(TAG, "onSuccess: "+m);
            }

            @Override
            public void onFailure() {
                Log.d(TAG, "onFailure: ");
            }
        });
    }


}
