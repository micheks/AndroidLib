package com.alliky.core.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements BaseView {

    //上下文
    public Context mContext;
    public T mPresenter;
//    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView;
        if (setLayout() instanceof Integer) {
            rootView = inflater.inflate((int) setLayout(), container, false);
        } else if (setLayout() instanceof View) {
            rootView = (View) setLayout();
        } else {
            throw new ClassCastException("type of setLayout() must be int or View!");
        }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mContext = getActivity();

//        mUnbinder = ButterKnife.bind(this, rootView);

        onInitView(savedInstanceState, rootView);

        onInitData();

        onInitEvent();

        return rootView;
    }

    public abstract Object setLayout();

    public abstract void onInitView(Bundle savedInstanceState, View view);

    public void onInitData() {
    }

    public void onInitEvent() {
    }

    public void ToastMsg(String message) {
        Toasty.normal(mContext, message).show();
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
        intent.setClass(getActivity(), clazz);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mUnbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }

    }
}
