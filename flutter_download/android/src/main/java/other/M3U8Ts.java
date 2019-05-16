package other;

import java.io.Serializable;

/**
 * Created by lingjianzhong on 2017/11/30.
 * M3U8下载片段信息
 */

public class M3U8Ts implements Serializable {
    public String m3u8Url;//用DownLoadBean里面的下载视频地址作为M3U8Ts的id
    public String tsUrl;//ts链接
    public String tsName;//ts名字
    public String tsPath;//视频保存路径
    public long tsTotalSize;//单个m3u8片段的总大小
    public long tsCurrentSize = 0;//单个m3u8片段的当前大小
    public int tsDownloadState = DownLoadState.STATE_NONE;//单个m3u8片段下载的状态

    public M3U8Ts() {
    }

    public M3U8Ts(String m3u8Url, String tsUrl, String tsName) {
        this.m3u8Url = m3u8Url;
        this.tsUrl = tsUrl;
        this.tsName = tsName;
    }

    public M3U8Ts(String m3u8Url, String tsUrl, String tsName, String path) {
        this.m3u8Url = m3u8Url;
        this.tsUrl = tsUrl;
        this.tsName = tsName;
        tsPath = path;
    }


    @Override
    public String toString() {
        return "M3U8Ts{" +
                "m3u8Url='" + m3u8Url + '\'' +
                ", tsUrl='" + tsUrl + '\'' +
                ", tsName='" + tsName + '\'' +
                ", tsPath='" + tsPath + '\'' +
                ", tsTotalSize=" + tsTotalSize +
                ", tsCurrentSize=" + tsCurrentSize +
                ", tsDownloadState=" + tsDownloadState +
                '}';
    }

    public String getM3u8Url() {
        return m3u8Url;
    }

    public void setM3u8Url(String m3u8Url) {
        this.m3u8Url = m3u8Url;
    }

    public String getTsUrl() {
        return tsUrl;
    }

    public void setTsUrl(String tsUrl) {
        this.tsUrl = tsUrl;
    }

    public String getTsName() {
        return tsName;
    }

    public void setTsName(String tsName) {
        this.tsName = tsName;
    }

    public String getTsPath() {
        return tsPath;
    }

    public void setTsPath(String tsPath) {
        this.tsPath = tsPath;
    }

    public long getTsTotalSize() {
        return tsTotalSize;
    }

    public void setTsTotalSize(long tsTotalSize) {
        this.tsTotalSize = tsTotalSize;
    }

    public long getTsCurrentSize() {
        return tsCurrentSize;
    }

    public void setTsCurrentSize(long tsCurrentSize) {
        this.tsCurrentSize = tsCurrentSize;
    }

    public int getTsDownloadState() {
        return tsDownloadState;
    }

    public void setTsDownloadState(int tsDownloadState) {
        this.tsDownloadState = tsDownloadState;
    }
}
