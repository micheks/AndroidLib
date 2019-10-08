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

        Toasty.normal(this, "这是什么");
        HttpClient.builder()
                .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
                .params("cityName", "深圳市")//参数，可添加多个
                .loader(this)//loading加载动画
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
                .get();

        HttpClient.builder()
                .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
                .params("cityName", "深圳市")//参数，可添加多个
                .extension("apk")//文件后缀名
                .dir("/")//保存到文件夹
                .loader(this)//loading加载动画
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
                .download();

    }
}
