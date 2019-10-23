package com.alliky.sample;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.widget.CommonTitleBar;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.presenter.MainPresenter;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {

    private CommonTitleBar mTitleBar;



    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);

        mTitleBar = (CommonTitleBar) findViewById(R.id.title_bar);
        mTitleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action==CommonTitleBar.ACTION_LEFT_TEXT){
                    onBackPressed();
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
