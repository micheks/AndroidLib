package com.alliky.core.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alliky.core.dialog.util.DialogSettings;
import com.alliky.core.entity.EventMessage;
import com.alliky.core.util.Toasty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/9/29 17:18
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {

    public static final String TAG = "BaseActivity";
    protected T mPresenter;
    public Context mContext;

    public Activity mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //设置屏幕不进入睡眠
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //去掉toolbar
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        if (setLayout() instanceof Integer) {
            setContentView((int) setLayout());
        } else if (setLayout() instanceof View) {
            setContentView((View) setLayout());
        } else {
            throw new ClassCastException("type of setLayout() must be int or View!");
        }

        mContext = this;
        mActivity = this;

//        ButterKnife.bind(this);

        onInitView(savedInstanceState);
        onInitData();
        onInitEvent();
        DialogSettings.init();
        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
        DialogSettings.theme = DialogSettings.THEME.LIGHT;
    }

    public abstract Object setLayout();

    public abstract void onInitView(@Nullable Bundle savedInstanceState);

    public void onInitData() {
    }

    public void onInitEvent() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventMessage message) {

    }

    /**
     * 页面跳转
     *
     * @param clazz
     */
    public void startActivity(Class<?> clazz) {
        startActivity(clazz, null);
    }

    /**
     * 携带数据跳转
     *
     * @param clazz
     * @param bundle
     */
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void Toast(String message){
        Toasty.normal(mContext,message).show();
    }

    private static long lastClickTime;
    private static long delayTime = 800;

    //防止点击事件过快,点击时间间隔小于800毫秒时为重复点击
    public static boolean isFastClick() {
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - lastClickTime;
        if (0 < intervalTime && intervalTime < delayTime) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息接收
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
