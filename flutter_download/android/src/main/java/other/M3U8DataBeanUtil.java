package other;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjianzhong on 2017/12/4.
 * m3u8下载
 */

public class M3U8DataBeanUtil {
    private static String table = Constants.TABLE_DOWN_M3U8;// m3u8表名

    /**
     * 插入M3U8下载数据
     *
     * @param context 上下文
     * @param bean    视频实体类
     * @return
     */
    public synchronized static void insertM3U8Down(Context context, M3U8Ts bean) {
        /** 字段名对应字段值 **/
        String[] titles = new String[]{
                Constants.DOWN_M3U8_ID,
                Constants.DOWN_M3U8_NAME,
                Constants.DOWN_M3U8_URL,
                Constants.DOWN_M3U8_PATH,
                Constants.DOWN_M3U8_FILE_SIZE,
                Constants.DOWN_M3U8_SIZE,
                Constants.DOWN_M3U8_STATE
        };

        /** 字段值对应字段名 **/
        String[] values = new String[]{
                bean.getM3u8Url() + "",
                bean.getTsName() + "",
                bean.getTsUrl() + "",
                bean.getTsPath() + "",
                bean.getTsTotalSize() + "",
                bean.getTsCurrentSize() + "",
                bean.getTsDownloadState() + ""
        };

        DBHelper.insertInfo(context, true, table, titles, values);
    }

    /**
     * 获取所有M3U8视频数据
     *
     * @param context 上下文
     * @return 获取所有视频数据
     */
    public synchronized static List<M3U8Ts> getM3U8DownLoad(Context context, String id) {
        List<M3U8Ts> list = new ArrayList<>();
        Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"}, Constants.DOWN_M3U8_ID + " = ? ", new String[]{id}, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                M3U8Ts bean = new M3U8Ts();
                bean.setM3u8Url(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_ID)));
                bean.setTsName(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_NAME)));
                bean.setTsUrl(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_URL)));
                bean.setTsPath(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_PATH)));
                bean.setTsTotalSize(cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_FILE_SIZE)));
                bean.setTsCurrentSize(cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_SIZE)));
                bean.setTsDownloadState(cursor.getInt(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_STATE)));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 获取所有M3U8视频数据
     *
     * @param context 上下文
     * @return 获取所有视频数据
     */
    public synchronized static List<M3U8Ts> getM3U8DownLoad(Context context) {
        List<M3U8Ts> list = new ArrayList<M3U8Ts>();
        Cursor cursor = DBHelper.selectInfo(context, table, new String[]{"*"}, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                M3U8Ts bean = new M3U8Ts();
                bean.setM3u8Url(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_ID)));
                bean.setTsName(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_NAME)));
                bean.setTsUrl(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_URL)));
                bean.setTsPath(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_PATH)));
                bean.setTsTotalSize(cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_FILE_SIZE)));
                bean.setTsCurrentSize(cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_SIZE)));
                bean.setTsDownloadState(cursor.getInt(cursor.getColumnIndex(Constants.DOWN_M3U8_STATE)));
//                bean.setIsTsSupportRange(cursor.getInt(cursor
//                        .getColumnIndex(Constants.DOWN_M3U8_SUPPORT_RANGE)));
//                bean.setTsSeconds(cursor.getFloat(cursor
//                        .getColumnIndex(Constants.DOWN_M3U8_SECONDS)));
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }

    /**
     * 修改M3U8下载视频数据
     *
     * @param context 上下文
     * @param bean    实体
     */
    public synchronized static void UpdateM3U8DownById(Context context, M3U8Ts bean) {
        /** 字段名对应字段值 **/
        String[] titles = new String[]{
                Constants.DOWN_M3U8_ID,
                Constants.DOWN_M3U8_NAME,
                Constants.DOWN_M3U8_URL,
                Constants.DOWN_M3U8_PATH,
                Constants.DOWN_M3U8_FILE_SIZE,
                Constants.DOWN_M3U8_SIZE,
                Constants.DOWN_M3U8_STATE
//                ,
//                Constants.DOWN_M3U8_SUPPORT_RANGE,
//                Constants.DOWN_M3U8_SECONDS
        };

        /** 字段值对应字段名 **/
        String[] values = new String[]{
                bean.getM3u8Url() + "",
                bean.getTsName() + "",
                bean.getTsUrl() + "",
                bean.getTsPath() + "",
                bean.getTsTotalSize() + "",
                bean.getTsCurrentSize() + "",
                bean.getTsDownloadState() + ""
//                ,
//                bean.getIsTsSupportRange() + "",
//                bean.getTsSeconds() + ""
        };

        DBHelper.updateInfo(context, true, table, titles, values, Constants.DOWN_M3U8_URL + " =? ", new String[]{bean.getTsUrl()});
    }

    /**
     * 删除M3U8下载视频数据
     *
     * @param url m4u8视频地址
     */
    public synchronized static void DeleteM3U8DownLoadById(Context context, String url) {
        if (!TextUtils.isEmpty(url)) {
            DBHelper.deleteInfo(context, true, table, Constants.DOWN_M3U8_ID + " =? ", new String[]{url});
        }
    }

    /**
     * 删除M3U8下载所有数据
     *
     * @param context
     */
    public synchronized static void DeleteM3U8DownLoadAll(Context context) {
        if (getM3U8DownLoad(context).size() > 0) {
            DBHelper.deleteTableAll(context, table);
        } else {
            Loger.e("DeleteM3U8DownLoadAll: 没有数据可以删除 ");
        }
    }

    /**
     * 查询m3u8Ts集合
     *
     * @param cursor 游标
     * @return m3u8Ts集合
     */
    private synchronized static List<M3U8Ts> getListM3U8Ts(Cursor cursor) {
        List<M3U8Ts> list = new ArrayList<M3U8Ts>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                M3U8Ts bean = new M3U8Ts();
                bean.setM3u8Url(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_ID)));
                bean.setTsName(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_NAME)));
                bean.setTsUrl(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_URL)));
                bean.setTsPath(cursor.getString(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_PATH)));
                bean.setTsTotalSize(cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_FILE_SIZE)));
                bean.setTsCurrentSize(cursor.getLong(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_SIZE)));
                bean.setTsDownloadState(cursor.getInt(cursor
                        .getColumnIndex(Constants.DOWN_M3U8_STATE)));
//                bean.setIsTsSupportRange(cursor.getInt(cursor
//                        .getColumnIndex(Constants.DOWN_M3U8_SUPPORT_RANGE)));
//                bean.setTsSeconds(cursor.getFloat(cursor
//                        .getColumnIndex(Constants.DOWN_M3U8_SECONDS)));
//                Loger.e("bean=" + bean.toString());
                list.add(bean);
            }
            cursor.close();
        }
        return list;
    }
}
