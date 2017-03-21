package wxb.beautifulgirls.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.data.GankDatas;
import wxb.beautifulgirls.data.entity.Gank;
import wxb.beautifulgirls.net.RetrofitSingleton;
import wxb.beautifulgirls.net.RxUtils;
import wxb.beautifulgirls.ui.adapter.LearnListAdapter;

/**
 * Created by 黑月 on 2017/3/20.
 */

public class GankFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_learn)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_girl)
    ImageView mImgGirl;
    @BindView(R.id.scrollView)
    NestedScrollView mScollView;


    private static final String ARG_YEAR = "year";
    private static final String ARG_MONTH = "month";
    private static final String ARG_DAY = "day";
    int mYear, mMonth, mDay;

    private List<Gank> mList = new ArrayList<>();
    private LearnListAdapter mAdapter;

    public static GankFragment newInstance(int year, int month, int day) {
        GankFragment fragment = new GankFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_YEAR, year);
        args.putInt(ARG_MONTH, month);
        args.putInt(ARG_DAY, day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parseArgument();
    }

    private void parseArgument() {
        Bundle bundle = getArguments();
        mYear = bundle.getInt(ARG_YEAR);
        mMonth = bundle.getInt(ARG_MONTH);
        mDay = bundle.getInt(ARG_DAY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_gank, container, false);
        ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new LearnListAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        initToolbar();
        fetchData();

    }


    private void initToolbar() {
        mToolbar.setTitle(mYear + "/" + mMonth + "/" + mDay);
        mToolbar.setNavigationIcon(R.drawable.icon_return);
        mToolbar.setNavigationOnClickListener(view -> getActivity().finish());
    }

    private void fetchData() {
        RetrofitSingleton.getInstance().getLearnDatas(mYear, mMonth, mDay)
                .map(gankDatas -> gankDatas.results)
                .map(this::addAllResults)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(ganks -> {
                    if (ganks.isEmpty()) {
                        mScollView.setVisibility(View.VISIBLE);
                        mRecyclerView.setVisibility(View.GONE);
                        mImgGirl.setImageDrawable(getResources().getDrawable(R.drawable.img_girl));
                        return;
                    } else {
                        Glide.with(getActivity()).load(ganks.get(ganks.size()-1).getUrl()).into(mImgGirl);
                        mAdapter.addItems(ganks);
                    }

                });

    }

    private List<Gank> addAllResults(GankDatas.Result results) {
        if (results.androidList != null) mList.addAll(results.androidList);
        if (results.iOSList != null) mList.addAll(results.iOSList);
        if (results.拓展资源List != null) mList.addAll(results.拓展资源List);
        if (results.休息视频List != null) mList.addAll(results.休息视频List);
        if (results.AppList != null) mList.addAll(results.AppList);
        if (results.瞎推荐List != null) mList.addAll(results.瞎推荐List);
        if (results.妹纸List != null) mList.addAll(results.妹纸List);
        return mList;
    }

}
