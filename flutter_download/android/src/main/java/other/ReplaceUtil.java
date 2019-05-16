package other;

import android.content.Context;

/**
 * Created by lingjianzhong on 2018/1/7.
 * 关键字替换
 */

public class ReplaceUtil {
    /**
     * 封面关键字替换
     *
     * @param context
     * @param content
     * @return
     */
    public static String replaceCover(Context context, String content) {
        String language = AppInfoUtils.getLanguage(context);
        if (language.toLowerCase().contains("zh")) {
            //简体中文
            return content.replace("封面", "下载");
        } else if (language.toLowerCase().contains("ja")) {
            //日语
            return content.replace("大象", "ダウンロード");
        } else {
            //英语
            return content.replace("封面", "Download");
        }
    }

    /**
     * 运行关键字替换
     *
     * @param context
     * @param content
     * @return
     */
    public static String replaceRun(Context context, String content) {
        String language = AppInfoUtils.getLanguage(context);
        if (language.toLowerCase().contains("zh")) {
            //简体中文
            return content.replace("运行", "缓存");
        } else if (language.toLowerCase().contains("ja")) {
            //日语
            return content.replace("运行", "キャッシユ");
        } else {
            //英语
            return content.replace("运行", "Cach");
        }
    }

    /**
     * 警告关键字替换
     *
     * @param context
     * @param content
     * @return
     */
    public static String replaceCaveat(Context context, String content) {
        String language = AppInfoUtils.getLanguage(context);
        if (language.toLowerCase().contains("zh")) {
            //简体中文
            return content.replace("警告", "保存");
        } else if (language.toLowerCase().contains("ja")) {
            //日语
            return content.replace("警告", "追加");
        } else {
            //英语
            return content.replace("警告", "Save");
        }
    }

    /**
     * 禁止关键字替换
     *
     * @param context
     * @param content
     * @return
     */
    public static String replaceProhibited(Context context, String content) {
        String language = AppInfoUtils.getLanguage(context);
//        Loger.e("当前语言 language "+language);
        if (language.toLowerCase().contains("zh")) {
            //简体中文
            return content.replace("禁止", "保存");
        } else if (language.toLowerCase().contains("ja")) {
            //日语
            return content.replace("禁止", "保存");
        } else {
            //英语
            return content.replace("禁止", "Save");
        }
    }

    /**
     * 封面关键字替换
     */
    public static String replaceFail(Context context, String content) {
        String language = AppInfoUtils.getLanguage(context);
        if (language.toLowerCase().contains("zh")) {
            //简体中文
            return content.replace("封面", "保存");
        } else if (language.toLowerCase().contains("ja")) {
            //日语
            return content.replace("禁止", "保存");
        } else {
            //英语
            return content.replace("封面", "Save");
        }
    }

    /**
     * 奇葩关键字替换
     *
     * @param context
     * @param content
     * @return
     */
    public static String replaceWonderfulWork(Context context, String content) {
        String language = AppInfoUtils.getLanguage(context);
        if (language.toLowerCase().contains("zh")) {
            //简体中文
            return content.replace("奇葩", "5星");
        } else if (language.toLowerCase().contains("ja")) {
            //日语
            return content.replace("奇葩", "★５");
        } else {
            //英语
            return content.replace("奇葩", "5 stars");
        }
    }
}
