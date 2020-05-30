package com.alliky.core.net;

import android.util.Log;

import com.alliky.core.config.ConfigKeys;
import com.alliky.core.config.Kylin;
import com.alliky.core.net.callback.HttpService;
import com.alliky.core.net.ssl.TrustAllCerts;
import com.alliky.core.net.ssl.TrustAllHostnameVerifier;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Author wxianing
 * date 2018/6/26
 */
public class HttpCreator {
    /**
     * 参数容器
     */
    private static final class ParamsHolder {
        private static final WeakHashMap<String, Object> PARAMS = new WeakHashMap<>();
        private static final WeakHashMap<String, String> HEADERS = new WeakHashMap<>();
    }

    public static WeakHashMap<String, Object> getParams() {
        return ParamsHolder.PARAMS;
    }

    public static WeakHashMap<String, String> getHeaders() {
        return ParamsHolder.HEADERS;
    }

    /**
     * 构建OkHttp
     */
    private static final class OKHttpHolder {
        //连接超时时间60s
        private static final int TIME_OUT = 30;

        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();

        private static final ArrayList<Interceptor> INTERCEPTORS = Kylin.getConfiguration(ConfigKeys.INTERCEPTOR);

        private static OkHttpClient.Builder addInterceptor() {

            BUILDER.sslSocketFactory(createSSLSocketFactory());
            BUILDER.hostnameVerifier(new TrustAllHostnameVerifier());

            if (INTERCEPTORS != null && !INTERCEPTORS.isEmpty()) {
                for (Interceptor interceptor : INTERCEPTORS) {
                    BUILDER.addInterceptor(interceptor);
                }
            }

            Interceptor httpResponseStatusInterceptor = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = chain.proceed(request);
                    try {
                        int code = response.code();
                        if (code != 200) {
                            String url = request.url().toString();
                            Log.d(code + "", url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response;
                }
            };

            BUILDER.addInterceptor(httpResponseStatusInterceptor);
            return BUILDER;

        }

        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }


    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ssfFactory;
    }

    /**
     * 构建全局Retrofit客户端
     */
    private static final class RetrofitHolder {
        private static final String BASE_URL = Kylin.getConfiguration(ConfigKeys.API_HOST);
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    /**
     * Service接口
     */
    private static final class HttpServiceHolder {
        private static final HttpService HTTP_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(HttpService.class);
    }

    public static HttpService getHttpService() {
        return HttpServiceHolder.HTTP_SERVICE;
    }
}
