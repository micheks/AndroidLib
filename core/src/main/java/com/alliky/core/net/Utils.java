package com.alliky.core.net;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.alliky.core.db.DbManager;
import com.alliky.core.db.DbManagerImpl;
import com.alliky.core.ex.DbException;
import com.alliky.core.net.callback.HttpManager;
import com.alliky.core.net.common.TaskController;
import com.alliky.core.net.common.task.TaskControllerImpl;

import java.lang.reflect.Method;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/12/6 9:17
 */
public class Utils {

    private Utils() {
    }

    public static boolean isDebug() {
        return Ext.debug;
    }

    public static Application app() {
        if (Ext.app == null) {
            try {
                // 仅在IDE进行布局预览时使用，真机或模拟器不使用MockApplication.
                @SuppressLint("PrivateApi")
                Class<?> renderActionClass = Class.forName("com.android.layoutlib.bridge.impl.RenderAction");
                Method method = renderActionClass.getDeclaredMethod("getCurrentContext");
                Context context = (Context) method.invoke(null);
                Ext.app = new MockApplication(context);
            } catch (Throwable ignored) {
                throw new RuntimeException("please invoke Utils.Ext.init(app) on Application#onCreate()"
                        + " and register your Application in manifest.");
            }
        }
        return Ext.app;
    }

    public static TaskController task() {
        return Ext.taskController;
    }

    public static DbManager getDb(DbManager.DaoConfig daoConfig) throws DbException {
        return DbManagerImpl.getInstance(daoConfig);
    }

    public static HttpManager http() {
        if (Ext.httpManager == null) {
            HttpManagerImpl.registerInstance();
        }
        return Ext.httpManager;
    }

    public static class Ext {
        private static boolean debug;
        private static Application app;
        private static TaskController taskController;
        private static HttpManager httpManager;

        private Ext() {
        }

        public static void init(Application app) {
            TaskControllerImpl.registerInstance();
            if (Ext.app == null) {
                Ext.app = app;
            }
        }

        public static void setDebug(boolean debug) {
            Ext.debug = debug;
        }

        public static void setTaskController(TaskController taskController) {
            if (Ext.taskController == null) {
                Ext.taskController = taskController;
            }
        }

        public static void setHttpManager(HttpManager httpManager) {
            Ext.httpManager = httpManager;
        }


        public static void setDefaultHostnameVerifier(HostnameVerifier hostnameVerifier) {
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        }
    }

    private static class MockApplication extends Application {
        public MockApplication(Context baseContext) {
            this.attachBaseContext(baseContext);
        }
    }
}
