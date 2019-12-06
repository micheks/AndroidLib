package com.alliky.core.net.callback;

import com.alliky.core.net.HttpMethod;
import com.alliky.core.net.common.Callback;
import com.alliky.core.net.params.RequestParams;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:31
 * http请求接口
 */
public interface HttpManager {
    /**
     * 异步GET请求
     */
    <T> Callback.Cancelable get(RequestParams entity, Callback.CommonCallback<T> callback);

    /**
     * 异步POST请求
     */
    <T> Callback.Cancelable post(RequestParams entity, Callback.CommonCallback<T> callback);

    /**
     * 异步请求
     */
    <T> Callback.Cancelable request(HttpMethod method, RequestParams entity, Callback.CommonCallback<T> callback);


    /**
     * 同步GET请求
     */
    <T> T getSync(RequestParams entity, Class<T> resultType) throws Throwable;

    /**
     * 同步POST请求
     */
    <T> T postSync(RequestParams entity, Class<T> resultType) throws Throwable;

    /**
     * 同步请求
     */
    <T> T requestSync(HttpMethod method, RequestParams entity, Class<T> resultType) throws Throwable;

    /**
     * 同步请求
     */
    <T> T requestSync(HttpMethod method, RequestParams entity, Callback.TypedCallback<T> callback) throws Throwable;
}
