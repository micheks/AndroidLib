package com.alliky.core.net.callback;

import com.alliky.core.net.common.Callback;

/**
 * Author wxianing
 * date 2018/7/7
 */
public interface ICancelled {
    void onCancelled(Callback.CancelledException cex);
}
