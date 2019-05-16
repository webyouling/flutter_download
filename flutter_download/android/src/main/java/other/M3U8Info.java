package other;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjianzhong on 2017/11/30.
 * M3U8下载信息
 */

public class M3U8Info implements Serializable {
    private static final String TAG = "M3U8Info";
    private String basepath;//临时保存m3u8文本路径
    private List<M3U8Ts> tsList = new ArrayList<>();
    private double totalTime;

    public M3U8Info() {
    }

    public M3U8Info(String basepath, List<M3U8Ts> tsList) {
        this.basepath = basepath;
        this.tsList = tsList;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public void addTotalTime(double seconds) {
        this.totalTime += seconds;
    }

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public List<M3U8Ts> getTsList() {
        return tsList;
    }

    public void setTsList(List<M3U8Ts> tsList) {
        this.tsList = tsList;
    }

    public void addTs(M3U8Ts ts) {
        this.tsList.add(ts);
    }

}
