package com.alliky.core.net.app;

import com.alliky.core.annotation.HttpRequest;
import com.alliky.core.net.params.RequestParams;

import javax.net.ssl.SSLSocketFactory;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:26
 */
public interface ParamsBuilder {

    /**
     * 根据@HttpRequest构建请求的url
     */
    String buildUri(RequestParams params, HttpRequest httpRequest) throws Throwable;

    /**
     * 根据注解的cacheKeys构建缓存的自定义key,
     * 如果返回为空, 默认使用 url 和整个 query string 组成.
     */
    String buildCacheKey(RequestParams params, String[] cacheKeys);

    /**
     * 自定义SSLSocketFactory
     */
    SSLSocketFactory getSSLSocketFactory() throws Throwable;

    /**
     * 为请求添加通用参数等操作
     */
    void buildParams(RequestParams params) throws Throwable;

    /**
     * 自定义参数签名
     */
    void buildSign(RequestParams params, String[] signs) throws Throwable;
}
