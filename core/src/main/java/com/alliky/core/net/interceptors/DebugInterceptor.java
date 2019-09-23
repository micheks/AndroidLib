package com.alliky.core.net.interceptors;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.alliky.core.util.FileUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Author wxianing
 * date 2018/6/26
 */
public class DebugInterceptor extends BaseInterceptor {
    private final String DEBUG_URL;
    private final int DEBUG_RAW_ID;

    public DebugInterceptor(String debugUrl, int rawId) {
        this.DEBUG_URL = debugUrl;
        this.DEBUG_RAW_ID = rawId;
    }

    private Response getResponse(Interceptor.Chain chain, String json) {
        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    private Response debugResponse(Interceptor.Chain chain, @RawRes int rawId) {
        final String json = FileUtil.getRawFile(rawId);
        return getResponse(chain, json);
    }

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        if (null != chain) {

            final String url = chain.request().url().toString();
            if (url.contains(DEBUG_URL)) {
                return debugResponse(chain, DEBUG_RAW_ID);
            }
            return chain.proceed(chain.request());
        }
        return null;
    }
}
