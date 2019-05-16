package other;

import android.text.TextUtils;


/**
 * Created by lingjianzhong on 2017/11/29.
 * 视频下载实体类
 */
public class DownLoadBean implements MultiItemEntity {

    public String id;//视频的id唯一 时间戳
    /**
     * 视频真实名称
     */
    public String name;
    /**
     * 本地存储名称
     * 时间戳
     */
    public String videoName;
    /**
     * 视频的图片
     */
    public String icon;
    /**
     * 下载视频地址
     */
    public String url;
    /**
     * 视频的总大小
     */
    public long totalSize;
    /**
     * 视频的当前大小
     */
    public long currentSize;
    /**
     * 当前下载速度
     */
    public long currentSpeed;
    /**
     * 下载的状态
     */
    public int downloadState = DownLoadState.STATE_NONE;
    /**
     * 视频下载完成时间
     */
    public String time;
    /**
     * 标志是否下载完成
     * 0:未下载完成 1:下载完成
     */
    public int type;
    /**
     * 当前下载ts的位置
     */
    public int currentTsIndex;
    /**
     * 获取视频时长
     */
    public String videoTime;
    /**
     * 视频图片
     */
    public String picture;
    /**
     * 下载视频网站地址
     */
    public String web_url;
    /**
     * 文件格式
     */
    public String fileFormat;

    public long startTime;//开始下载时间
    public String error;//错误提示
    public String errorCode;//错误码
    public short select;
    public double downSpeed; //当前下载速度

    /**
     * 下载唯一id
     */
    public int downloadId;
    /**
     * 视频保存路径
     */
    public String path;
    /**
     * ts临时存储路径
     */
    public String tsPath;
    /**
     * 下载线程数
     */
    public int threadNumber;

    public DownLoadBean() {
    }

    public DownLoadBean(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DownLoadBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", path='" + path + '\'' +
                ", totalSize=" + totalSize +
                ", currentSize=" + currentSize +
                ", currentSpeed=" + currentSpeed +
                ", downloadState=" + downloadState +
                ", time='" + time + '\'' +
                ", videoName='" + videoName + '\'' +
                ", type=" + type +
                ", videoTime='" + videoTime + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }

    @Override
    public int getItemType() {
        if (TextUtils.isEmpty(id)) {
            return TYPE_TWO;
        } else {
            return TYPE_ONE;
        }
    }

    public static final int TYPE_ONE = 0;//布局1
    public static final int TYPE_TWO = 1;//布局2

    public String getVideoTime() {
        return "null".equals(videoTime) ? "" : videoTime;
    }

    public String getName() {
        return "null".equals(name) ? "" : name;
    }

    public String getTime() {
        return "null".equals(time) ? "" : time;
    }

    public String getPicture() {
        return "null".equals(picture) ? "" : picture;
    }
}
