package downloader.internal;

import java.io.FileOutputStream;


/**
 * abstract AbsDownLoadTask
 */
public abstract class AbsDownLoadTask {

    /**
     * retry times
     */
    protected static final int RETRY_TIMES = 12;
    /**
     * buff size
     */
    protected static final int BUF = 1024 * 16;
    /**
     * sd卡空间不足
     */
    protected static final String ERROR_SDCARD_FULL = "sd卡空间不足";
    /**
     * 写文件失败
     */
    protected static final String ERROR_FILE = "写文件失败";
    /**
     * m3u8 keyword
     */
    protected static final String M3U8 = "m3u8";
    /**
     * 刷新UI时间间隔
     */
    protected static final int REFRESH_INTEVAL = 1000;
    /**
     * 本次开始时间
     */
    protected long mStarttime;
    /**
     * 输出流
     */
    protected FileOutputStream mOut;
    /**
     * 上次刷新UI时间
     */
    protected long mLastRefreshTime;
    /**
     * 本次下载字节数
     */
    protected long mBytesThistime;
    /**
     * host url for m3u8
     */
    protected String mHostUrl;
    /**
     * 重试次数
     */
    protected int mRetrytimes;
    /**
     * 是否支持断点
     */
    protected boolean mCanContinue;

//    /**
//     * @param aInfo
//     */
//    public AbsDownLoadTask(DownLoadInfo aInfo) {
//        mStarttime = System.currentTimeMillis();
//        mRetrytimes = RETRY_TIMES;
//    }

    public abstract void start();

    public abstract void pause();

    public abstract void cancel(boolean iselfile, boolean inNotifyUI);

    public abstract void stop();

    /**
     * 重试次数
     */
    public void resetRetry() {
        mRetrytimes = AbsDownLoadTask.RETRY_TIMES;
    }

}
