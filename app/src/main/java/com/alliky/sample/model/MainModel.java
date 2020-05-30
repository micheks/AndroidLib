package com.alliky.sample.model;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alliky.core.callback.CallbackListener;
import com.alliky.core.net.HttpClient;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.net.parser.AppBean;
import com.alliky.core.net.parser.JsonResponseParser;
import com.alliky.core.util.Toasty;
import com.alliky.sample.bean.Shopping;
import com.alliky.sample.bean.User;
import com.alliky.sample.contract.MainContract;

import java.util.List;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 18:26
 */
public class MainModel implements MainContract.Model {
    @Override
    public void login(final Context context, final String username, String password, final CallbackListener<User> listener) {
        HttpClient.builder()
                .url("user/login")//请求地址url，不包含域名端口
                .params("username", username)//参数，可添加多个
                .params("password", password)//参数，可添加多个
                .loader(context)//loading加载动画
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        AppBean<User> appBean = JSONObject.parseObject(response,new TypeReference<AppBean<User>>(){});
                        if (appBean!=null){
                            if (appBean.isSuccess()){
                                listener.onSuccess(appBean.getData());
                            }else {
                                Toasty.normal(context,appBean.getMessage());
                            }
                        }
                    }
                })
                .build()
                .post();
    }

    @Override
    public void getShopList(final Context context, int pageNum, int pageSize, final CallbackListener<List<Shopping>> listener) {
        HttpClient.builder()
                .url("shipping/shoplist")//请求地址url，不包含域名端口
                .params("pageNum", pageNum)//参数，可添加多个
                .params("pageSize", pageNum)//参数，可添加多个
                .loader(context)//loading加载动画
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        AppBean<List<Shopping>> appBean = JSONObject.parseObject(response, new TypeReference<AppBean<List<Shopping>>>() {
                        });

                        if (appBean != null) {
                            if (appBean.isSuccess()) {
                                List<Shopping> list = appBean.getData();
                                listener.onSuccess(list);
                            }else {
                                Toasty.normal(context,appBean.getMessage());
                            }
                        }
                    }
                })
                .build()
                .post();

    }
}
