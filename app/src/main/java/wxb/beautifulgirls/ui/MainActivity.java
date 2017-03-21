package wxb.beautifulgirls.ui;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import rx.Observable;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.data.GirlDatas;
import wxb.beautifulgirls.data.VideoDatas;
import wxb.beautifulgirls.data.entity.Girl;
import wxb.beautifulgirls.db.DbManager;
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
    /**
     * 数据库管理操作
     */
    private DbManager mDbManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setToolbar(false);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new GirlsListAdapter(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
        mRefreshLayout.setOnRefreshListener(() -> {
            mPage = 1;
            loadFromNet(true);
        });
    }

    @Override
    protected void initData() {
        mDbManager = new DbManager(this);
        List<Girl> girls = mDbManager.query(Girl.class);
        if (girls.size() > 0) {
            //从数据库中加载
            mPage = 2;
            loadFromDb(girls);
        } else {
            loadFromNet(false);
        }


    }

    private void loadFromDb(List<Girl> girls) {
        Observable.from(girls).toList().subscribe(girls1 -> mAdapter.addItems(girls1));
    }

    private void loadFromNet(boolean isClean) {
        GankApi gankApi = RetrofitSingleton.getInstance().getGankApi();
        Observable.zip(gankApi.getGirls(mPage), gankApi.getVideos(mPage), this::getGirlDatas)
                .compose(RxUtils.rxSchedulerHelper())
                .map(girlDatas -> girlDatas.results)
                .flatMap(Observable::from)
                .toSortedList((girl, girl2) -> girl2.getPublishedAt().compareTo(girl.getPublishedAt()))
                .doOnNext(girls -> {
                    if (mPage == 1) {
                        Log.i("feng", "存数据");
                        saveDatas(girls);
                    }
                })
                .doOnRequest(aLong -> mRefreshLayout.setRefreshing(true))
                .subscribe(girls -> {
                    if (isClean) mAdapter.cleanItems();
                    mAdapter.addItems(girls);
                    mRefreshLayout.setRefreshing(false);
                });
    }

    private void saveDatas(List<Girl> girls) {
        mDbManager.deleteAll(Girl.class);
        for (int i = 0; i < girls.size(); i++) {
            Girl girl = new Girl();
            girl.setUrl(girls.get(i).getUrl());
            girl.setDesc(girls.get(i).getDesc());
            girl.setPublishedAt(girls.get(i).getPublishedAt());
            mDbManager.insert(girl);
        }
    }

    private GirlDatas getGirlDatas(GirlDatas girldatas, VideoDatas gankDatas) {
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < girldatas.results.size(); i++) {
            String desc1 = girldatas.results.get(i).getDesc();
            String desc2 = gankDatas.results.get(i).getDesc();
            strBuffer.setLength(0);
            strBuffer.append(desc1);
            strBuffer.append("\t");
            strBuffer.append(desc2);
            girldatas.results.get(i).setDesc(strBuffer.toString());
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
                        loadFromNet(false);
                    } else {
                        mIsFirstTimeTouchBottom = false;
                    }
                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_trending:
                openGitHubTrending();
                return true;
            case R.id.action_notifiable:
                showToast("点击2");
                return true;
            case R.id.action_about:
                startActivity(AboutActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openGitHubTrending() {
        String url = getString(R.string.url_github_trending);
        String title = getString(R.string.action_github_trending);
        Intent intent = WebActivity.newIntent(this, url, title);
        startActivity(intent);
    }

}
