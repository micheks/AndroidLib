package com.alliky.core.net;

import com.alliky.core.ex.HttpException;
import com.alliky.core.net.callback.HttpRequestResponse;
import com.alliky.core.net.common.Callback;
import com.alliky.core.net.params.RequestParams;
import com.alliky.core.util.Logger;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 11:49
 */
public class HttpUtils {

    private static HttpUtils mInstance;

    private HttpUtils() {
        HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
    }

    static class NullHostNameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            Logger.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }
    }

    /**
     * ``
     * 单例模式
     *
     * @return
     */
    public static HttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new HttpUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送post请求
     */
    public void post( String url, HashMap<String, Object> map, final HttpRequestResponse mCallBack) {
//        CustomDialogUtils.showProgressDialog(mContext);
        RequestParams params = new RequestParams(url);
        params.addHeader("_version", "1.0.1");

//        params.addBodyParameter("content-type", "application/json");

        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }

        //判断请求是否为https
        if (url.trim().toLowerCase().startsWith("https:")) {
            /** 判断https证书是否成功验证 */
            SSLContext sslContext = getSSLContext();
            if (null == sslContext) {
                return;
            }
            params.setSslSocketFactory(sslContext.getSocketFactory()); //绑定SSL证书(https请求)
        }
        Utils.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    mCallBack.onSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {//网络异常
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 发送get请求
     */
    public void get(String url, Map<String, Object> map, final HttpRequestResponse mCallBack) {
        try {
            RequestParams params = new RequestParams(url);
            params.addHeader("_version", "1.0.1");
            if (null != map) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    params.addQueryStringParameter(entry.getKey(), entry.getValue().toString());
                }
            }
            //判断请求是否为https
            if (url.trim().toLowerCase().startsWith("https:")) {
                /** 判断https证书是否成功验证 */
                SSLContext sslContext = getSSLContext();
                if (null == sslContext) {
                    return;
                }
                params.setSslSocketFactory(sslContext.getSocketFactory()); //绑定SSL证书(https请求)
            }
            Utils.http().get(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    try {
                        mCallBack.onSuccess(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                }

                @Override
                public void onCancelled(CancelledException cex) {
                }

                @Override
                public void onFinished() {
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 上传文件
     */
    public void upLoadFile(final String url, HashMap<String, Object> map, HttpRequestResponse mCallBack) {
        RequestParams params = new RequestParams(url);

        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }

        Logger.i("upload_url", url);

        params.setMultipart(true);

        //判断请求是否为https
        if (url.trim().toLowerCase().startsWith("https:")) {
            /** 判断https证书是否成功验证 */
            SSLContext sslContext = getSSLContext();
            if (null == sslContext) {
                return;
            }
            params.setSslSocketFactory(sslContext.getSocketFactory()); //绑定SSL证书(https请求)
        }

        Utils.http().post(params, new Callback.ProgressCallback<String>() {
            @Override
            public void onWaiting() {
                if (mCallBack != null) {
                    mCallBack.onWaiting();
                }
            }

            @Override
            public void onStarted() {
                if (mCallBack != null) {
                    mCallBack.onStarted();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                Logger.i("UploadMessage:", "total:" + total + "  " + "current:" + current);
                if (mCallBack != null) {
                    mCallBack.onLoading(total, current, isDownloading);
                }
            }

            @Override
            public void onSuccess(String result) {
                Logger.i("onSuccess", result);
                if (mCallBack != null) {
                    mCallBack.onSuccess(result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Logger.i("onError", "onError:>>>>>>>> " + ex.getMessage());
                if (ex instanceof HttpException) {//网络异常
                    if (mCallBack != null) {
                        mCallBack.onError(ex, isOnCallback);
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Logger.i("onCancelled", "onCancelled>>>>>>>>>");
                if (mCallBack != null) {
                    mCallBack.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (mCallBack != null) {
                    mCallBack.onFinished();
                }
            }
        });
    }

    /**
     * @param url      下载地址
     * @param filepath 保存路径
     */
    public void downLoadFile(final String url, String filepath, final HttpRequestResponse<File> mCallBack) {
        RequestParams params = new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);

        //判断请求是否为https
        if (url.trim().toLowerCase().startsWith("https:")) {
            /** 判断https证书是否成功验证 */
            SSLContext sslContext = getSSLContext();
            if (null == sslContext) {
                return;
            }
            params.setSslSocketFactory(sslContext.getSocketFactory()); //绑定SSL证书(https请求)
        }


        Utils.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                mCallBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {//网络异常
                    if (mCallBack != null) {
                        mCallBack.onError(ex, isOnCallback);
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (mCallBack != null) {
                    mCallBack.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (mCallBack != null) {
                    mCallBack.onFinished();
                }
            }

            @Override
            public void onWaiting() {
                if (mCallBack != null) {
                    mCallBack.onWaiting();
                }
            }

            @Override
            public void onStarted() {
                if (mCallBack != null) {
                    mCallBack.onStarted();
                }
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                if (mCallBack != null) {
                    mCallBack.onLoading(total, current, isDownloading);
                }
            }
        });
    }

    /**
     * 获取Https的证书
     *
     * @return SSL的上下文对象
     */
    private static SSLContext getSSLContext() {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //信任所有证书 （官方不推荐使用）
            sslContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
            }}, new SecureRandom());
            return sslContext;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
