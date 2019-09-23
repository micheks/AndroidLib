package com.alliky.core.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.alliky.core.config.Kylin;

/**
 * Author wxianing
 * date 2018/6/26
 */
public final class DimenUtil {

    public static int getScreenWidth() {
        final Resources resources = Kylin.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Kylin.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
