package other;

import android.os.Environment;

import java.io.File;

/**
 * Created by lingjianzhong on 2017/11/27.
 * 常量类
 */

public class Constants {
    /**
     * 数据库版本
     */
    public static final int DATA_BASE_VERSION = 3;

    /**
     * 数据库名字
     */
    public static final String DATA_BASE_DOWN = "video_downloader.db";
    /**
     * 默认视频下载数
     */
    public static final int SAVE_VIDEO_NUMBER = 5;
    /**
     * User-Agent
     */
    public static final String IPHONE_UA = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1";

    //搜藏网站实体
    //表名
    public static final String TABLE_WEB = "table_web";
    //自增ID
    public static final String ID_WEB = "_id";
    //id
    public static final String WEB_ID = "web_id";
    //网址名称
    public static final String WEB_NAME = "web_name";
    //网址链接
    public static final String WEB_URL = "web_url";
    //网址logo链接
    public static final String WEB_ICO_URL = "web_ico_url";
    //类型 0 网址 1 广告
    public static final String WEB_TYPE = "type";
    //颜色
    public static final String WEB_COLOR = "web_color";

    //搜索历史记录
    //表名
    public static final String TABLE_HIStTORY = "table_history";
    //自增ID
    public static final String ID_HIStTORY = "_id";
    //搜索内容
    public static final String HIStTORY_CONTENT = "content";
    //搜索时间
    public static final String HIStTORY_TIME = "time";
    //搜索类型
    public static final String HIStTORY_TYPE = "type";

    //视频下载
    // 下载表
    public static final String TABLE_DOWN = "table_down";
    // 下载表
    public static final String PLAY = "play";
    // 自增长id
    public static final String ID_DOWN = "_id";
    // 下载id
    public static final String DOWN_ID = "down_id";
    // 视频的名称
    public static final String DOWN_NAME = "down_name";
    // 视频的图片
    public static final String DOWN_ICON = "down_icon";
    // 下载视频地址
    public static final String DOWN_URL = "down_url";
    // 视频保存路径
    public static final String DOWN_FILE_PATH = "down_file_path";
    // 视频的总大小
    public static final String DOWN_TOTAL_SIZE = "down_file_size";
    // 视频的下载大小
    public static final String DOWN_SIZE = "down_size";
    // 视频的下载速度
    public static final String DOWN_SPEED = "down_speed";
    // 下载状态
    public static final String DOWN_STATE = "down_state";
    // 下载完成的时间
    public static final String DOWN_TIME = "down_time";
    // 保存视频名称
    public static final String DOWN_VIDEO_NAME = "down_videoName";
    // 是否从下载完成 1=完成 0=未完成
    public static final String DOWN_TYPE = "type";
    // 视频时长
    public static final String DOWN_VIDEO_TIME = "videotime";
    // 视频图片
    public static final String DOWN_PICTURE = "picture";
    //下载视频网站地址
    public static final String DOWN_WEB_URL = "web_url";
    //文件格式
    public static final String DOWN_MIME_TYPE = "mime_type";
    // 当前ts的下标
    public static final String CURRENT_TS_INDEX = "down_support_range";
    // 下载id
    public static final String DOWNLOAD_ID = "download_id";
    // 下载线程数
    public static final String DOWNLOAD_THREAD_NUMBER = "thread_number";


    //m3u8视频下载
    // 下载表
    public static final String TABLE_DOWN_M3U8 = "table_down_m3u8";
    // 自增长id
    public static final String ID_DOWN_m3u8 = "_id";
    // 下载id
    public static final String DOWN_M3U8_ID = "down_m3u8_id";
    // M3U8视频的名称
    public static final String DOWN_M3U8_NAME = "down_m3u8_name";
    // M3U8下载视频地址
    public static final String DOWN_M3U8_URL = "down_m3u8_url";
    // M3U8保存视频地址
    public static final String DOWN_M3U8_PATH = "down_m3u8_path";
    // M3U8视频的总大小
    public static final String DOWN_M3U8_FILE_SIZE = "down_m3u8_file_size";
    // M3U8视频的下载大小
    public static final String DOWN_M3U8_SIZE = "down_m3u8_size";
    // M3U8视频下载状态
    public static final String DOWN_M3U8_STATE = "down_m3u8_state";
    // M3U8视频是否支持断点下载
    public static final String DOWN_M3U8_SUPPORT_RANGE = "down_m3u8_range";
    //M3U8片段时长
    public static final String DOWN_M3U8_SECONDS = "down_m3u8_seconds";
//    //当前ts下载成功
//    public static final String DOWN_LOAD_OVER = "down_load_over";

    //播放列表
    // 播放列表
    public static final String TABLE_PLAYLIST = "table_playlist";
    // 自增长id
    public static final String ID_PLAYLIST = "_id";
    //播放列表id
    public static final String PLAYLIST_ID = "id";
    // 播放列表名称
    public static final String PLAYLIST_NAME = "playlist_name";
    // 播放列表视频数目
//    public static final String PLAYLIST_COUNT = "playlist_count";
    // 创建列表时间
    public static final String PLAYLIST_TIME = "playlist_time";

    //播放列表和下载视频中间表
    // 中间表
    public static final String TABLE_PLAYLIST_DOWN = "table_playlist_down";
    // 自增长id
    public static final String ID_PLAYLIST_DOWN = "_id";

    public static final String PLAYLIST_DOWN_VIDEO_ID = "id";
    // 播放列表id
    public static final String PLAYLIST_DOWN_NAME = "playlist_id";
    // 视频id
    public static final String PLAYLIST_DOWN_ID = "down_id";


    //浏览历史表
    //表
    public static final String TABLE_HISTORY = "table_browser_history";
    // 自增长id
    public static final String ID_HISTORY = "_id";
    //id
    public static final String HISTORY_ID = "id";
    //网址名称
    public static final String HISTORY_NAME = "name";
    //网址链接
    public static final String HISTORY_WEBSITE = "website";
    //网址日期
    public static final String HISTORY_TIME = "time";
    //网址类型
    public static final String HISTORY_TYPE = "type";

    //浏览历史中间表
    // 中间表
    public static final String TABLE_HISTORY_MIDDLE = "table_browser_history_middle";
    // 自增长id
    public static final String ID_HISTORY_MIDDLE = "_id";
    //id
    public static final String HISTORY_MIDDLE_ID = "id";
    //浏览日期
    public static final String HISTORY_MIDDLE_TIME = "time";

    //播放视频历史记录表
    //表
    public static final String TABLE_PLAYER_HISTORY = "table_player_history";
    // 自增长id
    public static final String ID_PLAYER_HISTORY = "_id";
    //id
    public static final String PLAYER_HISTORY_ID = "id";
    //视频id
    public static final String PLAYER_HISTORY_VIDEO_ID = "videoId";

    // 下载的实体类key
    public static final String KEY_DOWNLOAD_ENTRY = "key_download_entry";
    // 下载操作状态key
    public static final String KEY_OPERATING_STATE = "key_operating_state";

    public static final int CONNECT_TIME = 60 * 1000;
    public static final int READ_TIME = 60 * 1000;

    /**
     * 默认保存视频地址
     */
    public static final String PATH_BASE = Environment.getExternalStorageDirectory().getPath() + File.separator + "DownLoadFile" + File.separator;

    //M3U8临时下载目录
    public static final String M3U8TempDir = Environment.getExternalStorageDirectory().getPath() + File.separator;
    //视频图片保存地址
    public static final String VIDEO_PICTURE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "videoPicture" + File.separator;
    /**
     * 跳转请求码
     */
    public static final int REQUESTCODE = 0;
    public static final int SEARCH_ENGINE_RESULTCODE = 4;
    /**
     * 搜索引擎变量
     */
    public static final int SEARCH_ENGINE = 6;
    public static final int WEB_RESULTCODE = 5;


    public static final String UPDATA_URL = "http://x.lbfaoi0103u103u9791051.xyz/small-app-api/index.php/api2/ver?";


    //AdMob广告 测试-应用ID
    public static final String APP_ID_DEBUG = "ca-app-pub-3940256099942544~3347511713";
    //AdMob广告 正式-应用ID
    public static final String APP_ID_RELEASE = "ca-app-pub-5236624923943573~6618852667";

    //AdMob广告 自动-测试-单元ID
    public static final String AUTOMATIC_AD_UNIT_ID_DEBUG = "ca-app-pub-3940256099942544/2247696110";
    //AdMob广告 自动-正式-单元ID
    public static final String AUTOMATIC_AD_UNIT_ID_RELEASE = "ca-app-pub-5236624923943573/9524217602";

    //AdMob广告 横幅-测试-单元ID
    public static final String BANNER_AD_UNIT_ID_DEBUG = "ca-app-pub-3940256099942544/6300978111";
    //AdMob广告 横幅-正式-单元ID
    public static final String BANNER_AD_UNIT_ID_RELEASE = "ca-app-pub-5236624923943573/1861350003";

    //AdMob广告 全屏-测试-单元ID
    public static final String INTERSTITIAL_AD_UNIT_ID_DEBUG = "ca-app-pub-3940256099942544/1033173712";
    //AdMob广告 全屏-正式-单元ID
    public static final String INTERSTITIAL_AD_UNIT_ID_RELEASE = "ca-app-pub-5236624923943573/8287157551";

    //友盟APPKEY
    public static final String UM_APP_KEY_DEBUG = "5a7becaca40fa340c8000038";
    public static final String UM_APP_KEY_RELEASE = "5a65913cb27b0a4bba000063";

    public static final int UPDATE_PROGRESS = 0x01;
    public static final int UPDATE_OTHER = 0x02;
    public static final String RANGE = "Range";
    public static final String ETAG = "ETag";
    public static final String USER_AGENT = "User-Agent";
    public static final String DEFAULT_USER_AGENT = "PRDownloader";
    public static final int DEFAULT_READ_TIMEOUT_IN_MILLS = 20_000;
    public static final int DEFAULT_CONNECT_TIMEOUT_IN_MILLS = 20_000;
    public static final int HTTP_RANGE_NOT_SATISFIABLE = 416;
    public static final int HTTP_TEMPORARY_REDIRECT = 307;
    public static final int HTTP_PERMANENT_REDIRECT = 308;
    public static final String M3U8 = "m3u8";
    public static final String MP4 = "mp4";
    //BrowserFragment每页显示的最大的数量
    public static final int BROWSER_VISIBLE_PAGE_SIZE = 6;
    //视频Item插入广告位置（每2个视频插入一个广告）
    public static final int VIDEO_ADV_PAGE_SIZE = 3;
    //图片轮播资源文件
//    public static final Integer[] images_en = {
//            R.mipmap.vd_home_banner_1_en,
//            R.mipmap.vd_home_banner_2_en,
//            R.mipmap.vd_home_banner_3_en};
    //图片轮播资源文件
//    public static final Integer[] images_jp = {
//            R.mipmap.vd_home_banner_1_jp,
//            R.mipmap.vd_home_banner_2_jp,
//            R.mipmap.vd_home_banner_3_jp};
    //item广告
    public static final String VIDEO_ITEM_ADV = "VIDEO_ITEM_ADV";
    //四角广告
    public static final String VIDEO_FOUR_ADV = "VIDEO_FOUR_ADV";
    //自动轮播时间
    public static final int BANNER_TIME = 5 * 1000;
    public static String icon = Constants.VIDEO_PICTURE_PATH + "/five_star.png";




    public static final String EVENT_VIDEO_TO_AUDIO = "video_to_audio";//视频转音频

}