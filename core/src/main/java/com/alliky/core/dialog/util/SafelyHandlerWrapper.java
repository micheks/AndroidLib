package com.alliky.core.dialog.util;

import android.os.Handler;
import android.os.Message;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:47
 */
public class SafelyHandlerWrapper extends Handler {
    private Handler impl;
    
    public SafelyHandlerWrapper(Handler impl) {
        this.impl = impl;
    }
    
    @Override
    public void dispatchMessage(Message msg) {
        try {
            impl.dispatchMessage(msg);
        } catch (Exception e) {
        }
    }
    
    @Override
    public void handleMessage(Message msg) {
        impl.handleMessage(msg);
    }
}