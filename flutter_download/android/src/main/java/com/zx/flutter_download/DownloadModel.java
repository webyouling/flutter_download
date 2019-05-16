package com.zx.flutter_download;

import android.content.Context;

import java.util.List;

import other.DownLoadBean;
import other.DownloadBeanUtil;
import other.FileUtilities;
import other.M3U8DataBeanUtil;

/**
 * Created by lingjianzhong on 2017/12/19.
 * 视频下载业务处理类
 */

public class DownloadModel {
    /**
     * 获取所有视频
     */
    public static List<DownLoadBean> getVideoAll(Context context) {
        return DownloadBeanUtil.getDownLoadVideoAll(context);
    }
    /**
     * 获取重组后的数据(不包含广告) 除下载完的
     */
    public static List<DownLoadBean> getAllDownLoadVideo(Context context) {
        //获取数据库中所有的数据
        List<DownLoadBean> loadBeanList = DownloadBeanUtil.getDownLoad(context);
        return loadBeanList;
    }


    /**
     * 删除在下载列表中的所有视频
     */
    public static void deleteDownLoadingAll(Context context) {
        List<DownLoadBean> loadBeen = DownloadBeanUtil.getDownLoad(context);
        for (DownLoadBean bean : loadBeen) {
            FileUtilities.deleteFile(bean.path);//删除文件
        }
        //删除下载的除下载完成的
        DownloadBeanUtil.DeletDownLoad(context);
        //删除所有的
        M3U8DataBeanUtil.DeleteM3U8DownLoadAll(context);
    }
    /**
     * 删除所有视频
     */
    public static void deleteVideoAll(Context context) {
        List<DownLoadBean> loadBeen = DownloadBeanUtil.getDownLoadVideoAll(context);
        for (DownLoadBean bean : loadBeen) {
            FileUtilities.deleteFile(bean.path);//删除文件
            FileUtilities.deleteFile(bean.picture);
        }
        DownloadBeanUtil.DeleteDownLoadAll(context);
    }
    /**
     * 删除正在下载的视频
     */
    public static void deleteDownLoadingVideo(Context context, String id) {
        DownLoadBean loadBeen = DownloadBeanUtil.getDownLoadById(context, id);
        DownloadBeanUtil.DeleteDownLoadById(context, id);
        FileUtilities.deleteFile(loadBeen.path);//删除文件
    }
    /**
     * 更新视频数据
     */
    public static void UpdateDownLoadById(Context context, DownLoadBean bean) {
        DownloadBeanUtil.UpdateDownLoadById(context, bean);
    }

    /**
     * 根据视频url获取视频信息(是正在下载)
     */
    public static DownLoadBean findDownLoadByUrl(Context context, String url) {
        return DownloadBeanUtil.findDownLoadByUrl(context, url);
    }
    /**
     * 根据视频url获取视频信息(是下载完成)
     */
    public static DownLoadBean findVideoByUrl(Context context, String url) {
        return DownloadBeanUtil.findVideoByUrl(context, url);
    }
    /**
     * 条件查询
     */
    public static List<DownLoadBean> getVideoSaveVideoByConditionNoAdv(Context context, String condition) {
        return DownloadBeanUtil.getVideoSaveVideoByCondition(context, condition,true);
    }

    /**
     * 模糊查询+条件查询
     */
    public static List<DownLoadBean> getVideoSaveVideoLikeByConditionNoAdv(Context context, String search, String condition) {
        List<DownLoadBean> loadBeanList = DownloadBeanUtil.getVideoSaveVideoLikeByCondition(context, search, condition);
        return loadBeanList;
    }

}