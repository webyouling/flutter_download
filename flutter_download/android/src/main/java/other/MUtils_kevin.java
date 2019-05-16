package other;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lingjianzhong on 2017/11/20.
 * m3u8解析和合并工具
 */
public class MUtils_kevin {

    private static String sM3U8Url;
    private static String sTsPath;

    /**
     * 第一次 M3U8解析
     * 断网后：解析TS失败：java.net.UnknownHostException: Unable to resolve host "vid-egc.xvideos-cdn.com": No address associated with hostname
     *
     * @param url m3u8的url
     */
    public static ArrayList<M3U8Ts> parseIndex(String url, String path, boolean b, Activity activity) {
        if (b) {
            sM3U8Url = url;
            sTsPath = path;
        }

        ArrayList<M3U8Ts> tsArrayList = new ArrayList<>();
        BufferedReader reader;
        String subBaseUrl = url.substring(0, url.lastIndexOf("/") + 1);//原始URL
        try {
            reader = new BufferedReader(new InputStreamReader(new URL(url).openStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    if (line.startsWith("#EXTINF:")) {
                        line = line.substring(8);
                        if (line.endsWith(",")) {
                            line = line.substring(0, line.length() - 1);
                        }
                    }
                    continue;
                }
                if (line.contains(".m3u8")) {
                    if (!line.contains("/")) {
                        return parseIndex(subBaseUrl + line, path, false,activity);
                    } else if (line.startsWith("http://") || line.startsWith("https://")) {
                        return parseIndex(line, path, false,activity);
                    } else {
                        String m3u8Url = spliceLink(subBaseUrl, line);
                        return parseIndex(m3u8Url, path, false,activity);
                    }
                } else if (line.contains(".ts")) {
                    if (!line.contains("/")) {
                        String tsName = sTsPath + File.separator + System.currentTimeMillis() + ".ts";
                        M3U8Ts m3U8Ts = new M3U8Ts(sM3U8Url, subBaseUrl + line, tsName, sTsPath);
                        // TODO: 2019/3/15 上下文
                        M3U8DataBeanUtil.insertM3U8Down(activity, m3U8Ts);
                        tsArrayList.add(m3U8Ts);
                    } else if (line.startsWith("http://") || line.startsWith("https://")) {
                        String tsName = sTsPath + File.separator + System.currentTimeMillis() + ".ts";
                        M3U8Ts m3U8Ts = new M3U8Ts(sM3U8Url, subBaseUrl + line, tsName, sTsPath);
                        M3U8DataBeanUtil.insertM3U8Down(activity, m3U8Ts);
                        tsArrayList.add(m3U8Ts);
                    } else {
                        String tsUrl = spliceLink(subBaseUrl, line);
                        String tsName = sTsPath + File.separator + System.currentTimeMillis() + ".ts";
                        M3U8Ts m3U8Ts = new M3U8Ts(sM3U8Url, tsUrl, tsName, sTsPath);
                        M3U8DataBeanUtil.insertM3U8Down(activity, m3U8Ts);
                        tsArrayList.add(m3U8Ts);
                    }
                }
            }
        } catch (IOException e) {
            Loger.e("解析TS失败：" + e);
        }
        return tsArrayList;
    }


    /**
     * 用主域名进行拼接
     * 将字符串以/切分并存到数组中
     *
     * @param basepath m3u8文本链接
     * @param line     ts或子m3u8链接
     * @return
     */
    private static String spliceLink(String basepath, String line) {
        StringBuffer sb = new StringBuffer();
        String url = null;
        String[] linelit = line.split("/");
        //获取主域名
        Pattern p = Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(basepath);
        matcher.find();
        if (line.startsWith("/")) {
            if (basepath.contains(linelit[1])) {
                url = sb.append(basepath.substring(0, basepath.indexOf(":") + 3).trim())
                        .append(matcher.group().trim()).append(line.trim()).toString().trim();
            } else {
                url = sb.append(basepath.substring(0, basepath.lastIndexOf("/")).trim())
                        .append(line.trim()).toString().trim();
            }
        } else {
            if (basepath.contains(linelit[0])) {
                url = sb.append(basepath.substring(0, basepath.indexOf(":") + 3).trim())
                        .append(matcher.group().trim())
                        .append("/")
                        .append(line.trim()).toString().trim();
            } else {
                url = sb.append(basepath.substring(0, basepath.lastIndexOf("/") + 1).trim())
                        .append(line.trim()).toString().trim();
            }
        }
        return url;
    }
//
//
//    /**
//     * 将M3U8对象的所有ts切片合并为1个
//     *
//     * @param m3u8
//     * @param tofile
//     * @throws IOException
//     */
//    public static void merge(M3U8Info m3u8, String tofile, String basePath) {
//        //List<M3U8Ts> mergeList = getLimitM3U8Ts(m3u8);
//        List<M3U8Ts> mergeList = m3u8.getTsList();
////        Log.e(TAG, "merge: mergeList " + mergeList.size());
//        File saveFile = new File(tofile);
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(saveFile);
//            File file;
//            for (M3U8Ts ts : mergeList) {
//                file = new File(basePath, ts.getTsName());
////                Log.e(TAG, "merge: 合并的第  " + ts.getTsName() + " 个 视频路径为 " + file);
//                if (file.isFile() && file.exists()) {
//                    IOUtils.copyLarge(new FileInputStream(file), fos);
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fos != null) {
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//
//    /**
//     * 移动文件
//     *
//     * @param sFile
//     * @param tFile
//     */
//    public static void moveFile(String sFile, String tFile) {
//        try {
////            Log.e(TAG, "moveFile: 移动");
//            FileUtils.moveFile(new File(sFile), new File(tFile));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 清空文件夹
//     */
//    public static void clearDir(File dir) {
//        if (dir.exists()) {// 判断文件是否存在
//            if (dir.isFile()) {// 判断是否是文件
//                dir.delete();// 删除文件
//                Loger.e("clearDir: dir" + dir.getName());
//            } else if (dir.isDirectory()) {// 否则如果它是一个目录
//                File[] files = dir.listFiles();// 声明目录下所有的文件 files[];
//                Loger.e("clearDir: files " + files.length);
//
//                for (int i = 0; i < files.length; i++) {// 遍历目录下所有的文件
//                    Loger.e("clearDir: file " + files[i]);
//                    clearDir(files[i]);// 把每个文件用这个方法进行迭代
//                }
//                dir.delete();// 删除文件夹
//            }
//        }
//    }
}
