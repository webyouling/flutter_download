package other;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by lingjianzhong on 2017/12/7.
 * 视频相关工具类
 */

public class VideoUtils {
    private static final String TAG = "VideoUtils";
    private static Context mContext;

    /**
     * 私有的构造器
     */
    private VideoUtils() {

    }

    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     */
    private static class VideoUtilsHolder {
        private static VideoUtils instance = new VideoUtils();
    }

    public static VideoUtils getInstance(Context context) {
        mContext = context;
        return VideoUtils.VideoUtilsHolder.instance;
    }

    /**
     * 获取视频的第一帧图片
     *
     * @param playerPath 视频路径
     * @return
     */
    public synchronized static String getVideoPicture(Context context, String playerPath) {
        return getVideoPicture(context, playerPath, "");
    }

    /**
     * 获取视频的第一帧图片并且保存到sd卡上
     *
     * @param context
     * @param playerPath
     * @param videoid
     * @return
     */
    public synchronized static String getVideoPicture(Context context, String playerPath, String videoid) {
        String picturePath = "";
        if (!TextUtils.isEmpty(playerPath)) {
            File source = new File(playerPath);
            if (source.exists()) {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                try {
                    mmr.setDataSource(source.getPath());
                    Bitmap bitmap = mmr.getFrameAtTime();//获取第一帧图片
                    // 获取视频的缩略图
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, 1400, 1000, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    if (bitmap != null)
                        picturePath = saveBitmapToSD(bitmap, videoid);
                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
                } finally {
                    mmr.release();//释放资源
                }
            }
        }
        return picturePath;
    }

    /**
     * 提取视频文件的 缩略图
     *
     * @param path
     * @param videoid
     * @return
     */
    public synchronized static String getVideoPicture(String path, String videoid) {
        if (path == null
                || TextUtils.isEmpty(path)
                || !new File(path).exists())
            return "";
        Bitmap bitmap = null;
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);
            bitmap = mmr.getFrameAtTime();//获取第一帧图片
            if (bitmap != null)
                bitmap = ThumbnailUtils.extractThumbnail(bitmap,
                        1400,
                        1000,
                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        } catch (Exception e) {
            Loger.e(" ffmpegRetriever error ");
        } finally {
            try {
                if (mmr != null)
                    mmr.release();
            } catch (RuntimeException e) {
                Loger.e(TAG, e);
            }
        }
        if (bitmap != null && !TextUtils.isEmpty(videoid)) {
            return saveBitmapToSD(bitmap, videoid);
        } else {
            return "";
        }
    }

    /**
     * bitmap保存到sd卡上
     *
     * @param bitmap
     * @param imageName
     * @return
     */
    public synchronized static String saveBitmapToSD(Bitmap bitmap, String imageName) {
        File dir = new File(Constants.VIDEO_PICTURE_PATH);//图片保存目录
        if (!dir.exists()) {//目录不存在
            dir.mkdirs();
        }
        FileOutputStream out = null;
        File file = null;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(imageName).append(".png");
            file = new File(dir.getPath(), sb.toString());//图片路径
            if (!file.exists())
                file.createNewFile();
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
        } catch (Exception e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        if (file != null && file.exists()) {
            return file.getPath();
        } else {
            return "";
        }
    }


    /**
     * 获取本地视频 时长
     *
     * @param playerPath 视频路径
     * @return
     */
    public synchronized static String getVideoTime(String playerPath) {
        String time = "";
        Loger.e("保存视频的路径 " + playerPath);
        if (!TextUtils.isEmpty(playerPath)) {
            File source = new File(playerPath);
            if (source.exists()) {
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                try {
                    mmr.setDataSource(source.getPath());//这里有崩溃
                    // 播放时长单位为毫秒
                    String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    long ms = Long.parseLong(duration);
                    time = String.valueOf(ms);
//                    time = getVideoDuration(ms);
                } catch (Exception e) {
//                    e.printStackTrace();
                } finally {
                    mmr.release();//释放资源
                }
            }
        }
        return time;
    }

    /**
     * 提取视频文件的 时长
     *
     * @param path 视频文件路径
     * @return
     */
    public synchronized static String getDuration(String path) {
        if (path == null
                || TextUtils.isEmpty(path)
                || !new File(path).exists())
            return "";
        String duration = "";
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(path);//这里有崩溃
            duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        } catch (Exception e) {
            Loger.e(" ffmpegRetriever error ");
        } finally {
            try {
                if (mmr != null)
                    mmr.release();
            } catch (RuntimeException e) {
                Loger.e(TAG, e);
            }
        }
        return duration;
    }

    /**
     * 视频时长的转换
     *
     * @param duration
     * @return
     */
    public synchronized static String getVideoDuration(long duration) {
//        Loger.e("视频时长的转换 duration " + duration);
        StringBuffer sb = new StringBuffer();
        int hour = (int) (duration / (60 * 60 * 1000));
        int minute = (int) (duration - hour * 60 * 60 * 1000) / (60 * 1000);
        int seconds = (int) (duration - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;

        if (seconds >= 60) {
            seconds = seconds % 60;
            minute += seconds / 60;
        }
        if (minute >= 60) {
            minute = minute % 60;
            hour += minute / 60;
        }

        String sh = "";
        String sm = "";
        String ss = "";
        if (hour < 10) {
            sh = "0" + String.valueOf(hour);
        } else {
            sh = String.valueOf(hour);
        }
        if (minute < 10) {
            sm = "0" + String.valueOf(minute);
        } else {
            sm = String.valueOf(minute);
        }
        if (seconds < 10) {
            ss = "0" + String.valueOf(seconds);
        } else {
            ss = String.valueOf(seconds);
        }
        if (!sh.equals("00"))
            return sb.append(sh).append(":").append(sm).append(":").append(ss).toString();
        else
            return sb.append(sm).append(":").append(ss).toString();
    }

    /**
     * 视频格式
     *
     * @param contentType 请求返回的视频信息
     * @return 视频格式 如 mp4
     */
    public synchronized static String videoContentType(String contentType) {
        String type = "";
        if (contentType.contains("mp4")) {
            type = ".mp4";
        } else if (contentType.contains("mpeg4")) {
            type = ".mpeg4";
        } else if (contentType.contains("mov")) {
            type = ".mov";
        } else if (contentType.contains("avi")) {
            type = ".avi";
        } else if (contentType.contains("wmv")) {
            type = ".wmv";
        } else if (contentType.contains("mpegps")) {
            type = ".mpegps";
        } else if (contentType.contains("flv")) {
            type = ".flv";
        } else if (contentType.contains("3gpp")) {
            type = ".3gpp";
        } else if (contentType.contains("webm")) {
            type = ".webm";
        } else if (contentType.contains("mkv")) {
            type = ".mkv";
        } else if (contentType.contains("mpg")) {
            type = ".mpg";
        } else if (contentType.contains("vob")) {
            type = ".vob";
        }
        return type;
    }
}