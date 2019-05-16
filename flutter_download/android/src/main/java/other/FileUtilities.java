package other;

import android.app.Activity;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Created by lingjianzhong on 2017/12/4.
 */

public class FileUtilities {
    private static final String TAG = "FileUtilities";
    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26; // 10 digits + 26 letters

    /**
     * 获取下载视频保存名称
     *
     * @param url
     * @return
     */
    public static String getMd5FileName(String url) {
        String videoName = null;
        //先获取链接最后一段
        String str = url.substring(url.lastIndexOf("/") + 1, url.length());
        //在判断是否有？
        if (str.contains("?")) {
            //如果有？就再次截断
            videoName = str.substring(0, str.indexOf("?"));
        } else {
            videoName = str;
        }
        if (videoName != null) {
            if (videoName.contains(".m3u8")) {

                StringBuffer sb = new StringBuffer();
                videoName = videoName.substring(0, videoName.indexOf(".m3u8"));

                byte[] md5 = getMD5(videoName.getBytes());
                BigInteger bi = new BigInteger(md5).abs();
                videoName = sb.append(bi.toString(RADIX)).append(System.currentTimeMillis()).append(".ts").toString();
            } else {
                Log.e(TAG, "getMd5FileName: " + videoName);
                String[] strs = videoName.toString().split("\\.");
                Log.e(TAG, "getMd5FileName: strs[0] " + strs.toString());
//                Log.e(TAG, "getMd5FileName: strs[1] " + strs[1]);
                StringBuffer sb = new StringBuffer();
                byte[] md5 = getMD5(strs[0].getBytes());
                BigInteger bi = new BigInteger(md5).abs();
                videoName = sb.append(bi.toString(RADIX)).append(System.currentTimeMillis()).append(".").append(strs[1]).toString();
                Log.e(TAG, "getMd5FileName:  " + videoName);
            }
        }
        Log.e(TAG, "getMd5FileName: videoName " + videoName);
        return videoName;
    }

    /**
     * MD5对视频名称进行加密
     *
     * @param data
     * @return
     */
    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }


    public synchronized static File getDownloadFile(String url) {
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        return new File(downloadDir, FileUtilities.getMd5FileName(url));
    }


    /**
     * 转换文件大小
     *
     * @param fileSize 文件大小
     * @return 格式化
     */
    public static String convertFileSize(long fileSize) {
        if (fileSize <= 0) {
            return "0KB";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件下载速度大小
     *
     * @param fileSize 文件下载大小
     * @return 格式化
     */
    public static String convertSpeedFileSize(double fileSize) {
        if (fileSize <= 0) {
            return "0KB";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        if (fileSize < 1024) {
            fileSizeString = df.format(fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format(fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format(fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format(fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小
     *
     * @param fileSize 文件大小
     * @return 格式化
     */
    public static String convertFileSize_cao(long fileSize) {
        if (fileSize <= 0) {
            return "0KB";
        }
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 对文件重命名
     *
     * @param filePath 原文件路径
     * @param reName   重命名名称
     */
    public static String chageFileName(String filePath, String reName) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            //前面路径必须一样才能修改成功
            Log.e(TAG, "chageFileName: filePath.substring(0, filePath.lastIndexOf(\"/\")+1) " + filePath.substring(0, filePath.lastIndexOf("/") + 1));
            sb.append(filePath.substring(0, filePath.lastIndexOf("/") + 1))
                    .append(reName).append(filePath.substring(filePath.lastIndexOf("."), filePath.length()));
            Log.e(TAG, "chageFileName: 修改 " + sb.toString());
            File newFile = new File(sb.toString());
            Log.e(TAG, "chageFileName: newFile " + newFile.getPath());
            file.renameTo(newFile);
        }
        return sb.toString();
    }

    /**
     * 获取js
     *
     * @param activity
     * @param inFile
     * @return
     */
    public static String LoadData(Activity activity, String inFile) {
        String tContents = "";
        try {
            InputStream stream = activity.getAssets().open(inFile);
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            tContents = new String(buffer);
        } catch (IOException e) {
            // Handle exceptions here
        }
        return tContents;
    }

    /**
     * 删除文件，可以是单个文件或文件夹
     *
     * @param fileName 待删除的文件名
     * @return 文件删除成功返回true, 否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
//           Loger.e("删除文件失败：" + fileName + "文件不存在");
            return false;
        } else {
            if (file.isFile()) {

                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除文件的文件名
     * @return 单个文件删除成功返回true, 否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile() && file.exists()) {
            file.delete();
//           Loger.e("删除单个文件" + fileName + "成功！");
            return true;
        } else {
//           Loger.e("删除单个文件" + fileName + "失败！");
            return false;
        }
    }

    /**
     * 删除多个文件
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param dir 被删除目录的文件路径
     * @return 目录删除成功返回true, 否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        //如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            Loger.e("删除目录失败" + dir + "目录不存在！");
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files!=null) {
            for (int i = 0; i < files.length; i++) {
                //删除子文件
                if (files[i].isFile()) {
                    flag = deleteFile(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
                //删除子目录
                else {
                    flag = deleteDirectory(files[i].getAbsolutePath());
                    if (!flag) {
                        break;
                    }
                }
            }
        }

        if (!flag) {
            Loger.e("删除目录失败");
            return false;
        }

        //删除当前目录
        if (dirFile.delete()) {
            Loger.e("删除目录" + dir + "成功！");
            return true;
        } else {
            Loger.e("删除目录" + dir + "失败！");
            return false;
        }
    }

    public synchronized static boolean isSaveFile(String path) {
        File file = new File(path);
        if (file.exists())
            return true;
        return false;
    }
}