package com.alliky.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.util.Logger;
import com.alliky.core.util.PhotoUtils;
import com.alliky.sample.bean.Shopping;
import com.alliky.sample.bean.User;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.presenter.MainPresenter;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, View.OnClickListener {

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
        mPresenter.login("admin","admin");
    }

    @Override
    public void onInitEvent() {
        mTextView.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void loginResult(User user) {
        if (user!=null) {
            mTextView.setText(user.getUsername());
        }
    }

    @Override
    public void getShopListResult(List<Shopping> appBean) {
        for (int i = 0; i < appBean.size(); i++) {
            Logger.i("list",appBean.get(i).getReceiverName()+">>");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView:
                mPresenter.getShopList(1,12);
                break;
        }
    }
}
