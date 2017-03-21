package wxb.beautifulgirls.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import wxb.beautifulgirls.data.entity.Gank;

/**
 * Created by 黑月 on 2017/3/21.
 */

public class GankDatas {
    public Result results;
    public List<String> category;

    public class Result {
        @SerializedName("Android")
        public List<Gank> androidList;
        @SerializedName("休息视频")
        public List<Gank> 休息视频List;
        @SerializedName("iOS")
        public List<Gank> iOSList;
        @SerializedName("福利")
        public List<Gank> 妹纸List;
        @SerializedName("拓展资源")
        public List<Gank> 拓展资源List;
        @SerializedName("瞎推荐")
        public List<Gank> 瞎推荐List;
        @SerializedName("App")
        public List<Gank> AppList;

    }
}
