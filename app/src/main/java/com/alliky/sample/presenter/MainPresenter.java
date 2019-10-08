package com.alliky.sample.presenter;

import android.content.Context;

import com.alliky.core.base.BasePresenter;
import com.alliky.core.callback.CallbackListener;
import com.alliky.sample.contract.MainContract;
import com.alliky.sample.model.MainModel;

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
    public void getVehicleList(String cityName) {
        if (!isViewAttached())
            return;
        mModel.getVehicleList(mContext, cityName, new CallbackListener<String>() {
            @Override
            public void onSuccess(String result) {
                mView.getVehicleListResult(result);
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
