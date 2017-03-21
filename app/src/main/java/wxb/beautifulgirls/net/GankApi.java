package wxb.beautifulgirls.net;


import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import wxb.beautifulgirls.data.GankDatas;
import wxb.beautifulgirls.data.GirlDatas;
import wxb.beautifulgirls.data.VideoDatas;

/**
 * Created by 黑月 on 2017/3/10.
 */

public interface GankApi {
    @GET("data/福利/10/{page}")
    Observable<GirlDatas> getGirls(@Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Observable<GankDatas> getLearnData(
            @Path("year") int year,
            @Path("month") int month,
            @Path("day") int day);

    @GET("data/休息视频/10/{page}")
    Observable<VideoDatas> getVideos(@Path("page") int page);


}
