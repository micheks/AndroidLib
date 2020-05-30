package com.alliky.sample;

import com.alliky.core.base.BaseApplication;
import com.alliky.core.config.Kylin;
import com.alliky.core.net.interceptors.DebugInterceptor;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/9/29 17:34
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化常用配置
        Kylin.init(this)
                .withLoaderDelayed(500)
                .withApiHost("http://192.168.0.102:8088/")
                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .configure();
    }
}
