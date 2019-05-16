package other;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import downloader.core.DefaultExecutorSupplier;

/**
 * Created by lingjianzhong on 2017/11/30.
 * 视频下载相关
 */

public class DownloadBeanUtil {
    private static String table = Constants.TABLE_DOWN;// 表名

    /**
     * 字段名对应字段值
     **/
    private static String[] titles = new String[]{
            Constants.DOWN_ID,
            Constants.DOWN_NAME,
            Constants.DOWN_ICON,
            Constants.DOWN_URL,
            Constants.DOWN_FILE_PATH,
            Constants.DOWN_TOTAL_SIZE,
            Constants.DOWN_SIZE,
            Constants.DOWN_SPEED,
            Constants.DOWN_STATE,
            Constants.CURRENT_TS_INDEX,
            Constants.DOWN_TIME,
            Constants.DOWN_VIDEO_NAME,
            Constants.DOWN_TYPE,
            Constants.DOWN_VIDEO_TIME,
            Constants.DOWN_PICTURE,
            Constants.DOWN_WEB_URL,
            Constants.DOWN_MIME_TYPE,
            Constants.DOWNLOAD_ID,
            Constants.DOWNLOAD_THREAD_NUMBER
    };

    /**
     * 插入下载视频数据
     */

    public static synchronized boolean insertDown(Context context, DownLoadBean bean) {
        return DBHelper.insertInfo(context, true, table, titles, getValues(bean));
    }

    /**
     * 根据id获取视频数据
     *
     * @param context 上下文
     * @param id      视频id = 时间戳
     * @return 单个视频下载信息
     */
    public synchronized static DownLoadBean getDownLoadById(Context context, String id) {
        Cursor cursor = DBHelper.selectInfo(context, table
                , new String[]{"*"}, Constants.DOWN_ID + " = ? "
                , new String[]{id}, null, null
                , null, null);
        return getDownLoadBean(cursor);
    }

    /**
     * 根据downloadid获取视频数据
     *
     * @param context 上下文
     * @param dlId
     * @return 单个视频下载信息
     */
    public synchronized static DownLoadBean getDownLoadByDownloadId(Context context, String dlId) {
        DownLoadBean dlBean = null;
        try {
            Cursor cursor = DBHelper.selectInfo(context, table
                    , new String[]{"*"}, Constants.DOWNLOAD_ID + " = ? "
                    , new String[]{dlId}, null, null
                    , null, null);
            dlBean = getDownLoadBean(cursor);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dlBean;
    }

    /**
     * 根据视频url获取单个视频信息
     */
    public synchronized static DownLoadBean findDownLoadByUrl(Context context, String url) {
        Cursor cursor = DBHelper.selectInfo(context, table
                , new String[]{"*"}, Constants.DOWN_URL + " = ? "
                , new String[]{url}, null, null
                , null, null);
        return getDownLoadBean(cursor);
    }

    /**
     * 根据视频url获取下载完成的视频信息
     */
    public synchronized static DownLoadBean findVideoByUrl(Context context, String url) {
        Cursor cursor = DBHelper.selectInfo(context, table,
                new String[]{"*"}, Constants.DOWN_URL + " = ? and "
                        + Constants.DOWN_STATE + " = ? ",
                new String[]{url, String.valueOf(DownLoadState.STATE_DOWNLOADED)}
                , null, null, null, null);
        return getDownLoadBean(cursor);
    }

    /**
     * 获取所有未下载完成的视频数据
     */
    public synchronized static List<DownLoadBean> getDownLoad(Context context) {
        Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"},
                Constants.DOWN_TYPE + " = ? "
                , new String[]{String.valueOf(0)}
                , null, null
                , Constants.ID_DOWN + " ASC", null);
        return getListDownLoadBean(cursor);
    }

    public synchronized static int getCount(Context context) {
        Cursor cursor = DBHelper.rawQuery(context, "SELECT count(*) FROM " + table + " WHERE " + Constants.DOWN_TYPE + " = " + 0);
        int count = 0;
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }


    /**
     * 修改视频数据
     */
    public synchronized static void UpdateDownLoadById(Context context, DownLoadBean bean) {
        String[] values = getValues(bean);
        DBHelper.updateInfo(context, true, table
                , titles, values
                , Constants.DOWN_ID + " =? "
                , new String[]{bean.id});
//        DBHelper.updateInfo(context, true, table, titles, values, Constants.DOWN_ID + " =? ", new String[]{bean.id});

    }

    /**
     * 一个ts读取完成，记录在数据库
     * 记录下载的断点
     */
    public synchronized static void updateTsIndex(Context context, DownLoadBean bean) {
        String[] titles = new String[]{
                Constants.DOWN_TOTAL_SIZE,
                Constants.DOWN_SIZE,
                Constants.DOWN_STATE,
                Constants.CURRENT_TS_INDEX,
                Constants.DOWNLOAD_THREAD_NUMBER
        };

        String[] values = new String[]{
                bean.totalSize + "",
                bean.currentSize + "",
                bean.downloadState + "",
                bean.currentTsIndex + "",
                DefaultExecutorSupplier.MAX_NUM_THREADS + ""};
//        Loger.e("bean=" + bean.totalSize + "  " + bean.currentSize + "  " + bean.downloadState + "  " + bean.currentTsIndex);

        DBHelper.updateInfo(context, true, table, titles, values, Constants.DOWN_ID + " =? ", new String[]{bean.id});
    }

    /**
     * 记录视频状态
     */
    public synchronized static void updateState(Context context, int status, String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DOWN_STATE, status);
        DBHelper.getInstance(context).update(table, contentValues, Constants.DOWN_ID + " =? ", new String[]{id});
    }

    /**
     * 记录下载状态
     */
    public synchronized static void updateTsSize(Context context, long totalSize, String id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.DOWN_TOTAL_SIZE, totalSize);
        contentValues.put(Constants.DOWNLOAD_THREAD_NUMBER, DefaultExecutorSupplier.MAX_NUM_THREADS);
        DBHelper.getInstance(context).update(table, contentValues, Constants.DOWN_ID + " =? ", new String[]{id});
    }

    /**
     * 根据id删除视频数据 id=时间戳
     */
    public synchronized static void DeleteDownLoadById(Context context, String id) {
        DBHelper.deleteInfo(context, true, table
                , Constants.DOWN_ID + " =? "
                , new String[]{id});
    }

    /**
     * 删除所有未下载完成的视频
     */
    public synchronized static void DeletDownLoad(Context context) {
        DBHelper.deleteInfo(context, true, table
                , Constants.DOWN_TYPE + " =? "
                , new String[]{String.valueOf(0)});
    }

    /**
     * 删除所有下载完成的视频
     */
    public synchronized static void DeletDownLoadVideo(Context context) {
        DBHelper.deleteInfo(context, true, table
                , Constants.DOWN_TYPE + " =? "
                , new String[]{String.valueOf(1)});
    }

    /**
     * 删除所有视频数据
     */
    public synchronized static void DeleteDownLoadAll(Context context) {
        DBHelper.deleteTableAll(context, table);
    }

    /**
     * 获取下载完的视频
     */
    public synchronized static List<DownLoadBean> getDownVideo(Context context) {
        Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"}
                , Constants.DOWN_STATE + " = ? "
                , new String[]{String.valueOf(DownLoadState.STATE_DOWNLOADED)}
                , null, null, null, null);
        return getListDownLoadBean(cursor);
    }

    /**
     * 获取所有下载视频数据(下载完成和未下载完成)
     */
    public synchronized static List<DownLoadBean> getDownLoadVideoAll(Context context) {
        Cursor cursor = DBHelper.selectInfo(context, table
                , new String[]{"*"}, null
                , null, null, null
                , null, null);
        return getListDownLoadBean(cursor);
    }

    /**
     * 条件查询(下载完成的视频)
     */
    public synchronized static List<DownLoadBean> getVideoSaveVideoByCondition(Context context, String condition, boolean isAdd) {
        String cond = QueryConditions(context, condition);//获取排序条件
        if (isAdd) {
            Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"}
                    , Constants.DOWN_STATE + " = ? ",
                    new String[]{String.valueOf(DownLoadState.STATE_DOWNLOADED)}, null, null, cond, null);
            return getListDownLoadBean(cursor);
        } else {
            Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"}
                    , Constants.DOWN_STATE + " = ? and " + Constants.DOWN_NAME + " like ?",
                    new String[]{String.valueOf(DownLoadState.STATE_DOWNLOADED), "%" + ".mp4" + "%"}, null, null, cond, null);
            return getListDownLoadBean(cursor);
        }
    }

    /**
     * 模糊查询+条件查询(下载完成的视频)
     */
    public synchronized static List<DownLoadBean> getVideoSaveVideoLikeByCondition(Context context, String search, String condition) {
        String cond = QueryConditions(context, condition);//获取排序条件
        Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"},
                Constants.DOWN_STATE + " = ? and "
                        + Constants.DOWN_NAME + " LIKE ? ",
                new String[]{String.valueOf(DownLoadState.STATE_DOWNLOADED), "%" + search + "%"},
                null, null, cond, null);
        return getListDownLoadBean(cursor);
    }

    /**
     * 组合查询条件
     */
    public synchronized static String QueryConditions(Context context, String condition) {
//        StringBuilder sb = new StringBuilder();
//        Resources resources = context.getResources();
//        if (condition.equals(resources.getString(R.string.MSG12003))) {
//            sb.append(Constants.DOWN_TIME).append(" desc");
//            return sb.toString();
//        } else if (condition.equals(ReplaceUtil.replaceProhibited(context, resources.getString(R.string.MSG12004)))) {
//            sb.append("CAST(").append(Constants.DOWN_VIDEO_TIME)
//                    .append(" as SIGNED ").append(")").append(" desc");
//            return sb.toString();
//        } else if (condition.equals(resources.getString(R.string.MSG12005))) {
//            String nameSort = " CASE WHEN %1$s GLOB '[a-zA-Z]*' THEN %2$s END ASC, %3$s + 0 ASC";
//            nameSort = String.format(nameSort, Constants.DOWN_NAME, Constants.DOWN_NAME, Constants.DOWN_NAME);
//            return nameSort;
//        }
        return "";
    }

    /**
     * 获取当个视频
     */
    private synchronized static DownLoadBean getDownLoadBean(Cursor cursor) {
        DownLoadBean bean = null;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                bean = new DownLoadBean();
                bean.id = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_ID));
                bean.name = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_NAME));
                bean.icon = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_ICON));
                bean.url = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_URL));
                bean.path = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_FILE_PATH));
                bean.totalSize = cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_TOTAL_SIZE));
                bean.currentSize = cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_SIZE));
                bean.currentSpeed = cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_SPEED));
                bean.downloadState = cursor.getInt(cursor
                        .getColumnIndex(Constants.DOWN_STATE));
                bean.currentTsIndex = cursor.getInt(cursor
                        .getColumnIndex(Constants.CURRENT_TS_INDEX));
                bean.time = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_TIME));
                bean.videoName = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_VIDEO_NAME));
                bean.type = cursor.getInt(cursor
                        .getColumnIndex(Constants.DOWN_TYPE));
                bean.videoTime = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_VIDEO_TIME));
                bean.picture = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_PICTURE));
                bean.web_url = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_WEB_URL));
                bean.fileFormat = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_MIME_TYPE));
                try {
                    String dlid = cursor.getString(cursor.getColumnIndex(Constants.DOWNLOAD_ID));
                    if (!TextUtils.isEmpty(dlid) && !"null".equals(dlid)) {
                        bean.downloadId = Integer.parseInt(dlid);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        if (cursor != null)
            cursor.close();
        return bean;
    }

    /**
     * 获取视频集合
     */
    private synchronized static List<DownLoadBean> getListDownLoadBean(Cursor cursor) {
        List<DownLoadBean> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                DownLoadBean bean = new DownLoadBean();
                bean.id = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_ID));
                bean.name = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_NAME));
                bean.icon = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_ICON));
                bean.url = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_URL));
                bean.path = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_FILE_PATH));
                bean.totalSize = cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_TOTAL_SIZE));
                bean.currentSize = cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_SIZE));
                bean.currentSpeed = cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_SPEED));
                bean.downloadState = cursor.getInt(cursor
                        .getColumnIndex(Constants.DOWN_STATE));
                bean.currentTsIndex = cursor.getInt(cursor
                        .getColumnIndex(Constants.CURRENT_TS_INDEX));
                bean.time = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_TIME));
                bean.videoName = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_VIDEO_NAME));
                bean.type = cursor.getInt(cursor
                        .getColumnIndex(Constants.DOWN_TYPE));
                bean.videoTime = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_VIDEO_TIME));
                bean.picture = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_PICTURE));
                bean.web_url = cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_WEB_URL));
                try {
                    bean.fileFormat = cursor.getString(cursor.getColumnIndex(Constants.DOWN_MIME_TYPE));
                    bean.downloadId = cursor.getInt(cursor.getColumnIndex(Constants.DOWNLOAD_ID));
                    bean.threadNumber = cursor.getInt(cursor.getColumnIndex(Constants.DOWNLOAD_THREAD_NUMBER));
                } catch (Exception ignored) {
                }
                list.add(bean);
            }
//            cursor.close();
        }
        if (cursor != null)
            cursor.close();
        return list;
    }

    /**
     * 设置对应字段的值
     */
    private synchronized static String[] getValues(DownLoadBean bean) {
        /** 字段值对应字段名 **/
        return new String[]{
                bean.id + "",
                bean.name + "",
                bean.icon + "",
                bean.url + "",
                bean.path + "",
                bean.totalSize + "",
                bean.currentSize + "",
                bean.currentSpeed + "",
                bean.downloadState + "",
                bean.currentTsIndex + "",
                bean.time + "",
                bean.videoName + "",
                bean.type + "",
                bean.videoTime + "",
                bean.picture + "",
                bean.web_url + "",
                bean.fileFormat + "",
                bean.downloadId + "",
                bean.threadNumber + ""
        };
    }
}