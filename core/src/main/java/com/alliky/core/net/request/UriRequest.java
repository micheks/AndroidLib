package com.alliky.core.net.request;

import com.alliky.core.net.Utils;
import com.alliky.core.net.app.RequestInterceptListener;
import com.alliky.core.net.app.ResponseParser;
import com.alliky.core.net.loader.LoaderFactory;
import com.alliky.core.net.loader.Loaders;
import com.alliky.core.net.callback.ProgressHandler;
import com.alliky.core.net.params.RequestParams;
import com.alliky.core.util.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:54
 */
public   abstract class UriRequest implements Closeable {

    protected final String queryUrl;
    protected final RequestParams params;
    protected final Loaders<?> loader;

    protected ProgressHandler progressHandler = null;
    protected ResponseParser responseParser = null;
    protected RequestInterceptListener requestInterceptListener = null;

    public UriRequest(RequestParams params, Type loadType) throws Throwable {
        this.params = params;
        this.queryUrl = buildQueryUrl(params);
        this.loader = LoaderFactory.getLoader(loadType);
        this.loader.setParams(params);
    }

    // build query
    protected String buildQueryUrl(RequestParams params) throws IOException {
        return params.getUri();
    }

    public void setProgressHandler(ProgressHandler progressHandler) {
        this.progressHandler = progressHandler;
        this.loader.setProgressHandler(progressHandler);
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public void setRequestInterceptListener(RequestInterceptListener requestInterceptListener) {
        this.requestInterceptListener = requestInterceptListener;
    }

    public RequestParams getParams() {
        return params;
    }

    public String getRequestUri() {
        return queryUrl;
    }

    /**
     * invoke via Loader
     */
    public abstract void sendRequest() throws Throwable;

    public abstract boolean isLoading();

    public abstract String getCacheKey();

    /**
     * 由loader发起请求, 拿到结果.
     */
    public Object loadResult() throws Throwable {
        return this.loader.load(this);
    }

    /**
     * 尝试从缓存获取结果, 并为请求头加入缓存控制参数.
     */
    public abstract Object loadResultFromCache() throws Throwable;

    public abstract void clearCacheHeader();

    public void save2Cache() {
        Utils.task().run(new Runnable() {
            @Override
            public void run() {
                try {
                    loader.save2Cache(UriRequest.this);
                } catch (Throwable ex) {
                    Logger.e(ex.getMessage(), ex.getMessage());
                }
            }
        });
    }

    public abstract InputStream getInputStream() throws IOException;

    @Override
    public abstract void close() throws IOException;

    public abstract long getContentLength();

    public abstract int getResponseCode() throws IOException;

    public abstract String getResponseMessage() throws IOException;

    public abstract long getExpiration();

    public abstract long getLastModified();

    public abstract String getETag();

    public abstract String getResponseHeader(String name);

    public abstract Map<String, List<String>> getResponseHeaders();

    public abstract long getHeaderFieldDate(String name, long defaultValue);

    @Override
    public String toString() {
        return getRequestUri();
    }
}
