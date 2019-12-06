package com.alliky.core.net.app;

import android.text.TextUtils;
import android.webkit.URLUtil;

import com.alliky.core.net.HttpMethod;
import com.alliky.core.net.request.HttpRequest;
import com.alliky.core.net.request.UriRequest;
import com.alliky.core.net.params.RequestParams;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:51
 */
public class DefaultRedirectHandler implements RedirectHandler {
    @Override
    public RequestParams getRedirectParams(UriRequest request) throws Throwable {
        if (request instanceof HttpRequest) {
            HttpRequest httpRequest = (HttpRequest) request;
            RequestParams params = httpRequest.getParams();
            String location = httpRequest.getResponseHeader("Location");
            if (!TextUtils.isEmpty(location)) {
                if (!URLUtil.isHttpsUrl(location) && !URLUtil.isHttpUrl(location)) {
                    String url = params.getUri();
                    if (location.startsWith("/")) {
                        int pathIndex = url.indexOf("/", 8);
                        if (pathIndex != -1) {
                            url = url.substring(0, pathIndex);
                        }
                    } else {
                        int pathIndex = url.lastIndexOf("/");
                        if (pathIndex >= 8) {
                            url = url.substring(0, pathIndex + 1);
                        } else {
                            url += "/";
                        }
                    }
                    location = url + location;
                }
                params.setUri(location);


                /* http 1.0 301 302
                 * http 1.1 303 307 308
                 */
                int code = request.getResponseCode();
                if (code == 301 || code == 302 || code == 303) {
                    params.clearParams();
                    params.setMethod(HttpMethod.GET);
                } /*else if (code == 307 || code == 308) {
                    // don't change the request method or params
                }*/

                return params;
            }
        }

        return null;
    }
}