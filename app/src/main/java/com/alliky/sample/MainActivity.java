package com.alliky.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.dialog.MessageDialog;
import com.alliky.core.dialog.interfaces.OnDialogButtonClickListener;
import com.alliky.core.dialog.util.BaseDialog;
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

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageDialog.show(MainActivity.this,"温馨提示","由于您的账号在别的设备上登录！","确定","取消")
                .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
                        Toasty.normal(mActivity,"点击了取消按钮").show();
                        return false;
                    }
                })
                .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                    @Override
                    public boolean onClick(BaseDialog baseDialog, View v) {
//                        Toasty.normal(mActivity,"确定").show();

                        Toast.makeText(mActivity,"确定",Toast.LENGTH_LONG).show();

                        return false;
                    }
                });

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
