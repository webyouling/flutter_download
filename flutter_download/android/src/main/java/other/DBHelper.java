package other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by lingjianzhong on 2017/11/27.
 */

public class DBHelper extends SQLiteOpenHelper {
    private volatile static DBHelper instance = null;

    public static synchronized SQLiteDatabase getInstance(Context context) {
        if (instance == null)
            synchronized (DBHelper.class) {
                if (instance == null)
                    instance = new DBHelper(context.getApplicationContext());
            }
        return instance.getWritableDatabase();
    }

    private DBHelper(Context context) {
        super(context, Constants.DATA_BASE_DOWN, null, Constants.DATA_BASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Loger.e("onCreate: 创建表 ");
        createWeb(db);
        createHistory(db);
        createDownload(db);
        createPLAY(db);
        createM3u8TsDown(db);
        createPlayLists(db);
        createPlaylistAndDown(db);
        createBrowseHistoryMiddle(db);
        createBrowseHistroys(db);
        createPlayerHistory(db);
    }

    /**
     * 创建播放视频历史记录
     *
     * @param db
     */
    private void createPlayerHistory(SQLiteDatabase db) {
        Loger.e("createPlayerHistory: ");
        String player_history_sql = "create table if not exists "
                + Constants.TABLE_PLAYER_HISTORY + "("
                + Constants.ID_PLAYER_HISTORY + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.PLAYER_HISTORY_ID + " varchar, " // 列表id
                + Constants.PLAYER_HISTORY_VIDEO_ID + " varchar " // 视频id
                + ")";
        db.execSQL(player_history_sql);
    }

    /**
     * 浏览历史记录
     *
     * @param db
     */
    private void createBrowseHistroys(SQLiteDatabase db) {
        Loger.e("createBrowseHistroys: 浏览历史记录 ");
        String browse_history_sql = "create table if not exists "
                + Constants.TABLE_HISTORY + "("
                + Constants.ID_HISTORY + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.HISTORY_ID + " varchar, " // 列表id
                + Constants.HISTORY_NAME + " varchar, " // 网址名称
                + Constants.HISTORY_WEBSITE + " varchar, "//网址链接
                + Constants.HISTORY_TIME + " varchar, "//日期
                + Constants.HISTORY_TYPE + " int "//类型
                + ")";
        db.execSQL(browse_history_sql);
    }

    /**
     * 创建浏览历史中间表
     *
     * @param db
     */
    private void createBrowseHistoryMiddle(SQLiteDatabase db) {
        Loger.e("createBrowseHistoryMiddle: 创建浏览历史中间表");
        String browse_history_middle_sql = "create table if not exists "
                + Constants.TABLE_HISTORY_MIDDLE + "("
                + Constants.ID_HISTORY_MIDDLE + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.HISTORY_MIDDLE_ID + " varchar, " // 列表id
                + Constants.HISTORY_MIDDLE_TIME + " varchar" // 日期
                + ")";
        db.execSQL(browse_history_middle_sql);
    }

    /**
     * 播放列表和下载视频中间表
     *
     * @param db
     */
    private void createPlaylistAndDown(SQLiteDatabase db) {
        Loger.e("createPlaylistAndDown: ");
        String playlist_down_sql = "create table if not exists "
                + Constants.TABLE_PLAYLIST_DOWN + "("
                + Constants.ID_PLAYLIST_DOWN + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.PLAYLIST_DOWN_VIDEO_ID + " varchar, " // 播放列表id
                + Constants.PLAYLIST_DOWN_NAME + " varchar, " // 播放列表id
                + Constants.PLAYLIST_DOWN_ID + " varchar " // 视频id
                + ")";
        db.execSQL(playlist_down_sql);
    }

    /**
     * 创建播放列表
     *
     * @param db
     */
    private void createPlayLists(SQLiteDatabase db) {
        Loger.e("createPlayLists: ");
        String playlists_sql = "create table if not exists "
                + Constants.TABLE_PLAYLIST + "("
                + Constants.ID_PLAYLIST + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.PLAYLIST_ID + " varchar, " // 播放列表的id
                + Constants.PLAYLIST_NAME + " varchar, " // 播放列表名称
                + Constants.PLAYLIST_TIME + " varchar " // 创建列表时间
                + ")";
        db.execSQL(playlists_sql);
    }

    /**
     * 创建M3u8下载
     *
     * @param db
     */
    private void createM3u8TsDown(SQLiteDatabase db) {
        Loger.e("createM3u8TsDown: ");
        String m3u8download_sql = "create table if not exists "
                + Constants.TABLE_DOWN_M3U8 + "("
                + Constants.ID_DOWN_m3u8 + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.DOWN_M3U8_ID + " varchar, " // m3u8视频的id
                + Constants.DOWN_M3U8_NAME + " varchar, " // m3u8视频名称
                + Constants.DOWN_M3U8_URL + " varchar, " // M3U8下载视频地址
                + Constants.DOWN_M3U8_PATH + " varchar, " // M3U8保存视频地址
                + Constants.DOWN_M3U8_FILE_SIZE + " long, " // M3U8视频的总大小
                + Constants.DOWN_M3U8_SIZE + " long, " // M3U8视频的下载大小
                + Constants.DOWN_M3U8_STATE + " int" // M3U8视频下载状态
//                + Constants.DOWN_M3U8_SUPPORT_RANGE + " int, " // M3U8视频是否支持断点下载
//                + Constants.DOWN_M3U8_SECONDS + " float" // M3U8片段时间长度
                + ");";
        db.execSQL(m3u8download_sql);
    }

    /**
     * 创建视频下载表
     *
     * @param db
     */
    private void createPLAY(SQLiteDatabase db) {
        String download_sql = "create table if not exists "
                + Constants.PLAY + "(" // 创建视频下载表
                + Constants.ID_DOWN + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.DOWN_ID + " varchar, " // 视频的id
                + Constants.DOWN_NAME + " varchar, " // 视频的名称
                + Constants.DOWN_ICON + " varchar, " // 视频的图片
                + Constants.DOWN_URL + " varchar, " // 下载视频地址
                + Constants.DOWN_FILE_PATH + " varchar, " // 视频保存路径
                + Constants.DOWN_TOTAL_SIZE + " long, " // 视频的总大小
                + Constants.DOWN_SIZE + " long, " // 视频的下载大小
                + Constants.DOWN_SPEED + " long, " // 下载速度
                + Constants.DOWN_STATE + " int, " // 下载状态
                + Constants.CURRENT_TS_INDEX + " int, " //当前ts的下标
                + Constants.DOWN_TIME + " varchar, " //视频下载完成时间
                + Constants.DOWN_VIDEO_NAME + " varchar, " //保存视频名称
                + Constants.DOWN_TYPE + " int, "//是否从下载列表中删除
                + Constants.DOWN_VIDEO_TIME + " varchar, "//视频时长
                + Constants.DOWN_PICTURE + " varchar, "//视频图片
                + Constants.DOWN_WEB_URL + " varchar, "//下载视频网站地址
                + Constants.DOWN_MIME_TYPE + " varchar "//文件格式
                + ");";
        db.execSQL(download_sql);
    }

    /**
     * 创建视频下载表
     *
     * @param db
     */
    private void createDownload(SQLiteDatabase db) {
        Loger.e("createDownload: ");
        String download_sql = "create table if not exists "
                + Constants.TABLE_DOWN + "(" // 创建视频下载表
                + Constants.ID_DOWN + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.DOWN_ID + " varchar, " // 视频的id
                + Constants.DOWN_NAME + " varchar, " // 视频的名称
                + Constants.DOWN_ICON + " varchar, " // 视频的图片
                + Constants.DOWN_URL + " varchar, " // 下载视频地址
                + Constants.DOWN_FILE_PATH + " varchar, " // 视频保存路径
                + Constants.DOWN_TOTAL_SIZE + " long, " // 视频的总大小
                + Constants.DOWN_SIZE + " long, " // 视频的下载大小
                + Constants.DOWN_SPEED + " long, " // 下载速度
                + Constants.DOWN_STATE + " int, " // 下载状态
                + Constants.CURRENT_TS_INDEX + " int, " //当前ts的下标
                + Constants.DOWN_TIME + " varchar, " //视频下载完成时间
                + Constants.DOWN_VIDEO_NAME + " varchar, " //保存视频名称
                + Constants.DOWN_TYPE + " int, "//是否下载完成
                + Constants.DOWN_VIDEO_TIME + " varchar, "//视频时长
                + Constants.DOWN_PICTURE + " varchar, "//视频图片
                + Constants.DOWN_WEB_URL + " varchar, "//下载视频网站地址
                + Constants.DOWN_MIME_TYPE + " varchar, "//文件格式
                + Constants.DOWNLOAD_ID + " int, "//下载id
                + Constants.DOWNLOAD_THREAD_NUMBER + " int "//下载id
                + ");";
        db.execSQL(download_sql);
    }

    /**
     * 创建搜索记录表
     *
     * @param db
     */
    private void createHistory(SQLiteDatabase db) {
        Loger.e("createHistory: ");
        String history_sql = "create table if not exists "
                + Constants.TABLE_HIStTORY + "(" // 创建搜索记录表
                + Constants.ID_HIStTORY + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.HIStTORY_CONTENT + " varchar, " // 内容
                + Constants.HIStTORY_TIME + " varchar, " // 时间
                + Constants.HIStTORY_TYPE + " varchar " // 时间
                + ");";
        db.execSQL(history_sql);
    }

    /**
     * 创建网站收藏表
     *
     * @param db
     */
    private void createWeb(SQLiteDatabase db) {
        Loger.e("createWeb: ");
        String web_sql = "create table if not exists "
                + Constants.TABLE_WEB + "(" // 创建网站收藏表
                + Constants.ID_WEB + " integer PRIMARY KEY autoincrement, " // 自增长id.
                + Constants.WEB_ID + " long, "//ID
                + Constants.WEB_NAME + " varchar, " // 网站名称
                + Constants.WEB_URL + " varchar, " // 网站链接
                + Constants.WEB_ICO_URL + " varchar, " // 网站图标
                + Constants.WEB_TYPE + " int," // 0 默认是网站 1 是广告
                + Constants.WEB_COLOR + " int" // 颜色
                + ");";
        db.execSQL(web_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                createPLAY(db);
            case 2:
                if (!checkColumnExists(db, Constants.TABLE_DOWN, Constants.CURRENT_TS_INDEX))
                    db.execSQL("alter table " + Constants.TABLE_DOWN + " add column " + Constants.CURRENT_TS_INDEX + " int");
                if (!checkColumnExists(db, Constants.TABLE_DOWN, Constants.DOWN_MIME_TYPE))
                    db.execSQL("alter table " + Constants.TABLE_DOWN + " add column " + Constants.DOWN_MIME_TYPE + " varchar");
                if (!checkColumnExists(db, Constants.TABLE_DOWN, Constants.DOWNLOAD_ID))
                    db.execSQL("alter table " + Constants.TABLE_DOWN + " add column " + Constants.DOWNLOAD_ID + " int");
                if (!checkColumnExists(db, Constants.TABLE_DOWN, Constants.DOWNLOAD_THREAD_NUMBER))
                    db.execSQL("alter table " + Constants.TABLE_DOWN + " add column " + Constants.DOWNLOAD_THREAD_NUMBER + " int");
            default:

        }
    }


    /**
     * 检查表中某列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return true:存在  false：不存在
     */
    private boolean checkColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
            Loger.e("checkColumnExists2..." + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 查询数据库的指定表中的指定数据.
     *
     * @param table         表名.
     * @param columns       查询字段.
     * @param selection     条件字段.
     * @param selectionArgs 条件值.
     * @param groupBy       分组名称.
     * @param having        分组条件.与groupBy配合使用.
     * @param orderBy       排序字段.
     * @param limit         分页.
     * @return 查询结果游标
     */
    static synchronized Cursor selectInfo(Context context, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return getInstance(context).query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit); // 执行查询操作.
    }

    static synchronized Cursor rawQuery(Context context, String sql) {
        return getInstance(context).rawQuery(sql, null);
    }


    /**
     * 修改数据库的指定表中的指定数据.
     *
     * @param needClose   是否需要关闭数据库连接.true为关闭,否则不关闭.
     * @param table       表名.
     * @param titles      字段名.
     * @param values      数据值.
     * @param conditions  条件字段.
     * @param whereValues 条件值.
     * @return 若传入的字段名与插入值的长度不等则返回false, 否则执行成功则返回true.
     */
    static synchronized boolean updateInfo(Context context, boolean needClose, String table, String[] titles, String[] values, String conditions, String[] whereValues) {
        if (titles.length != values.length) {
            return false;
        } else {
            if (getInstance(context).isOpen()) {
                // 将插入值与对应字段放入ContentValues实例中
                ContentValues contentValues = new ContentValues();
                int length = titles.length;
                for (int i = 0; i < length; i++) {
                    if (values[i] == null)
                        continue;
                    contentValues.put(titles[i], values[i]);
                }
                getInstance(context).update(table, contentValues, conditions, whereValues); // 执行修改操作
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 删除数据库的指定表中的指定数据.
     *
     * @param needClose   是否需要关闭数据库连接.true为关闭,否则不关闭.
     * @param table       表名.
     * @param conditions  条件字段.
     * @param whereValues 条件值.
     */
    static synchronized void deleteInfo(Context context, boolean needClose, String table, String conditions, String[] whereValues) {
        getInstance(context).delete(table, conditions, whereValues); // 执行删除操作
    }

    /**
     * 向数据库的指定表中插入数据.
     *
     * @param needClose 是否需要关闭数据库连接.true为关闭,否则不关闭.
     * @param table     表名.
     * @param titles    字段名.
     * @param values    数据值.
     * @return 若传入的字段名与插入值的长度不等则返回false, 否则执行成功则返回true.
     */
    static synchronized boolean insertInfo(Context context, boolean needClose, String table, String[] titles, String[] values) {
        if (titles.length != values.length) { // 判断传入的字段名数量与插入数据的数量是否相等
            return false;
        } else {
            // 将插入值与对应字段放入ContentValues实例中
            ContentValues contentValues = new ContentValues();
            int length = titles.length;
            for (int i = 0; i < length; i++) {
                if (values[i] == null)
                    continue;
                contentValues.put(titles[i], values[i]);
            }
            long insert = getInstance(context).insert(table, null, contentValues);// 执行插入操作
            return true;
        }
    }

    /**
     * 删除表中所有数据
     *
     * @param context
     * @param table
     */
    static synchronized void deleteTableAll(Context context, String table) {
        getInstance(context).delete(table, null, null);
    }
}