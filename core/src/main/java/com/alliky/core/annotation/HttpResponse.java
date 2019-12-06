package com.alliky.core.annotation;

import com.alliky.core.net.app.ResponseParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:36
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpResponse {

    Class<? extends ResponseParser> parser();

}