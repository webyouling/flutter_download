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

import downloader.Error;
import downloader.PRDownloader;
import downloader.Response;
import downloader.handler.ProgressHandler;
import downloader.httpclient.HttpClient;
import downloader.request.DownloadRequest;
import other.Constants;
import other.DownLoadState;
import other.DownloadBeanUtil;
import other.Loger;
import other.M3U8DataBeanUtil;
import other.M3U8Ts;
import other.MUtils_kevin;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;


/**
 * Created by kevin on 2018/3/7.
 */
public class DownloadM3U8Task {

    private static final int BUFFER_SIZE = 1024 * 4;
    private static final long TIME_GAP_FOR_SYNC = 2000;
    private static final long MIN_BYTES_FOR_SYNC = 65536;
//    private final VideoKit videoKit;

    /**
     * 请求数据
     */
    private DownloadRequest request;
    private ProgressHandler progressHandler;
    private InputStream inputStream;
    private HttpClient httpClient;
    private int responseCode;
    private long downloadSize;//当前下载大小
    private List<M3U8Ts> mTsList;
    private int mInt;
    private int mRetryTimes;//重试次数
    private long lastSyncTime;
    private long lastSyncBytes;
    private Activity activity;

    private DownloadM3U8Task(DownloadRequest request,Activity activity) {
        this.request = request;
        this.activity = activity;
        Loger.e("创建下载request=" + request.toString());
//        videoKit = new VideoKit();
    }

    static DownloadM3U8Task create(DownloadRequest request, Activity activity) {
        return new DownloadM3U8Task(request,activity);
    }

    Response run() {

        Response response = new Response();

        if (request.downloadState == DownLoadState.STATE_DELETE) {
            Loger.e("--删除1");
            return response;
        } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
            Loger.e("--暂停1");
            response.isPaused = true;
            return response;
        } else if (request.downloadState == DownLoadState.STATE_CHANGE_THREAD) {
            Loger.e("--改变线程1");
            response.isChangeThread = true;
            return response;
        }

        FileOutputStream outputStream = null;
        try {
            //创建handler
            if (request.getOnProgressListener() != null)
                progressHandler = new ProgressHandler(request.getOnProgressListener());

            request.downloadState = DownLoadState.STATE_DOWNLOADING;
            Loger.e("request.downloadState=" + request.downloadState + "  request.downloadId=" + request.downloadId);

            //查询数据库是否有下载一半的数据
            // TODO: 2019/3/15 上下文
            mTsList = M3U8DataBeanUtil.getM3U8DownLoad(activity, request.url);
            if (mTsList.size() <= 0) {
                mTsList = MUtils_kevin.parseIndex(request.url, request.tsPath, true,activity);//解析

                if (mTsList.size() < 1) {//ts解析失败
                    request.downloadState = DownLoadState.STATE_ERROR;
                    Error error = new Error();
                    error.isParseTsError = true;
                    response.error = error;
                    return response;
                }

                request.totalSize = 1024 * 1024 * mTsList.size();
                // TODO: 2019/3/15 上下文
                DownloadBeanUtil.updateTsSize(activity, request.totalSize, request.id);

                Loger.e("ts路径=" + request.tsPath);
                //创建ts文件的临时文件路径
                createFile(request.tsPath);

            } else if (mTsList.size() > 0) {//断点下载
                File tsTempPath = new File(mTsList.get(0).tsPath);
                if (!tsTempPath.exists()) {//不存在本地文件
                    tsTempPath.mkdirs();
                    mInt = 0;
                } else
                    mInt = request.currentTsIndex;
                request.tsPath = mTsList.get(0).tsPath;
                Loger.e("-断点下载=" + tsTempPath);
            }
            DownloadBeanUtil.updateState(activity, request.downloadState, request.downloadId + "");

            //开始下载
            request.deliverStartEvent();

            //记录开始下载时间
            long startTime = System.currentTimeMillis();

            int size = mTsList.size();
            for (int i = mInt; i < size; i++) {
                M3U8Ts ts = mTsList.get(i);
                final File tsFile = new File(ts.tsName);

                //开始链接url
                httpClient = ComponentHolder.getInstance().getHttpClient();
                httpClient.connect(ts.tsUrl);

                if (request.downloadState == DownLoadState.STATE_DELETE) {
                    Loger.e("--删除2");
                    return response;
                } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
                    Loger.e("--暂停2");
                    response.isPaused = true;
                    DownloadBeanUtil.updateTsIndex(activity, request);
                    return response;
                } else if (request.downloadState == DownLoadState.STATE_CHANGE_THREAD) {
                    response.isChangeThread = true;
                    Loger.e("--改变线程2=" + request.downloadId);
                    return response;
                }
                responseCode = httpClient.getResponseCode();

                //是否成功
                if (!isSuccessful()) {
                    Error error = new Error();
                    error.isConnectionError = true;
                    response.error = error;
                    request.downloadState = DownLoadState.STATE_ERROR;
                    sendOtherUpdate();
                    return response;
                }

                inputStream = httpClient.getInputStream();

                byte[] buff = new byte[BUFFER_SIZE];
                outputStream = new FileOutputStream(tsFile);
                long currentSize = request.currentSize;
                //开始读数据
                do {
                    final int byteCount = inputStream.read(buff);

                    if (byteCount == -1)
                        break;

                    outputStream.write(buff, 0, byteCount);

                    downloadSize += byteCount;

                    //计算下载的平均速度
                    request.downSpeed = downloadSize / (System.currentTimeMillis() - startTime + 1);
                    request.currentSize += byteCount;

                    sendProgress();

                    syncIfRequired(outputStream);

                    if (request.downloadState == DownLoadState.STATE_DELETE) {
                        Loger.e("--删除3");
                        return response;
                    } else if (request.downloadState == DownLoadState.STATE_PAUSED) {
                        Loger.e("--暂停3");
                        sync(outputStream);
                        response.isPaused = true;
                        return response;
                    } else if (request.downloadState == DownLoadState.STATE_CHANGE_THREAD) {
                        sync(outputStream);
                        response.isChangeThread = true;
                        Loger.e("--改变线程3=" + request.downloadId);
                        return response;
                    }
                } while (true);
                request.totalSize = request.currentSize + (size - (i + 1)) * (request.currentSize - currentSize);//当前大小 +（（总数量 - 已下载数量） * 当前ts的大小）
                request.currentTsIndex = i;

                //写入数据库下载成功的ts
                DownloadBeanUtil.updateTsIndex(activity, request);
                Loger.e("第" + i + "个ts下载成功" + tsFile.getPath());
            }
            Loger.e("下载完成");

            //下载完成,不能用response,因为异步转mp4
            request.downloadState = DownLoadState.STATE_DOWNLOADED;
            sendOtherUpdate();

            tsConversionMp4();
        } catch (IOException e) {
            Loger.e("抛出异常=" + e.toString());
            Error error = new Error();
            String string = e.toString();

            if (string.indexOf("java.net.SocketException", 0) > -1) {
                Loger.e("下载过程中断网");
                error.isSocketException = true;
                request.error = "下载过程中断网" + string;
                request.downloadState = DownLoadState.NETWORK_STATUS;
                PRDownloader.shutdownNow();

            } else if (string.indexOf("java.net.UnknownHostException", 0) > -1) {
                //已测试：无网络   未测试：访问网站已经倒闭/关闭或者不存在；无法解析该域名
                Loger.e("应用启动时，无网络");
                error.isUnknownHostException = true;
                request.error = "应用启动时，无网络" + string;
                request.downloadState = DownLoadState.NETWORK_STATUS;
                PRDownloader.shutdownNow();

            } else if (string.indexOf("java.net.SocketTimeoutException", 0) > -1) {
                Loger.e("url链接超时");
                resumePost(outputStream);
                error.isInvalidError = true;
                request.error = "url链接超时" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else if (string.indexOf("java.io.FileNotFoundException", 0) > -1) {
                Loger.e("没有这样的文件或目录");
                resumePost(outputStream);
                error.isFileNotFoundException = true;
                request.error = "没有这样的文件或目录" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else if (string.indexOf("java.net.ProtocolException", 0) > -1) {
                Loger.e("url链接协议错误");
                resumePost(outputStream);
                error.isProtocolException = true;
                request.error = "url链接协议错误" + string;
                request.downloadState = DownLoadState.STATE_ERROR;

            } else if (string.indexOf("javax.net.ssl.SSLException", 0) > -1) {
                Loger.e("系统调用期间的I/O错误，软件人为的连接中止");
                resumePost(outputStream);
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
            Loger.e("-finally-关闭流文件");
            closeAllSafely(outputStream);
        }
        return response;
    }

    //异常重新链接
    private void resumePost(FileOutputStream outputStream) {
        mRetryTimes += 1;
        Loger.e("mRetryTimes=" + mRetryTimes);
        if (mRetryTimes < 4) {
            closeAllSafely(outputStream);
            run();
            Loger.e("异常重新链接");
        } else
            mRetryTimes = 0;
    }

    //回收
    private void syncIfRequired(FileOutputStream outputStream) throws IOException {
        final long currentBytes = downloadSize;
        final long currentTime = System.currentTimeMillis();
        final long bytesDelta = currentBytes - lastSyncBytes;
        final long timeDelta = currentTime - lastSyncTime;
        if (bytesDelta > MIN_BYTES_FOR_SYNC && timeDelta > TIME_GAP_FOR_SYNC) {
            outputStream.flush();
            lastSyncBytes = currentBytes;
            lastSyncTime = currentTime;
        }
    }

    /**
     * ts转换成Mp4
     */
    private synchronized void tsConversionMp4() {
        StringBuilder ts = new StringBuilder();
        //获取下载完成片段
        int size = mTsList.size();
        Loger.e("共" + size + "个ts");
        for (int i = 0; i < size; i++) {
            M3U8Ts m3U8Ts = mTsList.get(i);
            File tsFile = new File(m3U8Ts.tsName);
            if (tsFile.exists())
                ts.append(tsFile.getPath()).append("|");//拼接片段
            else
                Loger.e(" 没有下载成功的片段 " + tsFile.getPath());
        }

        //视频保存地址
        File mp4Path = createFile(request.path);

        //合并后的名称
        String toMp4Name = mp4Path.getPath() + File.separator + request.videoName + ".mp4";
        Loger.e("toMp4Name=" + toMp4Name);
        //进行视频转换
        tsToMp4(ts.toString(), toMp4Name);
    }

    /**
     * 开始转换
     */
    private void tsToMp4(String inPutPath, String outPutPath) {
//        String tsVideo = inPutPath.substring(0, inPutPath.length() - 1);
//        Command command = videoKit.createCommand()
//                .inputPathTs(tsVideo)
//                .outputPath(outPutPath)
//                .customCommand("-acodec copy -vcodec copy -f mp4")
//                .build();
//        //开始转换
//        new AsyncCommandExecutor(command, new ProcessingListener() {
//            @Override
//            public void onSuccess(String path) {
//                Loger.e("转换成功");
//                request.time = String.valueOf(System.currentTimeMillis());
//                request.type = 1;
//                request.path = path;
//                request.videoTime = VideoUtils.getDuration(path);
//                request.picture = VideoUtils.getVideoPicture(path, request.id);
//
//                request.deliverSuccess();//通知用户下载完成
//            }
//
//            @Override
//            public void onFailure(int returnCode) {//重新下载 code：1;ts文件不存在:3073;目标文件不存在:3031
//                Loger.e("转换失败:" + returnCode);
//                Error error = new Error();
//                error.isDecodeError = true;
//                request.deliverError(error);//通知用户解析失败
//                FileUtilities.deleteDirectory(request.tsPath);//删除ts临时文件
//                DownloadBeanUtil.DeleteDownLoadById(activity, request.id);//删除下载表
//                M3U8DataBeanUtil.DeleteM3U8DownLoadById(activity, request.url);//删除ts表
//            }
//        }).execute();
    }

    /**
     * 链接请求是否成功
     */
    private boolean isSuccessful() {
        return responseCode >= HttpURLConnection.HTTP_OK
                && responseCode < HttpURLConnection.HTTP_MULT_CHOICE;
    }

    /**
     * 更新界面
     */
    private void sendProgress() {
        if (request.downloadState != DownLoadState.STATE_DELETE) {
            if (progressHandler != null)
                progressHandler.obtainMessage(Constants.UPDATE_PROGRESS, request.downloadId, Constants.UPDATE_PROGRESS).sendToTarget();
        }
    }

    /**
     * 不在下载页面时更新 || 下载完成更新 || 下载错误更新
     */
    private void sendOtherUpdate() {
        if (progressHandler != null)
            progressHandler.obtainMessage(Constants.UPDATE_OTHER, request.downloadId, Constants.UPDATE_OTHER).sendToTarget();
    }

    private void sync(FileOutputStream outputStream) {
        boolean success;
        try {
            outputStream.flush();
            success = true;
        } catch (IOException e) {
            success = false;
            e.printStackTrace();
        }
        if (success)
            DownloadBeanUtil.updateTsIndex(activity, request);
    }

    private void closeAllSafely(FileOutputStream outputStream) {
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
        } finally {
            if (outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private File createFile(String path) {
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();
        return file;
    }
}
