package com.alliky.core.net;

import android.content.Context;

import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.ILoading;
import com.alliky.core.net.callback.IRequest;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.net.loader.LoaderStyle;

import java.io.File;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author wxianing
 * date 2018/6/26
 */
public final class HttpClientBuilder {

    private static final WeakHashMap<String, Object> PARAMS = HttpCreator.getParams();
    private static final WeakHashMap<String, String> HEADERS = HttpCreator.getHeaders();
    private String mUrl = null;
    private IRequest mIRequest = null;
    private ISuccess mISuccess = null;
    private ILoading mILoading = null;
    private IFailure mIFailure = null;
    private IError mIError = null;
    private RequestBody mBody = null;
    private Context mContext = null;
    private LoaderStyle mLoaderStyle = null;
    private File mFile = null;
    private String mDownloadDir = null;
    private String mExtension = null;
    private String mName = null;
    private boolean mCancelable = true;


    HttpClientBuilder() {
    }

    public final HttpClientBuilder url(String url) {
        this.mUrl = url;
        return this;
    }

    public final HttpClientBuilder params(WeakHashMap<String, Object> params) {
        PARAMS.putAll(params);
        return this;
    }

    public final HttpClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final HttpClientBuilder headers(WeakHashMap<String, String> headers) {
        HEADERS.putAll(headers);
        return this;
    }

    public final HttpClientBuilder headers(String key, String value) {
        HEADERS.put(key, value);
        return this;
    }

    public final HttpClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final HttpClientBuilder file(String file) {
        this.mFile = new File(file);
        return this;
    }

    public final HttpClientBuilder name(String name) {
        this.mName = name;
        return this;
    }

    public final HttpClientBuilder dir(String dir) {
        this.mDownloadDir = dir;
        return this;
    }

    public final HttpClientBuilder extension(String extension) {
        this.mExtension = extension;
        return this;
    }

    public final HttpClientBuilder raw(String raw) {
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final HttpClientBuilder onRequest(IRequest iRequest) {
        this.mIRequest = iRequest;
        return this;
    }

    public final HttpClientBuilder success(ISuccess iSuccess) {
        this.mISuccess = iSuccess;
        return this;
    }

    public final HttpClientBuilder loading(ILoading iLoading) {
        this.mILoading = iLoading;
        return this;
    }

    public final HttpClientBuilder failure(IFailure iFailure) {
        this.mIFailure = iFailure;
        return this;
    }

    public final HttpClientBuilder error(IError iError) {
        this.mIError = iError;
        return this;
    }

    public final HttpClientBuilder loader(Context context, LoaderStyle style) {
        this.mContext = context;
        this.mLoaderStyle = style;
        return this;
    }

    public final HttpClientBuilder loader(Context context) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallSpinFadeLoaderIndicator;
        return this;
    }

    public final HttpClientBuilder loader(Context context, boolean cancelable) {
        this.mContext = context;
        this.mLoaderStyle = LoaderStyle.BallSpinFadeLoaderIndicator;
        this.mCancelable = cancelable;
        return this;
    }

    public final HttpClient build() {
        return new HttpClient(mUrl, PARAMS, HEADERS,
                mDownloadDir, mExtension, mName,
                mIRequest, mISuccess, mILoading, mIFailure,
                mIError, mBody, mFile, mContext, mCancelable,
                mLoaderStyle);
    }
}
