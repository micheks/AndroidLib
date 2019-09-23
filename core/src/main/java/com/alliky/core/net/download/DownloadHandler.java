package com.alliky.core.net.download;

import android.os.AsyncTask;

import com.alliky.core.net.HttpCreator;
import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.ILoading;
import com.alliky.core.net.callback.IRequest;
import com.alliky.core.net.callback.ISuccess;
import com.blankj.utilcode.util.StringUtils;

import java.util.WeakHashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author wxianing
 * date 2018/6/26
 */
public final class DownloadHandler {

    private final String URL;
    private static final WeakHashMap<String, Object> PARAMS = HttpCreator.getParams();
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final ILoading LOADING;

    public DownloadHandler(String url,
                           IRequest request,
                           String downDir,
                           String extension,
                           String name,
                           ISuccess success,
                           ILoading loading,
                           IFailure failure,
                           IError error) {
        this.URL = url;
        this.REQUEST = request;
        this.DOWNLOAD_DIR = downDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.LOADING = loading;
    }

    public final void handleDownload() {
        
        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        HttpCreator
                .getHttpService()
                .download(URL, PARAMS)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            final ResponseBody responseBody = response.body();
                            final SaveFileTask task = new SaveFileTask(REQUEST, SUCCESS, LOADING);

                            String ext = EXTENSION;

                            if (StringUtils.isEmpty(ext)) {
                                ext = URL.substring(URL.lastIndexOf('.') + 1);
                            }

                            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                    DOWNLOAD_DIR, ext, responseBody, NAME);


                            //这里一定要注意判断，否则文件下载不全
                            if (task.isCancelled()) {
                                if (REQUEST != null) {
                                    REQUEST.onRequestEnd();
                                }
                            }
                        } else {
                            if (ERROR != null) {
                                ERROR.onError(response.code(), response.message());
                            }
                        }
                        HttpCreator.getParams().clear();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (FAILURE != null) {
                            FAILURE.onFailure( t);
                            HttpCreator.getParams().clear();
                        }
                    }
                });

    }
}
