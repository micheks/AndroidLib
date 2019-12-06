package com.alliky.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.alliky.core.base.BaseActivity;
import com.alliky.core.dialog.MessageDialog;
import com.alliky.core.dialog.interfaces.OnDialogButtonClickListener;
import com.alliky.core.dialog.util.BaseDialog;
import com.alliky.core.net.HttpClient;
import com.alliky.core.net.callback.IFSuccess;
import com.alliky.core.net.callback.ILoading;
import com.alliky.core.util.FileUtil;
import com.alliky.core.util.Logger;
import com.alliky.core.util.Toasty;
import com.alliky.core.widget.TitleBar;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.presenter.MainPresenter;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, View.OnClickListener {

    private TitleBar mTitleBar;

    private Button downloadBtn;

    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;


    @Override
    public Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        mPresenter = new MainPresenter(this);
        mPresenter.attachView(this);


        downloadBtn = findViewById(R.id.download_btn);

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
                MessageDialog.show(MainActivity.this, "温馨提示", "由于您的账号在别的设备上登录！", "确定", "取消")
                        .setOnCancelButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                Toasty.normal(mActivity, "点击了取消按钮").show();
                                return false;
                            }
                        })
                        .setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
//                        Toasty.normal(mActivity,"确定").show();

                                Toast.makeText(mActivity, "确定", Toast.LENGTH_LONG).show();

                                return false;
                            }
                        });

            }
        });


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }

    }

    @Override
    public void onInitData() {
        mPresenter.getVehicleList("深圳市");
    }

    @Override
    public void onInitEvent() {
        downloadBtn.setOnClickListener(this);
    }

    @Override
    public void getVehicleListResult(String response) {
    }

    private String downloadUrl = "http://oss.dev.diyue123.com/upload/apk/20191122/13697a84725d4b5b93e9efd1cd0bad7c.apk";
    final String appName = "嘀约送货司机版";
    final String savePath = Environment.getExternalStorageDirectory()
            .toString()
            + File.separator
            + appName
            + File.separator
            + appName
            + ".apk";

    @Override
    public void onClick(View view) {
        download();
    }

    private void download() {
        HttpClient.builder()
                .url(downloadUrl)
                .savePath(savePath)
                .fsuccess(new IFSuccess<File>() {
                    @Override
                    public void onSuccess(File file) {
                        if (file.getName().endsWith(".apk")) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Uri contentUri = FileProvider.getUriForFile(mContext, FileUtil.AUTHORITY, file);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                            } else {
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                            }
                            startActivity(intent);
                        }
                    }
                })
                .loading(new ILoading() {
                    @Override
                    public void onLoading(long total, long current) {
                        Logger.i("文件大小：" + total, "当前进度:" + current);
                    }
                })
                .build()
                .download();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }
}
