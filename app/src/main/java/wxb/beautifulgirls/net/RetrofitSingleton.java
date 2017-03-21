package wxb.beautifulgirls.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import wxb.beautifulgirls.constant.Constants;
import wxb.beautifulgirls.data.GankDatas;
import wxb.beautifulgirls.data.GirlDatas;

/**
 * Created by 黑月 on 2017/3/8.
 */

public class RetrofitSingleton {
    private static Retrofit sRetrofit = null;
    private static OkHttpClient sOkHttp = null;
    private static GankApi sGankApi = null;

    private RetrofitSingleton() {
        init();
    }

    public static RetrofitSingleton getInstance() {
        return RetrofitHolder.instance;
    }

    private static class RetrofitHolder {
        private static final RetrofitSingleton instance = new RetrofitSingleton();
    }

    private void init() {
        initOkHttp();
        initRetrofit();
        sGankApi = sRetrofit.create(GankApi.class);
    }

    private void initOkHttp() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        //设置超时
        client.connectTimeout(15, TimeUnit.SECONDS);
        client.readTimeout(20, TimeUnit.SECONDS);
        client.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        client.retryOnConnectionFailure(true);
        sOkHttp = client.build();

    }

    private void initRetrofit() {
        sRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(sOkHttp)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())

                .build();
    }

    public GankApi getGankApi() {
        return sGankApi;
    }

    public Observable<GirlDatas> getGirlDatas(int page) {
        return sGankApi.getGirls(page).compose(RxUtils.rxSchedulerHelper());
    }

    public Observable<GankDatas> getLearnDatas(int year, int month, int day) {
        return sGankApi.getLearnData(year, month, day).compose(RxUtils.rxSchedulerHelper());
    }

}
