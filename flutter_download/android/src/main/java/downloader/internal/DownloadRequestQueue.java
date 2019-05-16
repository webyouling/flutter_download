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
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import downloader.core.Core;
import downloader.request.DownloadRequest;
import other.DownLoadState;
import other.DownloadBeanUtil;

/**
 * Created by kevin on 2018/3/7.
 */
public class DownloadRequestQueue {

    private static DownloadRequestQueue instance;
    private final AtomicInteger sequenceGenerator;
    private final ConcurrentHashMap<Integer, DownloadRequest> currentRequestMap;
    private int mDownloadId;
    private Activity activity;

    private DownloadRequestQueue(Activity activity) {
        currentRequestMap = new ConcurrentHashMap<>();
        sequenceGenerator = new AtomicInteger();
        this.activity = activity;
    }

    public static void initialize(Activity activity) {
        getInstance(activity);
    }

    public static DownloadRequestQueue getInstance(Activity activity) {
        if (instance == null) {
            synchronized (DownloadRequestQueue.class) {
                if (instance == null) {
                    instance = new DownloadRequestQueue(activity);
                }
            }
        }
        return instance;
    }

    //返回的是新值（即加1后的值）
    private int getSequenceNumber() {
        return sequenceGenerator.incrementAndGet();
    }

    //返回旧值（即加1前的原始值）
    public int getAndIncrement() {
        return sequenceGenerator.getAndIncrement();
    }

    public void pause(int downloadId) {
        DownloadRequest request = currentRequestMap.get(downloadId);
        if (request != null) {
            request.downloadState = DownLoadState.STATE_PAUSED;
        }
    }

    public void changeThread(int downloadId) {
        DownloadRequest request = currentRequestMap.get(downloadId);
        if (request != null) {
            request.downloadState = DownLoadState.STATE_CHANGE_THREAD;
        }
    }

    public void pauseAll() {
        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            DownloadRequest request = currentRequestMapEntry.getValue();
            if (request != null) {
                request.downloadState = DownLoadState.STATE_PAUSED;
                // TODO: 2019/3/15 需要传递上下文
//                DownloadBeanUtil.updateState(MyApplication.getInstance(), request.downloadState, request.id);
            }
        }
    }

    public ConcurrentHashMap<Integer, DownloadRequest> getCurrentRequestMap() {
        return currentRequestMap;
    }

    public int getIndex(int downloadId) {
        int index = 0;
        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            if (downloadId == currentRequestMapEntry.getKey())
                return index;
            index += 1;
        }
        return index;
    }

    public int getDownloadId() {
        return mDownloadId;
    }

    public void resume(int downloadId) {
        DownloadRequest request = currentRequestMap.get(downloadId);
        if (request != null) {
            request.downloadState = DownLoadState.STATE_WAITING;
            request.setFuture(Core.getInstance()
                    .getExecutorSupplier()
                    .forDownloadTasks()
                    .submit(new DownloadRunnable(request,activity)));
            mDownloadId = downloadId;
        }
    }

    public void resumeArray(SparseIntArray downloadIds) {
        for (int i = 0; i < downloadIds.size(); i++) {
            DownloadRequest request = currentRequestMap.get(downloadIds.get(i));
            if (request != null) {
                request.downloadState = DownLoadState.STATE_WAITING;
                request.setFuture(Core.getInstance()
                        .getExecutorSupplier()
                        .forDownloadTasks()
                        .submit(new DownloadRunnable(request,activity)));
            }
        }
    }

    public void resumeAllN() {
        if (currentRequestMap.size() <= 0)
            return;
        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            DownloadRequest request = currentRequestMapEntry.getValue();
            if (request != null && request.downloadState == DownLoadState.NETWORK_STATUS) {
                request.downloadState = DownLoadState.STATE_WAITING;
                request.setFuture(Core.getInstance()
                        .getExecutorSupplier()
                        .forDownloadTasks()
                        .submit(new DownloadRunnable(request,activity)));
            }
        }
    }

    public void resumeAll() {
        if (currentRequestMap.size() <= 0)
            return;
        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            DownloadRequest request = currentRequestMapEntry.getValue();
            if (request != null) {
                request.downloadState = DownLoadState.STATE_WAITING;
                request.setFuture(Core.getInstance()
                        .getExecutorSupplier()
                        .forDownloadTasks()
                        .submit(new DownloadRunnable(request,activity)));
            }
        }
    }

    public void resumeAllP(SparseIntArray downloadIds) {
        ArrayList<DownloadRequest> downloads = new ArrayList<>();
        ArrayList<DownloadRequest> waits = new ArrayList<>();

        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            DownloadRequest request = currentRequestMapEntry.getValue();

            int downloadId = request.downloadId;
            if (downloadId == downloadIds.get(0)
                    || downloadId == downloadIds.get(1)
                    || downloadId == downloadIds.get(2))
                downloads.add(request);
            else
                waits.add(request);

            DownloadBeanUtil.updateState(activity, request.downloadState, request.id);
        }
        int size = downloads.size();
        for (int i = 0; i < size; i++) {
            DownloadRequest request = downloads.get(i);
            request.downloadState = DownLoadState.STATE_WAITING;
            request.setFuture(Core.getInstance()
                    .getExecutorSupplier()
                    .forDownloadTasks()
                    .submit(new DownloadRunnable(request,activity)));
        }
        int size1 = waits.size();
        for (int i = 0; i < size1; i++) {
            DownloadRequest request = waits.get(i);
            request.downloadState = DownLoadState.STATE_WAITING;
            request.setFuture(Core.getInstance()
                    .getExecutorSupplier()
                    .forDownloadTasks()
                    .submit(new DownloadRunnable(request,activity)));
        }
    }

    private void cancelAndRemoveFromMap(DownloadRequest request) {
        if (request != null) {
            request.cancel();
            currentRequestMap.remove(request.downloadId);
        }
    }

    public void cancel(int downloadId) {
        DownloadRequest request = currentRequestMap.get(downloadId);
        cancelAndRemoveFromMap(request);
    }

    public void cancel(Object tag) {
        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            DownloadRequest request = currentRequestMapEntry.getValue();
            if (request.getTag() instanceof String && tag instanceof String) {
                final String tempRequestTag = (String) request.getTag();
                final String tempTag = (String) tag;
                if (tempRequestTag.equals(tempTag)) {
                    cancelAndRemoveFromMap(request);
                }
            } else if (request.getTag().equals(tag)) {
                cancelAndRemoveFromMap(request);
            }
        }
    }

    public void cancelAll() {
        for (Map.Entry<Integer, DownloadRequest> currentRequestMapEntry : currentRequestMap.entrySet()) {
            DownloadRequest request = currentRequestMapEntry.getValue();
            cancelAndRemoveFromMap(request);
        }
    }

    public int getStatus(int downloadId) {
        DownloadRequest request = currentRequestMap.get(downloadId);
        if (request != null) {
            return request.downloadState;
        }
        return DownLoadState.STATE_NONE;
    }


    public void addRequest(DownloadRequest request) {
        currentRequestMap.put(request.downloadId, request);
        request.sequenceNumber = getAndIncrement();
        if (request.downloadState == DownLoadState.STATE_PAUSED)
            return;
        request.downloadState = DownLoadState.STATE_WAITING;
        request.setFuture(Core.getInstance()
                .getExecutorSupplier()
                .forDownloadTasks()
                .submit(new DownloadRunnable(request,activity)));
    }

    public void finish(DownloadRequest request) {
        currentRequestMap.remove(request.downloadId);
    }
}
