package com.alliky.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.net.HttpClient;
import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.util.PhotoUtils;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.presenter.MainPresenter;

import java.io.File;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    TextView mTextView;

    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {

        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);

        mTextView = (TextView) findViewById(R.id.textView);

        //拍照或者相册选择回调
        PhotoUtils.getInstance().init(this, false, new PhotoUtils.OnSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                //TODO
            }
        });
    }

    @Override
    public void onInitData() {
        mPresenter.getVehicleList("深圳市");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getVehicleListResult(String response) {
        mTextView.setText(response);
    }
}
