package com.alliky.sample.contract;

import android.content.Context;

import com.alliky.core.base.BaseView;
import com.alliky.core.callback.CallbackListener;
import com.alliky.core.net.parser.AppBean;
import com.alliky.sample.bean.Shopping;
import com.alliky.sample.bean.User;

import java.util.List;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 18:15
 */
public interface MainContract {

    interface View extends BaseView {
        void loginResult(User user);
        void getShopListResult(List<Shopping> list);
    }

    interface Model {
        void login(Context context, String username, String password, final CallbackListener<User> listener);
        void getShopList(Context context,int pageNum,int pageSize,final CallbackListener<List<Shopping>> listener);
    }

    interface Presenter {
        void login(String username, String password);
        void getShopList(int pageNum,int pageSize);
    }
}
