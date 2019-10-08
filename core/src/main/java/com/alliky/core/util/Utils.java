package com.alliky.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.core.content.FileProvider;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: DOTO
 * @Author: wxianing
 * @CreateDate: 2019/10/8 14:44
 */
public final class Utils {
    @SuppressLint({"StaticFieldLeak"})
    private static Application sApplication;
    private static final Utils.ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new Utils.ActivityLifecycleImpl();

    private Utils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void init(Context context) {
        if (context == null) {
            init(getApplicationByReflect());
        } else {
            init((Application) context.getApplicationContext());
        }
    }

    public static void init(Application app) {
        if (sApplication == null) {
            if (app == null) {
                sApplication = getApplicationByReflect();
            } else {
                sApplication = app;
            }

            sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
        } else if (app != null && app.getClass() != sApplication.getClass()) {
            sApplication.unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
            ACTIVITY_LIFECYCLE.mActivityList.clear();
            sApplication = app;
            sApplication.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
        }

    }

    public static Application getApp() {
        if (sApplication != null) {
            return sApplication;
        } else {
            Application app = getApplicationByReflect();
            init(app);
            return app;
        }
    }

    private static Application getApplicationByReflect() {
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke((Object) null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }

            return (Application) app;
        } catch (NoSuchMethodException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        } catch (InvocationTargetException var5) {
            var5.printStackTrace();
        } catch (ClassNotFoundException var6) {
            var6.printStackTrace();
        }

        throw new NullPointerException("u should init first");
    }

    static Utils.ActivityLifecycleImpl getActivityLifecycle() {
        return ACTIVITY_LIFECYCLE;
    }

    static LinkedList<Activity> getActivityList() {
        return ACTIVITY_LIFECYCLE.mActivityList;
    }

    static Context getTopActivityOrApp() {
        if (isAppForeground()) {
            Activity topActivity = ACTIVITY_LIFECYCLE.getTopActivity();
            return (Context) (topActivity == null ? getApp() : topActivity);
        } else {
            return getApp();
        }
    }

    static boolean isAppForeground() {
        @SuppressLint("WrongConstant") ActivityManager am = (ActivityManager) getApp().getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> info = am.getRunningAppProcesses();
        if (info != null && info.size() != 0) {
            Iterator var2 = info.iterator();

            ActivityManager.RunningAppProcessInfo aInfo;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                aInfo = (ActivityManager.RunningAppProcessInfo) var2.next();
            } while (aInfo.importance != 100);

            return aInfo.processName.equals(getApp().getPackageName());
        } else {
            return false;
        }
    }

    public interface OnActivityDestroyedListener {
        void onActivityDestroyed(Activity var1);
    }

    public interface OnAppStatusChangedListener {
        void onForeground();

        void onBackground();
    }

    public static final class FileProvider4UtilCode extends FileProvider {
        public FileProvider4UtilCode() {
        }

        public boolean onCreate() {
            Utils.init(this.getContext());
            return true;
        }
    }

    static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {
        final LinkedList<Activity> mActivityList = new LinkedList();
        final HashMap<Object, OnAppStatusChangedListener> mStatusListenerMap = new HashMap();
        final HashMap<Activity, Set<OnActivityDestroyedListener>> mDestroyedListenerMap = new HashMap();
        private int mForegroundCount = 0;
        private int mConfigCount = 0;
        private boolean mIsBackground = false;

        ActivityLifecycleImpl() {
        }

        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            this.setTopActivity(activity);
        }

        public void onActivityStarted(Activity activity) {
            if (!this.mIsBackground) {
                this.setTopActivity(activity);
            }

            if (this.mConfigCount < 0) {
                ++this.mConfigCount;
            } else {
                ++this.mForegroundCount;
            }

        }

        public void onActivityResumed(Activity activity) {
            this.setTopActivity(activity);
            if (this.mIsBackground) {
                this.mIsBackground = false;
                this.postStatus(true);
            }

        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
            if (activity.isChangingConfigurations()) {
                --this.mConfigCount;
            } else {
                --this.mForegroundCount;
                if (this.mForegroundCount <= 0) {
                    this.mIsBackground = true;
                    this.postStatus(false);
                }
            }

        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        public void onActivityDestroyed(Activity activity) {
            this.mActivityList.remove(activity);
            this.consumeOnActivityDestroyedListener(activity);
        }

        Activity getTopActivity() {
            Activity topActivityByReflect;
            if (!this.mActivityList.isEmpty()) {
                topActivityByReflect = (Activity) this.mActivityList.getLast();
                if (topActivityByReflect != null) {
                    return topActivityByReflect;
                }
            }

            topActivityByReflect = this.getTopActivityByReflect();
            if (topActivityByReflect != null) {
                this.setTopActivity(topActivityByReflect);
            }

            return topActivityByReflect;
        }

        void addOnAppStatusChangedListener(Object object, Utils.OnAppStatusChangedListener listener) {
            this.mStatusListenerMap.put(object, listener);
        }

        void removeOnAppStatusChangedListener(Object object) {
            this.mStatusListenerMap.remove(object);
        }

        void removeOnActivityDestroyedListener(Activity activity) {
            if (activity != null) {
                this.mDestroyedListenerMap.remove(activity);
            }
        }

        void addOnActivityDestroyedListener(Activity activity, Utils.OnActivityDestroyedListener listener) {
            if (activity != null && listener != null) {
                Object listeners;
                if (!this.mDestroyedListenerMap.containsKey(activity)) {
                    listeners = new HashSet();
                    this.mDestroyedListenerMap.put(activity, (Set<OnActivityDestroyedListener>) listeners);
                } else {
                    listeners = (Set) this.mDestroyedListenerMap.get(activity);
                    if (((Set) listeners).contains(listener)) {
                        return;
                    }
                }

                ((Set) listeners).add(listener);
            }
        }

        private void postStatus(boolean isForeground) {
            if (!this.mStatusListenerMap.isEmpty()) {
                Iterator var2 = this.mStatusListenerMap.values().iterator();

                while (var2.hasNext()) {
                    Utils.OnAppStatusChangedListener onAppStatusChangedListener = (Utils.OnAppStatusChangedListener) var2.next();
                    if (onAppStatusChangedListener == null) {
                        return;
                    }

                    if (isForeground) {
                        onAppStatusChangedListener.onForeground();
                    } else {
                        onAppStatusChangedListener.onBackground();
                    }
                }

            }
        }

        private void setTopActivity(Activity activity) {
            if (!"com.blankj.utilcode.util.PermissionUtils$PermissionActivity".equals(activity.getClass().getName())) {
                if (this.mActivityList.contains(activity)) {
                    if (!((Activity) this.mActivityList.getLast()).equals(activity)) {
                        this.mActivityList.remove(activity);
                        this.mActivityList.addLast(activity);
                    }
                } else {
                    this.mActivityList.addLast(activity);
                }

            }
        }

        private void consumeOnActivityDestroyedListener(Activity activity) {
            Set<Map.Entry<Activity, Set<OnActivityDestroyedListener>>> entries = this.mDestroyedListenerMap.entrySet();
            Iterator var3 = entries.iterator();

            while (true) {
                Map.Entry entry;
                do {
                    if (!var3.hasNext()) {
                        return;
                    }

                    entry = (Map.Entry) var3.next();
                } while (entry.getKey() != activity);

                Set<Utils.OnActivityDestroyedListener> value = (Set) entry.getValue();
                Iterator var6 = value.iterator();

                while (var6.hasNext()) {
                    Utils.OnActivityDestroyedListener listener = (Utils.OnActivityDestroyedListener) var6.next();
                    listener.onActivityDestroyed(activity);
                }

                this.removeOnActivityDestroyedListener(activity);
            }
        }

        private Activity getTopActivityByReflect() {
            try {
                Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke((Object) null);
                Field activitiesField = activityThreadClass.getDeclaredField("mActivityList");
                activitiesField.setAccessible(true);
                Map activities = (Map) activitiesField.get(activityThread);
                if (activities == null) {
                    return null;
                }

                Iterator var5 = activities.values().iterator();

                while (var5.hasNext()) {
                    Object activityRecord = var5.next();
                    Class activityRecordClass = activityRecord.getClass();
                    Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        return (Activity) activityField.get(activityRecord);
                    }
                }
            } catch (ClassNotFoundException var10) {
                var10.printStackTrace();
            } catch (IllegalAccessException var11) {
                var11.printStackTrace();
            } catch (InvocationTargetException var12) {
                var12.printStackTrace();
            } catch (NoSuchMethodException var13) {
                var13.printStackTrace();
            } catch (NoSuchFieldException var14) {
                var14.printStackTrace();
            }

            return null;
        }
    }
}
