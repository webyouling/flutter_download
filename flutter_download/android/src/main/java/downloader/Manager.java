package downloader;

import android.annotation.SuppressLint;
import android.app.Activity;
import downloader.internal.DownloadRequestQueue;
import downloader.notification.DLNotificationManager;
import downloader.request.DownloadRequest;
import downloader.utils.Utils;
import other.Constants;
import other.DownLoadBean;
import other.DownLoadState;
import other.DownloadBeanUtil;
import other.FileUtilities;
import other.Loger;
import other.M3U8DataBeanUtil;
import other.M3U8Ts;
import other.VideoUtils;

import java.io.File;
import java.util.List;


/**
 * Created by ${张奎勋} on 2018/3/7.
 */

public class Manager {

    //应用是否进入后台
    public static boolean isBackstage = false;
    @SuppressLint("StaticFieldLeak")
    private static Manager instance;

    private DLNotificationManager mDLNotificationManager;
    private DownloadRequest mBuild;
    private Activity mActivity;


    private Manager(Activity activity) {
        mActivity = activity;
        mDLNotificationManager = new DLNotificationManager(mActivity);
    }

    public static Manager getInstance(Activity activity) {
        if (instance == null) {
            synchronized (Manager.class) {
                if (instance == null) {
                    instance = new Manager(activity);
                }
            }
        }
        return instance;
    }

    public Manager buildStart(String videoUrl, String Web_title, String webUrl) {
        String tsPath = Constants.M3U8TempDir + String.valueOf(System.currentTimeMillis());
        mBuild =PRDownloader.download(videoUrl, tsPath, String.valueOf(System.currentTimeMillis()))
                .build(); 
        mBuild.url = videoUrl;
        mBuild.path = Constants.PATH_BASE;
        mBuild.name = Web_title + ".mp4";
        mBuild.web_url = webUrl;
        mBuild.downloadId = Utils.getUniqueId(videoUrl, Constants.PATH_BASE, mBuild.videoName);
        DownloadBeanUtil.insertDown(mActivity, mBuild);

        return this;
    }

    public Manager buildResume(DownLoadBean bean) {
        String tsPath = Constants.M3U8TempDir + String.valueOf(System.currentTimeMillis());
        mBuild = downloader.PRDownloader.download(bean.url, tsPath, String.valueOf(System.currentTimeMillis()))
                .build();
        mBuild.id = bean.id;
        mBuild.url = bean.url;
        mBuild.path = Constants.PATH_BASE;
        mBuild.name = bean.name;
        mBuild.videoName = bean.videoName;
        mBuild.web_url = bean.web_url;
        mBuild.totalSize = bean.totalSize;
        mBuild.currentSize = bean.currentSize;
        mBuild.currentTsIndex = bean.currentTsIndex;
        mBuild.downloadState = bean.downloadState;
        mBuild.downloadId = bean.downloadId;
        return this;
    }

    public void startDownload() {
        final DownloadRequest request = mBuild.setOnStartOrResumeListener(new OnStartOrResumeListener() {
            @Override
            public void onStartOrResume() {
                Loger.e("开始下载");
                mDLNotificationManager.showProgress(mBuild, true);
            }
        }).setOnProgressListener(new OnProgressListener() {
            @Override
            public void onProgress(int downloadId) {

                mDLNotificationManager.updateProgress();
            }

            @Override
            public void onOtherUpdate(int downloadId) {
                Loger.e("------onOtherUpdate------");
                mDLNotificationManager.updateProgress();
            }
        }).setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                Loger.e("--暂停");
            }
        }).setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel() {
                Loger.e("--清除");
                mDLNotificationManager.showProgress(mBuild, false);
            }
        }).setOnChangeThreadListener(new OnChangeThreadListener() {
            @Override
            public void onChangeThread(int downloadId) {
                Loger.e("--改变线程");
            }
        }).start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete(int downloadId) {
                Loger.e("---下载成功=" + downloadId);
                downloadSuccess(downloadId);
            }

            @Override
            public void onError(Error error, int downloadId) {
                Loger.e("---下载失败");
                DownloadRequest downloadRequest = DownloadRequestQueue.getInstance(mActivity).getCurrentRequestMap().get(downloadId);
                if (downloadRequest == null)
                    return;
            }
        });
    }

    /**
     * 视频下载完成的操作
     */
    private void downloadSuccess(int downloadId) {
        DownLoadBean downloadRequest = DownloadRequestQueue.getInstance(mActivity).getCurrentRequestMap().get(downloadId);

        //取出下载请求为null的话,从数据库重组一个请求
        if (null == downloadRequest) {
            downloadRequest = DownloadBeanUtil.getDownLoadByDownloadId(mActivity, String.valueOf(downloadId));
            if (null != downloadRequest) {
                downloadRequest.downloadState = DownLoadState.STATE_DOWNLOADED;
                downloadRequest.time = String.valueOf(System.currentTimeMillis());
                downloadRequest.type = 1;
                downloadRequest.path = downloadRequest.path + File.separator + downloadRequest.videoName + ".mp4";
                downloadRequest.picture = VideoUtils.getVideoPicture(downloadRequest.path, downloadRequest.id);
                downloadRequest.videoTime = VideoUtils.getDuration(downloadRequest.path);
                if ("null".equals(downloadRequest.icon)) {
                    downloadRequest.icon = null;
                }

                List<M3U8Ts> m3List = M3U8DataBeanUtil.getM3U8DownLoad(mActivity, downloadRequest.url);
                Loger.d("");
                if (m3List.size() > 0) {
                    M3U8Ts m3 = m3List.get(m3List.size() - 1);
                    downloadRequest.tsPath = m3.getTsPath();
                }
            }
        }

        if (null == downloadRequest) {
            return;
        }
        if (mDLNotificationManager == null)
            mDLNotificationManager = new DLNotificationManager(mActivity);
        mDLNotificationManager.updateTitle();
        mDLNotificationManager.suspension(downloadRequest.name);

        if (downloadRequest.fileFormat != null) {
            if (downloadRequest.fileFormat.equals(Constants.M3U8)) {
                FileUtilities.deleteDirectory(downloadRequest.tsPath);//删除ts临时文件
                M3U8DataBeanUtil.DeleteM3U8DownLoadById(mActivity, downloadRequest.url);//删除ts表
            }
        }
        DownloadBeanUtil.UpdateDownLoadById(mActivity, downloadRequest);//更新下载表

    }
}