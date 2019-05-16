/*
 *    Copyright (C) 2016 Tamic
 *
 *    link :https://github.com/Tamicer/FastDownloader
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package downloader.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import android.widget.RemoteViews;

import downloader.core.Core;
import downloader.request.DownloadRequest;

/**
 * 通知管理器
 */
public class DLNotificationManager {
    /**
     * 下载进度通知ID
     */
    private static final int PROGRESS_ID = 0x123;
    private static final String PROGRESS_CHANNEL_ID = String.valueOf(PROGRESS_ID);
    /**
     * 下载结果通知ID
     */
    private static final int RESULT_ID = 4512;
    private static final String CHANNEL_ID = String.valueOf(RESULT_ID);
    /**
     * 通知刷新间隔
     */
    private static final int SHOW_INTEVAL = 1000;
    /**
     * context
     */
    private Context mContext;
    /**
     * notification manager
     */
    private NotificationManager mNotificationManager;
    /**
     * 上次更新时间
     */
    private long mLastRefresh;
    /**
     * notification
     */
    private Notification mNotification;
    private DownloadRequest mRequest;
    private RemoteViews mContentView;

    public DLNotificationManager(Context context) {
        mContext = context.getApplicationContext();

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mLastRefresh = 0;
    }

    /**
     * 取消所有通知
     */
    public void cancelAll() {
        mNotificationManager.cancel(PROGRESS_ID);
        mNotificationManager.cancel(RESULT_ID);
    }

    /**
     * 取消进度通知
     */
    public void cancelProgress() {
        mNotificationManager.cancel(PROGRESS_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.deleteNotificationChannel(PROGRESS_CHANNEL_ID);
        }
    }

    /**
     * 显示进度通知
     */
    public synchronized void showProgress(DownloadRequest request, boolean isCancel) {
//        mRequest = request;
//
//        mContentView = new RemoteViews(mContext.getPackageName(), R.layout.download_notification_progress_bar);
//        String title;
//        int activeCount = Core.getInstance().getExecutorSupplier().forDownloadTasks().getActiveCount();
//        if (activeCount > 1)
//            title = "[" + activeCount + "]个项目下载中";
//        else
//            title = request.name;
////        int progress = (int) (request.currentSize * (float) MAX_PROGRESS / request.totalSize);
////        if (progress > MAX_PROGRESS)
////            progress = MAX_PROGRESS - 1;
//        mContentView.setTextViewText(R.id.progress_title, title);
////        mContentView.setTextViewText(R.id.progress_percentage, progress + "%");
//        mContentView.setProgressBar(R.id.progress_bar, (int) request.totalSize, (int) request.currentSize, false);
//
//        Intent intent = new Intent(mContext, MyApplication.app_activity.getClass());
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        mNotification = new Notification();
//        mNotification.icon = R.mipmap.icon_app;
//        mNotification.flags |= Notification.FLAG_ONGOING_EVENT & Notification.FLAG_NO_CLEAR;//FLAG_ONGOING_EVENT不能被销毁；FLAG_NO_CLEAR点击被销毁
//        mNotification.contentView = mContentView;
//        mNotification.contentIntent = pi;
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(PROGRESS_CHANNEL_ID, "showProgress", NotificationManager.IMPORTANCE_HIGH);
//            mNotificationManager.createNotificationChannel(channel);//创建通知渠道
//        }
//
//        if (isCancel)
//            mNotificationManager.notify(PROGRESS_ID, mNotification);
//        else
//            cancelProgress();
    }

    //更新进度
    public void updateProgress() {
//        long currentTime = System.currentTimeMillis();
//        if (currentTime - mLastRefresh <= SHOW_INTEVAL)
//            return;
//        mLastRefresh = currentTime;
//
//        if (mRequest == null)
//            return;
//
////        int progress = (int) (mRequest.currentSize * (float) MAX_PROGRESS / mRequest.totalSize);
////        mContentView.setTextViewText(R.id.progress_percentage, progress + "%");
//        try {
//            mContentView.setProgressBar(R.id.progress_bar, (int) mRequest.totalSize
//                    , (int) mRequest.currentSize, false);
//            mNotificationManager.notify(PROGRESS_ID, mNotification);
//        } catch (Exception e) {
//
//        }
    }

    //更新标题和进度
    public void updateTitle() {
//        String title;
//        int activeCount = Core.getInstance().getExecutorSupplier().forDownloadTasks().getActiveCount();
//        if (activeCount > 1)
//            title = "[" + activeCount + "]个项目下载中";
//        else if (activeCount == 1)
//            title = mRequest.name;
//        else {
//            cancelProgress();
//            return;
//        }
//        mContentView.setTextViewText(R.id.progress_title, title);
//        mContentView.setProgressBar(R.id.progress_bar, (int) mRequest.totalSize, (int) mRequest.currentSize, false);
//        mNotificationManager.notify(PROGRESS_ID, mNotification);
    }

    public void suspension(String name) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "suspension", NotificationManager.IMPORTANCE_HIGH);
//            mNotificationManager.createNotificationChannel(channel);//创建通知渠道
//        }
//
//        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification);
//        remoteViews.setImageViewResource(R.id.custom_icon, R.mipmap.icon_app);
//        remoteViews.setTextViewText(R.id.tv_custom_content, name + " " + mContext.getString(R.string.MSG70018));
//
//        Intent intent = new Intent(mContext, MyApplication.app_activity.getClass());
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
//                .setContent(remoteViews)
//                .setContentIntent(pendingIntent)
//                .setSmallIcon(R.mipmap.icon_app)
//                .setAutoCancel(true)// 点击消失
//                .setPriority(Notification.PRIORITY_MAX)//优先级
//                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_app))
//                .setWhen(System.currentTimeMillis())// 通知首次出现在通知栏，带上升动画效果的
//                // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
//                .setDefaults(Notification.DEFAULT_ALL);
//
//        mNotificationManager.notify(RESULT_ID, builder.build());
    }

    /**
     * 普通通知栏
     */
//    public void showResult(DownloadRequest request) {
//        try {
//            Intent intent = new Intent(mContext, MyApplication.app_activity.getClass());
//            intent.setAction(packageName + ACTION_RESULT);
//
//            NotificationCompat.Builder builder = null;
//            String contentText;
//            PendingIntent pi = PendingIntent.getActivity(mContext, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            if (request.downloadState == DownLoadState.STATE_DOWNLOADED) {
//                contentText = mContext.getResources().getString(R.string.MSG70018);
//                intent.putExtra(mExtraResult, true);
//            } else {
//                String title = mContext.getResources().getString(R.string.MSG11010);
//                contentText = ReplaceUtil.replaceFail(mContext, title);
//                intent.putExtra(mExtraResult, false);
//            }
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                String id = String.valueOf(RESULT_ID);
//                NotificationChannel channel = new NotificationChannel(id, mContext.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW);
//                mNotificationManager.createNotificationChannel(channel);
//                builder = new NotificationCompat.Builder(mContext, id);
//                mNotificationManager.notify(RESULT_ID, builder.build());
//
//            } else if (Build.VERSION.SDK_INT >= 23) {
//                builder = new NotificationCompat.Builder(mContext);
//                mNotificationManager.notify(RESULT_ID, builder.setAutoCancel(false)
//                        .setContentTitle(request.name)
//                        .setContentText(contentText)
//                        .setSmallIcon(R.mipmap.icon_60)
//                        .setContentIntent(pi)
//                        .build());
//
//            } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
//                Notification build = new Notification.Builder(mContext)
//                        .setAutoCancel(false)
//                        .setContentTitle(request.name)
//                        .setContentText(contentText)
//                        .setSmallIcon(R.mipmap.icon_60)
//                        .setContentIntent(pi)
//                        .build();
//            }
//
//        } catch (Exception e) {
//            return;
//        }
//    }
}
