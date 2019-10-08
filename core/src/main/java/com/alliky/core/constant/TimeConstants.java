package com.alliky.core.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:50
 */
public final class TimeConstants {
    public static final int MSEC = 1;
    public static final int SEC = 1000;
    public static final int MIN = 60000;
    public static final int HOUR = 3600000;
    public static final int DAY = 86400000;

    public TimeConstants() {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Unit {
    }
}
