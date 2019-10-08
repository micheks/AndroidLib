package com.alliky.sample.contract;

import android.content.Context;

import com.alliky.core.base.BaseView;
import com.alliky.core.callback.CallbackListener;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 18:15
 */
public interface MainContract {

    interface View extends BaseView {
        void getVehicleListResult(String response);
    }

    interface Model {

        void getVehicleList(Context context, String cityName,  final CallbackListener<String> listener);

    }

    interface Presenter {

        void getVehicleList(String cityName);

    }
}
