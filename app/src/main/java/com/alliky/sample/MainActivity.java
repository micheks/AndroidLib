package com.alliky.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.net.HttpClient;
import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.util.LogUtil;
import com.alliky.core.util.PhotoUtil;
import com.alliky.core.util.Toasty;

import java.io.File;

public class MainActivity extends BaseActivity {

    TextView mTextView;

    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        mTextView = (TextView) findViewById(R.id.textView);

        //拍照或者相册选择回调
        PhotoUtil.getInstance().init(this, false, new PhotoUtil.OnSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                //TODO
            }
        });
    }

    @Override
    public void onInitData() {
        HttpClient.builder()
                .url("user/biz/specialCar/category")//请求地址url，不包含域名端口
                .params("cityName", "深圳市")//参数，可添加多个
                .loader(this)//loading加载动画
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        //成功回调
                        mTextView.setText(response);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
