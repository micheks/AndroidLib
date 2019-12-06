package com.alliky.core.net.app;

import com.alliky.core.ex.HttpException;
import com.alliky.core.net.HttpMethod;
import com.alliky.core.net.common.Callback;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.util.Logger;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:29
 */
public class HttpRetryHandler {
    protected int maxRetryCount = 2;

    protected static HashSet<Class<?>> blackList = new HashSet<Class<?>>();

    static {
        blackList.add(HttpException.class);
        blackList.add(Callback.CancelledException.class);
        blackList.add(MalformedURLException.class);
        blackList.add(URISyntaxException.class);
        blackList.add(NoRouteToHostException.class);
        blackList.add(PortUnreachableException.class);
        blackList.add(ProtocolException.class);
        blackList.add(NullPointerException.class);
        blackList.add(FileNotFoundException.class);
        blackList.add(JSONException.class);
        blackList.add(UnknownHostException.class);
        blackList.add(IllegalArgumentException.class);
    }

    public HttpRetryHandler() {
    }

    public void setMaxRetryCount(int maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public boolean canRetry(UriRequest request, Throwable ex, int count) {

        Logger.w(ex.getMessage(), ex.getMessage());

        if (count > maxRetryCount) {
            Logger.w(request.toString());
            Logger.w("The Max Retry times has been reached!");
            return false;
        }

        if (!HttpMethod.permitsRetry(request.getParams().getMethod())) {
            Logger.w(request.toString());
            Logger.w("The Request Method can not be retried.");
            return false;
        }

        if (blackList.contains(ex.getClass())) {
            Logger.w(request.toString());
            Logger.w("The Exception can not be retried.");
            return false;
        }

        return true;
    }
}
