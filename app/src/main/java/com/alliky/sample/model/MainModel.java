package com.alliky.sample.model;

import android.content.Context;

import com.alliky.core.callback.CallbackListener;
import com.alliky.core.net.HttpClient;
import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.sample.contract.MainContract;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 18:26
 */
public class MainModel implements MainContract.Model {
    @Override
    public void getVehicleList(Context context, String cityName, CallbackListener<String> listener) {
        HttpClient.builder()
                .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
                .params("cityName", cityName)//参数，可添加多个
                .loader(context)//loading加载动画
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //成功回调
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
