package com.alliky.core.annotation;

import com.alliky.core.net.app.DefaultParamsBuilder;
import com.alliky.core.net.app.ParamsBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 10:25
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRequest {

    String host() default "";

    String path();

    Class<? extends ParamsBuilder> builder() default DefaultParamsBuilder.class;

    String[] signs() default "";

    String[] cacheKeys() default "";
}
