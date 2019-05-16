package other;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;




import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * Created by lingjianzhong on 2017/12/15.
 * 获取app应用信息
 */

public class AppInfoUtils {
    /**
     * 根据字符串获取资源id
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 获取应用名
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        PackageManager manager = context.getPackageManager();
        ApplicationInfo appLicationInfo = null;
        try {
            appLicationInfo = manager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) manager.getApplicationLabel(appLicationInfo);
    }

    /**
     * 获取包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        if (context == null)
            return "";
        PackageManager manager = context.getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String versionName = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 判断某个应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName.trim()
                    , PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            if (packageInfo != null) {
                // 说明某个应用使用了该包名
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {

            return false;
        }
        return false;
    }

    /**
     * 获取系统语言版本
     *
     * @param context
     * @return
     */
    public static String getLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }


    /**
     * 获取当前系统android 版本号
     *
     * @return
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    private static UsageStatsManager sUsageStatsManager = null;

    /**
     * 获取栈顶元素
     *
     * @param context
     * @return
     */
    @SuppressLint("ObsoleteSdkInt")
    public static String getLauncherTopApp(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> appTasks = activityManager.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks.get(0).topActivity.getPackageName();
            }
        } else {
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            if (sUsageStatsManager == null) {
                sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            }
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event);
                if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.getPackageName();
                }
            }
            if (!android.text.TextUtils.isEmpty(result)) {
                return result;
            }
        }
        return "";
    }


    /**
     * 获取随机色
     *
     * @return
     */
    public static int getRandomColor() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE & 0x00ffffff) | 0xff000000;
    }
}
