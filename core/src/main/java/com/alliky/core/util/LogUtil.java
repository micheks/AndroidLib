package com.alliky.core.util;


/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/9/29 17:28
 */
public final class LogUtil {

    private static final String TAG = "dy_log";
    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    //控制log等级
    private static int LEVEL = VERBOSE;

    public static void v(String tag, String message) {

        if (LEVEL <= VERBOSE) {
            com.orhanobut.logger.Logger.t(tag).v(message);
        }
    }

    public static void v(String message) {

        if (LEVEL <= VERBOSE) {
            com.orhanobut.logger.Logger.t(TAG).v(message);
        }
    }

    public static void d(String tag, Object message) {
        if (LEVEL <= DEBUG) {
            com.orhanobut.logger.Logger.t(tag).d(message);
        }
    }

    public static void d(Object message) {
        if (LEVEL <= DEBUG) {
            com.orhanobut.logger.Logger.d(message);
        }
    }

    public static void i(String tag, String message) {
        if (LEVEL <= INFO) {
            com.orhanobut.logger.Logger.t(tag).i(message);
        }
    }

    public static void i(String message) {
        if (LEVEL <= INFO) {
            com.orhanobut.logger.Logger.t(TAG).i(message);
        }
    }

    public static void w(String tag, String message) {
        if (LEVEL <= WARN) {
            com.orhanobut.logger.Logger.t(tag).w(message);
        }
    }

    public static void w(String message) {
        if (LEVEL <= WARN) {
            com.orhanobut.logger.Logger.t(TAG).w(message);
        }
    }

    public static void json(String tag, String message) {
        if (LEVEL <= WARN) {
            com.orhanobut.logger.Logger.t(tag).json(message);
        }
    }

    public static void json(String message) {
        if (LEVEL <= WARN) {
            com.orhanobut.logger.Logger.t(TAG).json(message);
        }
    }

    public static void e(String tag, String message) {
        if (LEVEL <= ERROR) {
            com.orhanobut.logger.Logger.t(tag).e(message);
        }
    }

    public static void e(String message) {
        if (LEVEL <= ERROR) {
            com.orhanobut.logger.Logger.t(TAG).e(message);
        }
    }

    public static void wtf(String tag, String message) {
        if (LEVEL <= ERROR) {
            com.orhanobut.logger.Logger.t(tag).wtf(message);
        }
    }

    public static void wtf(String message) {
        if (LEVEL <= ERROR) {
            com.orhanobut.logger.Logger.t(TAG).wtf(message);
        }
    }
}
