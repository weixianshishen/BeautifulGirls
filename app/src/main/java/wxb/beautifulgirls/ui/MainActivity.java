package wxb.beautifulgirls.ui;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import butterknife.BindView;
import rx.Observable;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.data.GirlDatas;
import wxb.beautifulgirls.data.VideoDatas;
import wxb.beautifulgirls.net.GankApi;
import wxb.beautifulgirls.net.RetrofitSingleton;
import wxb.beautifulgirls.net.RxUtils;
import wxb.beautifulgirls.ui.adapter.GirlsListAdapter;
import wxb.beautifulgirls.ui.base.BaseActivity;


public class MainActivity extends BaseActivity {
    @BindView(R.id.rv_girls)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout mRefreshLayout;
    private GirlsListAdapter mAdapter;
    private int mPage = 1;
    private static final int PRELOAD_SIZE = 6;
    private boolean mIsFirstTimeTouchBottom = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolbar(false, "漂亮美眉");
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new GirlsListAdapter(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
        mRefreshLayout.setOnRefreshListener(() -> {
            mPage = 1;
            load(true);
        });
    }

    @Override
    protected void initData() {
        load(false);
    }

    private void load(boolean isClean) {
        GankApi gankApi = RetrofitSingleton.getInstance().getGankApi();
        Observable.zip(gankApi.getGirls(mPage), gankApi.getVideos(mPage), this::getGirlDatas)
                .compose(RxUtils.rxSchedulerHelper())
                .map(girlDatas -> girlDatas.results)
                .flatMap(Observable::from)
                .toSortedList((girl, girl2) -> girl2.getPublishedAt().compareTo(girl.getPublishedAt()))
                .doOnRequest(aLong -> mRefreshLayout.setRefreshing(true))
                .subscribe(girls -> {
                    if (isClean) mAdapter.cleanItems();
                    mAdapter.addItems(girls);
                    new Handler().postDelayed(() -> mRefreshLayout.setRefreshing(false), 400);
                });
    }

    private GirlDatas getGirlDatas(GirlDatas girldatas, VideoDatas gankDatas) {
        for (int i = 0; i < girldatas.results.size(); i++) {
            girldatas.results.get(i).setDesc(gankDatas.results.get(i).getDesc());
        }
        return girldatas;
    }

    RecyclerView.OnScrollListener getOnBottomListener(StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                boolean isBottom =
                        layoutManager.findLastCompletelyVisibleItemPositions(new int[2])[1] >=
                                mAdapter.getItemCount() - PRELOAD_SIZE;
                if (!mRefreshLayout.isRefreshing() && isBottom) {
                    if (!mIsFirstTimeTouchBottom) {
                        mRefreshLayout.setRefreshing(true);
                        mPage += 1;
                        load(false);
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

}
