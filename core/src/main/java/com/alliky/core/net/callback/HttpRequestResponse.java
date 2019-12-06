package com.alliky.core.net.callback;

import com.alliky.core.net.common.Callback;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:52
 */
public abstract class HttpRequestResponse<ResultType> {

    public abstract void onSuccess(ResultType result);

    public void onError(Throwable ex, boolean isOnCallback) {
    }

    public void onCancelled(Callback.CancelledException cex) {

    }

    public void onFinished() {

    }

    public void onWaiting() {

    }

    public void onStarted() {

    }

    public void onLoading(long total, long current, boolean isDownloading) {

    }

}
