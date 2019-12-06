package com.alliky.core.net.app;

import java.lang.reflect.Type;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:04
 */
public interface ResponseParser <ResponseDataType> extends RequestInterceptListener {

    /**
     * 转换result为resultType类型的对象
     *
     * @param resultType  返回值类型(可能带有泛型信息)
     * @param resultClass 返回值类型
     * @param result      网络返回数据(支持String, byte[], JSONObject, JSONArray, InputStream)
     * @return 请求结果, 类型为resultType
     */
    Object parse(Type resultType, Class<?> resultClass, ResponseDataType result) throws Throwable;
}
