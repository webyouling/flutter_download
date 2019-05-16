/*
 *    Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
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

package downloader.request;


import android.app.Activity;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;

import downloader.Error;
import downloader.OnCancelListener;
import downloader.OnChangeThreadListener;
import downloader.OnDownloadListener;
import downloader.OnPauseListener;
import downloader.OnProgressListener;
import downloader.OnStartOrResumeListener;
import downloader.Priority;
import downloader.core.Core;
import downloader.internal.ComponentHolder;
import downloader.internal.DownloadRequestQueue;
import other.Constants;
import other.DownLoadBean;
import other.DownLoadState;
import other.Loger;

/**
 * Created by kevin on 2018/3/7.
 */

public class DownloadRequest extends DownLoadBean {

    public Priority priority;
    public Object tag;
    public int sequenceNumber;//下载顺序
    public Future future;
    public long downloadedBytes;
    public int readTimeout;
    public int connectTimeout;
    public String userAgent;
    private OnProgressListener onProgressListener;
    private OnDownloadListener onDownloadListener;
    private OnStartOrResumeListener onStartOrResumeListener;
    private OnPauseListener onPauseListener;
    private OnCancelListener onCancelListener;
    private OnChangeThreadListener onChangeThreadListener;
    private HashMap<String, List<String>> headerMap;
    private Activity activity;

    public DownloadRequest() {

    }

    DownloadRequest(DownloadRequestBuilder builder, Activity activity) {
        url = builder.url;
        this.tsPath = builder.dirPath;
        this.videoName = id = builder.fileName;
        this.headerMap = builder.headerMap;
        this.priority = builder.priority;
        this.tag = builder.tag;
        this.readTimeout = builder.readTimeout != 0 ? builder.readTimeout : getReadTimeoutFromConfig();
        this.connectTimeout = builder.connectTimeout != 0 ? builder.connectTimeout : getConnectTimeoutFromConfig();
        this.userAgent = builder.userAgent;
        this.activity = activity;
        if (url.indexOf(Constants.M3U8, 0) > -1)
            fileFormat = Constants.M3U8;
        else
            fileFormat = Constants.MP4;

    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDirPath() {
        return tsPath;
    }

    public void setDirPath(String dirPath) {
        this.tsPath = dirPath;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public HashMap<String, List<String>> getHeaders() {
        return headerMap;
    }

    public Future getFuture() {
        return future;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public void setDownloadedBytes(long downloadedBytes) {
        this.downloadedBytes = downloadedBytes;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getUserAgent() {
        if (userAgent == null) {
            userAgent = ComponentHolder.getInstance().getUserAgent();
        }
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public int getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(int downloadId) {
        this.downloadId = downloadId;
    }

    public OnProgressListener getOnProgressListener() {
        return onProgressListener;
    }

    public DownloadRequest setOnStartOrResumeListener(OnStartOrResumeListener onStartOrResumeListener) {
        this.onStartOrResumeListener = onStartOrResumeListener;
        return this;
    }

    public DownloadRequest setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
        return this;
    }

    public DownloadRequest setOnPauseListener(OnPauseListener onPauseListener) {
        this.onPauseListener = onPauseListener;
        return this;
    }

    public DownloadRequest setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    public DownloadRequest setOnChangeThreadListener(OnChangeThreadListener changeThreadListener) {
        this.onChangeThreadListener = changeThreadListener;
        return this;
    }


    public DownloadRequest start(OnDownloadListener onDownloadListener) {
        this.onDownloadListener = onDownloadListener;
        DownloadRequestQueue.getInstance(activity).addRequest(this);
        return this;
    }

    /**
     * 下载失败
     */
    public void deliverError(final Error error) {
        if (downloadState != DownLoadState.STATE_DELETE) {
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable() {
                public void run() {
                    if (onDownloadListener != null) {
                        onDownloadListener.onError(error, downloadId);
                    }
                }
            });
        }
    }

    /**
     * 成功
     */
    public void deliverSuccess() {
        if (downloadState != DownLoadState.STATE_DELETE) {
            downloadState = DownLoadState.STATE_DOWNLOADED;
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable() {
                public void run() {
                    if (onDownloadListener != null) {
                        onDownloadListener.onDownloadComplete(downloadId);
                    }
                    finish();
                }
            });
        }
    }

    /**
     * 改变线程
     */
    public void changeThread() {
        if (downloadState != DownLoadState.STATE_DELETE) {
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable() {
                public void run() {
                    if (onChangeThreadListener != null) {
                        onChangeThreadListener.onChangeThread(downloadId);
                    }
                }
            });
        }
    }

    /**
     * 开始下载
     */
    public void deliverStartEvent() {
        if (downloadState != DownLoadState.STATE_DELETE) {
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable() {
                public void run() {
                    if (onStartOrResumeListener != null) {
                        onStartOrResumeListener.onStartOrResume();
                    }
                }
            });
        }
    }

    /**
     * 暂停
     */
    public void deliverPauseEvent() {
        if (downloadState != DownLoadState.STATE_DELETE) {
            Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable() {
                public void run() {
                    if (onPauseListener != null) {
                        onPauseListener.onPause();
                    }
                }
            });
        }
    }

    /**
     * 删除
     */
    private void deliverCancelEvent() {
        Loger.e("deliverCancelEvent");
        Core.getInstance().getExecutorSupplier().forMainThreadTasks().execute(new Runnable() {
            public void run() {
                if (onCancelListener != null) {
                    onCancelListener.onCancel();
                }
            }
        });
    }

    public void cancel() {
        Loger.e("cancel");
        downloadState = DownLoadState.STATE_DELETE;
        if (future != null) {
            future.cancel(true);
        }
        deliverCancelEvent();
    }

    private void finish() {
        destroy();
        DownloadRequestQueue.getInstance(activity).finish(this);
    }

    private void destroy() {
        this.onProgressListener = null;
        this.onDownloadListener = null;
        this.onStartOrResumeListener = null;
        this.onPauseListener = null;
        this.onCancelListener = null;
    }

    private int getReadTimeoutFromConfig() {
        return ComponentHolder.getInstance().getReadTimeout();
    }

    private int getConnectTimeoutFromConfig() {
        return ComponentHolder.getInstance().getConnectTimeout();
    }

}
