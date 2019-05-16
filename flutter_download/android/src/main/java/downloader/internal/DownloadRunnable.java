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

package downloader.internal;


import android.app.Activity;

import downloader.Priority;
import downloader.Response;
import downloader.request.DownloadRequest;
import other.Constants;
import other.DownLoadState;
import other.Loger;

/**
 * Created by kevin on 2018/3/7.
 */

public class DownloadRunnable implements Runnable {

    /**
     * 优先
     */
    public final Priority priority;
    /**
     * 序列
     */
    public final int sequence;
    public final DownloadRequest request;
    public Activity activity;

    DownloadRunnable(DownloadRequest request,Activity activity) {
        this.request = request;
        this.priority = request.getPriority();
        this.sequence = request.sequenceNumber;
        this.activity =activity;
    }

    @Override
    public void run() {
        if (request.downloadState == DownLoadState.STATE_PAUSED || request.downloadState == DownLoadState.STATE_CHANGE_THREAD)
            return;

        request.downloadState = DownLoadState.STATE_CONNECTION;
        Response response;
        if (request.fileFormat.equals(Constants.M3U8))
            response = DownloadM3U8Task.create(request,activity).run();
        else
            response = DownloadMp4Task.create(request,activity).run();

        if (response.isSuccessful) {//成功
            Loger.e("成功");
            request.deliverSuccess();
        } else if (response.isPaused) {//暂停
            Loger.e("暂停");
            request.deliverPauseEvent();
        } else if (response.error != null) {//错误
            Loger.e("错误");
            request.deliverError(response.error);
        } else if (response.isChangeThread) {
            Loger.e("改变线程数");
            request.changeThread();
        }

    }

}
