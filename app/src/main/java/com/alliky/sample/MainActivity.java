package com.alliky.sample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.util.Toasty;
import com.alliky.core.widget.TitleBar;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private TitleBar mTitleBar;

    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);

        mTitleBar = (TitleBar) findViewById(R.id.title_bar);
        mTitleBar.setListener(new TitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == TitleBar.ACTION_LEFT_TEXT) {
                    onBackPressed();
                } else if (action == TitleBar.ACTION_RIGHT_TEXT) {
                    Toasty.normal(mContext, "你点击了按钮!");
                }
            }
        });
    }

    @Override
    public void onInitData() {
        mPresenter.getVehicleList("深圳市");
    }

    @Override
    public void getVehicleListResult(String response) {
    }
}
