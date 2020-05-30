package com.alliky.sample.presenter;

import android.content.Context;

import com.alliky.core.base.BasePresenter;
import com.alliky.core.callback.CallbackListener;
import com.alliky.core.net.parser.AppBean;
import com.alliky.sample.bean.Shopping;
import com.alliky.sample.bean.User;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.model.MainModel;

import java.util.List;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 18:27
 */
public class MainPresenter extends BasePresenter<MainContract.View> implements MainContract.Presenter {

    Context mContext;
    MainContract.Model mModel;

    public MainPresenter(Context context) {
        mContext = context;
        mModel = new MainModel();
    }

    @Override
    public void login(String username, String password) {
        if (!isViewAttached())
            return;
        mModel.login(mContext, username, password, new CallbackListener<User>() {
            @Override
            public void onSuccess(User result) {
                mView.loginResult(result);
            }

            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public void getShopList(int pageNum, int pageSize) {
        if (!isViewAttached())return;
        mModel.getShopList(mContext, pageNum, pageSize, new CallbackListener<List<Shopping>>() {
            @Override
            public void onSuccess(List<Shopping> result) {
                mView.getShopListResult(result);
            }

            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
