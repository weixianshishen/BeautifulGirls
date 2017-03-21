package wxb.beautifulgirls.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import wxb.beautifulgirls.R;
import wxb.beautifulgirls.ui.adapter.LearnPagerAdapter;

public class LearningActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {


    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private LearnPagerAdapter mAdapter;
    private Date mDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        ButterKnife.bind(this);
        mDate = (Date) getIntent().getSerializableExtra("date");
        initViewPager();
    }


    private void initViewPager() {
        mAdapter = new LearnPagerAdapter(getSupportFragmentManager(), mDate);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
