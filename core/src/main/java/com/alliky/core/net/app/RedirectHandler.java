package com.alliky.core.net.app;

import com.alliky.core.net.request.UriRequest;
import com.alliky.core.net.params.RequestParams;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:52
 */
public  interface RedirectHandler {

    /**
     * 根据请求信息返回自定义重定向的请求参数
     *
     * @param request 原始请求
     * @return 返回不为null时进行重定向
     */
    RequestParams getRedirectParams(UriRequest request) throws Throwable;
}
