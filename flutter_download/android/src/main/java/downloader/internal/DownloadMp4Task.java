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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.SyncFailedException;
import java.net.HttpURLConnection;

import downloader.Error;
import downloader.PRDownloader;
import downloader.Response;
import downloader.handler.ProgressHandler;
import downloader.httpclient.HttpClient;
import downloader.request.DownloadRequest;
import downloader.utils.Utils;
import other.Constants;
import other.DownLoadState;
import other.DownloadBeanUtil;
import other.Loger;
import other.VideoUtils;


public class DownloadMp4Task {

    private static final int BUFFER_SIZE = 1024 * 4;
    private static final long TIME_GAP_FOR_SYNC = 2000;
    private static final long MIN_BYTES_FOR_SYNC = 65536;
    private final DownloadRequest request;
    private ProgressHandler progressHandler;
    private long lastSyncTime;
    private long lastSyncBytes;
    private InputStream inputStream;
    private HttpClient httpClient;
    private int responseCode;
    private boolean isResumeSupported;
    private int downloadSize;
    private int mRetryTimes;//重试次数
    private Activity activity;

    private DownloadMp4Task(DownloadRequest request, Activity activity) {
        this.request = request;
        this.activity = activity;
    }

    static DownloadMp4Task create(DownloadRequest request, Activity activity) {
        return new DownloadMp4Task(request, activity);
    }

    Response run() {

        Response response = new Response();

        if (request.downloadState == DownLoadState.STATE_DELETE) {
            return response;
        } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
            response.isPaused = true;
            Loger.e("--暂停1");
            return response;
        } else if (request.downloadState == DownLoadState.STATE_CHANGE_THREAD) {
            Loger.e("--改变线程1");
            response.isChangeThread = true;
            return response;
        }

        BufferedOutputStream outputStream = null;

        FileDescriptor fileDescriptor = null;

        RandomAccessFile randomAccess = null;

        try {
            if (request.getOnProgressListener() != null)
                progressHandler = new ProgressHandler(request.getOnProgressListener());

            final File file = new File(request.path);
            if (!file.exists())
                file.mkdirs();

            request.downloadState = DownLoadState.STATE_DOWNLOADING;

            httpClient = ComponentHolder.getInstance().getHttpClient();
            httpClient.connect(request);

            request.fileFormat = httpClient.getContentType();

            if (request.downloadState == DownLoadState.STATE_DELETE) {
                return response;
            } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
                response.isPaused = true;
                Loger.e("--暂停1");
                return response;
            }

            httpClient = Utils.getRedirectedConnectionIfAny(httpClient, request);
            responseCode = httpClient.getResponseCode();

            if (!isSuccessful()) {
                Error error = new Error();
                error.isConnectionError = true;
                response.error = error;
                return response;
            }

            setResumeSupportedOrNot();

            if (request.totalSize == 0)
                request.totalSize = httpClient.getContentLength();

            if (request.downloadState == DownLoadState.STATE_DELETE) {
                return response;
            } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
                response.isPaused = true;
                Loger.e("--暂停2");
                return response;
            } else if (request.downloadState == DownLoadState.STATE_CHANGE_THREAD) {
                response.isChangeThread = true;
                Loger.e("--改变线程2=" + request.downloadId);
                return response;
            }
            DownloadBeanUtil.updateState(activity, request.downloadState, request.downloadId + "");

            request.deliverStartEvent();

            inputStream = httpClient.getInputStream();

            byte[] buff = new byte[BUFFER_SIZE];

            File file1 = new File(file + File.separator + request.videoName + ".mp4");
            if (!file1.exists())
                file1.createNewFile();

            randomAccess = new RandomAccessFile(file1, "rw");
            fileDescriptor = randomAccess.getFD();
            outputStream = new BufferedOutputStream(new FileOutputStream(randomAccess.getFD()));

            if (isResumeSupported && request.currentSize > 0)
                randomAccess.seek(request.currentSize);

            if (request.downloadState == DownLoadState.STATE_DELETE) {
                return response;
            } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
                response.isPaused = true;
                Loger.e("--暂停3");
                return response;
            }

            long startTime = System.currentTimeMillis();
            do {

                int byteCount = inputStream.read(buff);

                if (byteCount == -1)
                    break;

                if (outputStream == null)
                    outputStream = new BufferedOutputStream(new FileOutputStream(randomAccess.getFD()));

                outputStream.write(buff, 0, byteCount);

                downloadSize += byteCount;
                Loger.e("downloadSize  ------->  " + downloadSize);
                request.downSpeed = downloadSize / (System.currentTimeMillis() - startTime + 1);
                request.currentSize += byteCount;

                sendProgress();

                syncIfRequired(outputStream, fileDescriptor);

                if (request.downloadState == DownLoadState.STATE_DELETE) {
                    return response;
                } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
                    sync(outputStream, fileDescriptor);
                    response.isPaused = true;
                    Loger.e("--暂停4");
                    return response;
                } else if (request.downloadState == DownLoadState.STATE_CHANGE_THREAD) {
                    sync(outputStream, fileDescriptor);
                    response.isChangeThread = true;
                    Loger.e("--改变线程3=" + request.downloadId);
                    return response;
                }

            } while (true);

            request.downloadState = DownLoadState.STATE_DOWNLOADED;
            request.type = 1;
            request.time = String.valueOf(System.currentTimeMillis());
            request.videoTime = VideoUtils.getDuration(file1.getPath());
            request.picture = VideoUtils.getVideoPicture(file1.getPath(), request.id);
            request.path = file1.getPath();

            response.isSuccessful = true;

        } catch (IOException | IllegalAccessException e) {
            Loger.e("抛出异常=" + e.toString());
            Error error = new Error();
            String string = e.toString();

            if (string.indexOf("javax.net.ssl.SSLException", 0) > -1) {
                Loger.e("下载过程中断网");
                error.isSocketException = true;
                request.error = "下载过程中断网" + string;
                request.downloadState = DownLoadState.NETWORK_STATUS;
                PRDownloader.shutdownNow();

            } else if (string.indexOf("java.net.UnknownHostException", 0) > -1) {
                Loger.e("应用启动时，无网络");
                error.isUnknownHostException = true;
                request.error = "应用启动时，无网络" + string;
                request.downloadState = DownLoadState.NETWORK_STATUS;
                PRDownloader.shutdownNow();

            } else if (string.indexOf("java.net.SocketTimeoutException", 0) > -1) {
                Loger.e("url链接超时");
                resumePost(outputStream, fileDescriptor);
                error.isInvalidError = true;
                request.error = "url链接超时" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else if (string.indexOf("java.io.FileNotFoundException", 0) > -1) {
                Loger.e("没有这样的文件或目录");
                resumePost(outputStream, fileDescriptor);
                error.isFileNotFoundException = true;
                request.error = "没有这样的文件或目录" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else if (string.indexOf("java.net.ProtocolException", 0) > -1) {
                Loger.e("url链接协议错误");
                resumePost(outputStream, fileDescriptor);
                error.isProtocolException = true;
                request.error = "url链接协议错误" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else if (string.indexOf("java.net.ProtocolException", 0) > -1) {
                Loger.e("系统调用期间的I/O错误，软件人为的连接中止");
                resumePost(outputStream, fileDescriptor);
                error.isSSLException = true;
                request.error = "系统调用期间的I/O错误，软件人为的连接中止（人为断网会出现）" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else {
                Loger.e("其他情况");
                error.isIOError = true;
                request.error = "其他情况" + string;
                request.downloadState = DownLoadState.STATE_ERROR;
            }
            response.error = error;
//            FirebaseCrash.report(new Exception(request.error));
            sendOtherUpdate();
        } finally {
            closeAllSafely(outputStream, fileDescriptor);
            if (randomAccess != null) {
                try {
                    randomAccess.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return response;
    }

    private void resumePost(BufferedOutputStream outputStream, FileDescriptor fileDescriptor) {
        mRetryTimes += 1;
        if (mRetryTimes < 4) {
            closeAllSafely(outputStream, fileDescriptor);
            run();
            Loger.e("异常重新链接");
        } else
            mRetryTimes = 0;
    }

    private boolean isSuccessful() {
        return responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE;
    }

    private void setResumeSupportedOrNot() {
        isResumeSupported = (responseCode == HttpURLConnection.HTTP_PARTIAL);
    }

    private void sendProgress() {
        if (request.downloadState != DownLoadState.STATE_DELETE) {
            if (progressHandler != null) {
                progressHandler.obtainMessage(Constants.UPDATE_PROGRESS, request.downloadId, Constants.UPDATE_PROGRESS).sendToTarget();
            }
        }
    }

    private void sendOtherUpdate() {
        if (progressHandler != null)
            progressHandler.obtainMessage(Constants.UPDATE_OTHER, request.downloadId, Constants.UPDATE_OTHER).sendToTarget();
    }

    //回收
    private void syncIfRequired(BufferedOutputStream outputStream, FileDescriptor fileDescriptor) throws IOException {
        final long currentBytes = request.currentSize;
        final long currentTime = System.currentTimeMillis();
        final long bytesDelta = currentBytes - lastSyncBytes;
        final long timeDelta = currentTime - lastSyncTime;
        if (bytesDelta > MIN_BYTES_FOR_SYNC && timeDelta > TIME_GAP_FOR_SYNC) {
            sync(outputStream, fileDescriptor);
            lastSyncBytes = currentBytes;
            lastSyncTime = currentTime;
        }
    }

    private void sync(BufferedOutputStream outputStream, FileDescriptor fileDescriptor) {
        boolean success;
        try {
            outputStream.flush();
            fileDescriptor.sync();
            success = true;
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }
        if (success && isResumeSupported)
            DownloadBeanUtil.updateTsIndex(activity, request);
    }

    private void closeAllSafely(BufferedOutputStream outputStream, FileDescriptor fileDescriptor) {
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileDescriptor != null) {
                try {
                    fileDescriptor.sync();
                } catch (SyncFailedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
