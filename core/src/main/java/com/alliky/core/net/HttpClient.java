package com.alliky.core.net;

import android.content.Context;

import com.alliky.core.config.Kylin;
import com.alliky.core.net.callback.HttpService;
import com.alliky.core.net.callback.ICancelled;
import com.alliky.core.net.callback.IError;
import com.alliky.core.net.callback.IFSuccess;
import com.alliky.core.net.callback.IFailure;
import com.alliky.core.net.callback.IFinished;
import com.alliky.core.net.callback.ILoading;
import com.alliky.core.net.callback.IRequest;
import com.alliky.core.net.callback.IStarted;
import com.alliky.core.net.callback.ISuccess;
import com.alliky.core.net.callback.IWaiting;
import com.alliky.core.net.download.DownloadHandler;
import com.alliky.core.net.download.DownloadHelper;
import com.alliky.core.net.loader.Loader;
import com.alliky.core.net.loader.LoaderStyle;
import com.alliky.core.net.upload.UploadHelper;
import com.alliky.core.util.NetUtil;
import com.alliky.core.util.Toasty;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Author wxianing
 * date 2018/6/26
 */
public final class HttpClient {
    //参数
    private static final WeakHashMap<String, Object> PARAMS = HttpCreator.getParams();
    //请求头
    private static final WeakHashMap<String, String> HEADERS = HttpCreator.getHeaders();
    //URL
    private final String URL;
    private final IRequest REQUEST;
    private final String DOWNLOAD_DIR;
    private final String EXTENSION;
    private final String NAME;
    private final String SAVEPATH;


    private final ISuccess SUCCESS;
    private final IFSuccess<File> FSUCCESS;
    private final ILoading LOADING;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final ICancelled CANCELLED;
    private final IFinished FINISHED;
    private final IWaiting WAITING;
    private final IStarted STARTED;

    private final RequestBody BODY;
    private final LoaderStyle LOADER_STYLE;
    private final boolean CANCELABLE;
    private final File FILE;
    private final Context CONTEXT;

    HttpClient(String url,
               Map<String, Object> params,
               Map<String, String> headers,
               String downloadDir,
               String extension,
               String name,
               String savePath,

               IRequest request,
               ISuccess success,
               IFSuccess<File> fsuccess,

               ILoading loading,
               IFailure failure,
               IError error,
               ICancelled cancelled,
               IFinished finished,
               IWaiting waiting,
               IStarted started,
               RequestBody body,
               File file,
               Context context,
               boolean cancelable,
               LoaderStyle loaderStyle) {
        this.URL = url;
        PARAMS.putAll(params);
        HEADERS.putAll(headers);
        this.DOWNLOAD_DIR = downloadDir;
        this.EXTENSION = extension;
        this.NAME = name;
        this.REQUEST = request;
        this.SUCCESS = success;
        this.LOADING = loading;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
        this.FILE = file;
        this.CANCELABLE = cancelable;
        this.CONTEXT = context;
        this.LOADER_STYLE = loaderStyle;
        this.FSUCCESS = fsuccess;
        this.CANCELLED = cancelled;
        this.FINISHED = finished;
        this.WAITING = waiting;
        this.STARTED = started;
        this.SAVEPATH = savePath;
    }

    /**
     * 建造者创建HttpClientBuilder对象
     *
     * @return
     */
    public static HttpClientBuilder builder() {
        return new HttpClientBuilder();
    }

    private void request(HttpMethod method) {
        final HttpService service = HttpCreator.getHttpService();
        Call<String> call = null;

        if (REQUEST != null) {
            REQUEST.onRequestStart();
        }

        if (CONTEXT != null) {
            if (LOADER_STYLE != null) {
                Loader.showLoading(CONTEXT, LOADER_STYLE, CANCELABLE);
            }
        }

//        if (null != HEADERS) {
//            HEADERS.put("token", Const.token());
//            HEADERS.put("_version", AppUtil.getVersionName(Kylin.getApplicationContext()));
//            HEADERS.put("platform", "android");
//        }


        if (!NetUtil.isConnected(Kylin.getApplicationContext())) {
            Toasty.normal(Kylin.getApplicationContext(), "当前网络连接不可用，请检查网络连接状态再重试！");
        }

        switch (method) {
            case GET:
                call = service.get(URL, PARAMS, HEADERS);
                break;
            case POST:
                call = service.post(URL, PARAMS, HEADERS);
                break;
            case POST_RAW:
                call = service.postRaw(URL, BODY, HEADERS);
                break;
            case PUT:
                call = service.put(URL, PARAMS, HEADERS);
                break;
            case PUT_RAW:
                call = service.putRaw(URL, BODY, HEADERS);
                break;
            case DELETE:
                call = service.delete(URL, PARAMS, HEADERS);
                break;
            case UPLOAD:


                final RequestBody requestBody =
                        RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body =
                        MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);


                call = service.upload(URL, body, HEADERS);


                break;
            default:
                break;
        }

        if (call != null) {
            call.enqueue(getRequestCallback());
            PARAMS.clear();
        }
    }

    private Callback<String> getRequestCallback() {
        return new RequestCallbacks(
                URL,
                PARAMS,
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR,
                LOADER_STYLE
        );
    }

    public final void get() {
        request(HttpMethod.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HttpMethod.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.POST_RAW);
        }
    }

    public final void put() {
        if (BODY == null) {
            request(HttpMethod.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("params must be null!");
            }
            request(HttpMethod.PUT_RAW);
        }
    }

    public final void delete() {
        request(HttpMethod.DELETE);
    }

    public final void upload() {
//        request(HttpMethod.UPLOAD);


        new UploadHelper(URL,PARAMS,HEADERS,SUCCESS,CANCELLED,FINISHED,WAITING,STARTED,ERROR,LOADING).upLoadFile();

    }

    public final void download() {
//        new DownloadHandler(URL, REQUEST, DOWNLOAD_DIR, EXTENSION, NAME,
//                SUCCESS, LOADING, FAILURE, ERROR)
//                .handleDownload();

        new DownloadHelper(URL, PARAMS,HEADERS,SAVEPATH,FSUCCESS, CANCELLED, FINISHED, WAITING, STARTED, ERROR, LOADING).downLoadFile();
    }
}
