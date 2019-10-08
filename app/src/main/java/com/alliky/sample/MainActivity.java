package com.alliky.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alliky.core.net.HttpClient;
import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.util.LogUtil;
import com.alliky.core.util.Toasty;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toasty.normal(this,"这是什么");
        HttpClient.builder()
                .url("user/biz/specialCar/category")
                .params("cityName", "深圳市")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //成功回调
                        LogUtil.i(response);
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(Throwable t) {
                        //失败回调
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        //错误回调
                    }
                })
                .build()
                .post();
    }
}
