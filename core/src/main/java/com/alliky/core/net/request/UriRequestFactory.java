package com.alliky.core.net.request;

import android.text.TextUtils;

import com.alliky.core.net.app.RequestTracker;
import com.alliky.core.net.params.RequestParams;
import com.alliky.core.util.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:42
 */
public final class UriRequestFactory {

    private static Class<? extends RequestTracker> defaultTrackerCls;

    private static final HashMap<String, Class<? extends UriRequest>>
            SCHEME_CLS_MAP = new HashMap<String, Class<? extends UriRequest>>();

    private UriRequestFactory() {
    }

    public static UriRequest getUriRequest(RequestParams params, Type loadType) throws Throwable {

        // get scheme
        String scheme = null;
        String uri = params.getUri();
        int index = uri.indexOf(":");
        if (uri.startsWith("/")) {
            scheme = "file";
        } else if (index > 0) {
            scheme = uri.substring(0, index);
        }

        // get UriRequest
        if (!TextUtils.isEmpty(scheme)) {
            scheme = scheme.toLowerCase();
            Class<? extends UriRequest> cls = SCHEME_CLS_MAP.get(scheme);
            if (cls != null) {
                Constructor<? extends UriRequest> constructor
                        = cls.getConstructor(RequestParams.class, Type.class);
                return constructor.newInstance(params, loadType);
            } else {
                if (scheme.startsWith("http")) {
                    return new HttpRequest(params, loadType);
                } else if (scheme.equals("assets")) {
                    return new AssetsRequest(params, loadType);
                } else if (scheme.equals("file")) {
                    return new LocalFileRequest(params, loadType);
                } else if (scheme.equals("res")) {
                    return new ResRequest(params, loadType);
                } else {
                    throw new IllegalArgumentException("The url not be support: " + uri);
                }
            }
        } else {
            throw new IllegalArgumentException("The url not be support: " + uri);
        }
    }

    public static void registerDefaultTrackerClass(Class<? extends RequestTracker> trackerCls) {
        UriRequestFactory.defaultTrackerCls = trackerCls;
    }

    public static RequestTracker getDefaultTracker() {
        try {
            return defaultTrackerCls == null ? null : defaultTrackerCls.newInstance();
        } catch (Throwable ex) {
            Logger.e(ex.getMessage(), ex.getMessage());
        }
        return null;
    }

    public static void registerRequestClass(String scheme, Class<? extends UriRequest> uriRequestCls) {
        SCHEME_CLS_MAP.put(scheme, uriRequestCls);
    }
}
