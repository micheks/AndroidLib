package com.alliky.core.net.parser;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

/**
 * @Description TODO
 * @Author wxianing
 * @Date 2020/4/14
 */
public class JsonResponseParser<T> {

    public AppBean parse(String result) {

        AppBean<T> appBean = JSONObject.parseObject(result, new TypeReference<AppBean<T>>() {
        });

        return appBean;

    }
}
