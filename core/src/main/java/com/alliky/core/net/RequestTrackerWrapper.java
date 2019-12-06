package com.alliky.core.net;

import com.alliky.core.net.app.RequestTracker;
import com.alliky.core.net.params.RequestParams;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.util.Logger;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:46
 */
/*package*/ final class RequestTrackerWrapper implements RequestTracker {

    private final RequestTracker base;

    public RequestTrackerWrapper(RequestTracker base) {
        this.base = base;
    }

    @Override
    public void onWaiting(RequestParams params) {
        try {
            base.onWaiting(params);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }

    @Override
    public void onStart(RequestParams params) {
        try {
            base.onStart(params);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }

    @Override
    public void onRequestCreated(UriRequest request) {
        try {
            base.onRequestCreated(request);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }

    @Override
    public void onCache(UriRequest request, Object result) {
        try {
            base.onCache(request, result);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }

    @Override
    public void onSuccess(UriRequest request, Object result) {
        try {
            base.onSuccess(request, result);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }

    @Override
    public void onCancelled(UriRequest request) {
        try {
            base.onCancelled(request);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }

    @Override
    public void onError(UriRequest request, Throwable ex, boolean isCallbackError) {
        try {
            base.onError(request, ex, isCallbackError);
        } catch (Throwable exOnError) {
            Logger.e(exOnError.getMessage(), exOnError.getMessage());
        }
    }

    @Override
    public void onFinished(UriRequest request) {
        try {
            base.onFinished(request);
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
    }
}
